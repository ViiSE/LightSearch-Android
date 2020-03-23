package ru.viise.lightsearch.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;

import java.util.List;
import java.util.stream.Collectors;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.KeyboardHideToolInit;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.scan.ScannerInit;
import ru.viise.lightsearch.cmd.manager.task.v2.NetworkAsyncTask;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.UnitsEnum;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojo;
import ru.viise.lightsearch.data.pojo.ConfirmTypes;
import ru.viise.lightsearch.data.pojo.SearchSoftCheckPojo;
import ru.viise.lightsearch.data.v2.Command;
import ru.viise.lightsearch.data.v2.ConfirmSoftCheckProductsCommandSimple;
import ru.viise.lightsearch.data.v2.ConfirmSoftCheckProductsCommandWithCardCode;
import ru.viise.lightsearch.data.v2.ConfirmSoftCheckProductsCommandWithData;
import ru.viise.lightsearch.data.v2.ConfirmSoftCheckProductsCommandWithSoftCheckRecords;
import ru.viise.lightsearch.data.v2.ConfirmSoftCheckProductsCommandWithToken;
import ru.viise.lightsearch.data.v2.ConfirmSoftCheckProductsCommandWithType;
import ru.viise.lightsearch.data.v2.ConfirmSoftCheckProductsCommandWithUserIdentifier;
import ru.viise.lightsearch.data.v2.ProductSimple;
import ru.viise.lightsearch.data.v2.ProductWithAmount;
import ru.viise.lightsearch.data.v2.ProductWithId;
import ru.viise.lightsearch.data.v2.SearchSoftCheckCommandSimple;
import ru.viise.lightsearch.data.v2.SearchSoftCheckCommandWithBarcode;
import ru.viise.lightsearch.data.v2.SearchSoftCheckCommandWithToken;
import ru.viise.lightsearch.data.v2.SearchSoftCheckCommandWithUsername;
import ru.viise.lightsearch.dialog.alert.InfoProductAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.InfoProductAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.adapter.RecyclerViewAdapter;
import ru.viise.lightsearch.fragment.adapter.SwipeToDeleteCallback;
import ru.viise.lightsearch.fragment.adapter.SwipeToInfoCallback;
import ru.viise.lightsearch.fragment.snackbar.SnackbarSoftCheckCreator;
import ru.viise.lightsearch.fragment.snackbar.SnackbarSoftCheckCreatorInit;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class SoftCheckFragment extends Fragment implements ISoftCheckFragment {

    public static final String TAG = "softCheckFragment";

    private final String PREF = "pref";

    private ManagerActivityUI managerActivityUI;
    private ManagerActivityHandler managerActivityHandler;

    private List<SoftCheckRecord> softCheckRecords;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private CounterFab counterFabCart;

    private AlertDialog queryDialog;
    private EditText editTextSearch;

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soft_check, container, false);

        counterFabCart = view.findViewById(R.id.counterFabCart);

        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();
        AppCompatImageButton barcodeButton = view.findViewById(R.id.imageButtonBarcode);
        editTextSearch = view.findViewById(R.id.editTextSearchSC);

        Animation animAlpha = AnimationUtils.loadAnimation(this.getActivity(), R.anim.alpha);

        recyclerView = view.findViewById(R.id.recyclerViewSoftCheck);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        TextView tvTotalCost = view.findViewById(R.id.textViewSoftCheckTotalCost);

        initRecyclerView(tvTotalCost);
        initSwipeToDeleteAndUndo();
        initSwipeToInfo();

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String barcode = editTextSearch.getText().toString();

                if(barcode.length() < 2) {
                    Toast t = Toast.makeText(this.getActivity().getApplicationContext(), R.string.toast_barcode_not_enough_symbols, Toast.LENGTH_LONG);
                    t.show();
                } else
                    setSoftCheckBarcode(barcode);

                editTextSearch.clearFocus();
                v.requestFocus();

                return true;
            }
            return false;
        });

        barcodeButton.setOnClickListener(view2 -> {
            view2.startAnimation(animAlpha);

            managerActivityUI.setScanType(ScanType.SEARCH_SOFT_CHECK);
            ScannerInit.scanner(this.getActivity()).scan();

            KeyboardHideToolInit.keyboardHideTool(this.getActivity()).hideKeyboard();

            editTextSearch.clearFocus();
            barcodeButton.requestFocus();
        });

        counterFabCart.setOnClickListener(view3 -> {
            view3.startAnimation(animAlpha);
            if(adapter.getItemCount() == 0) {
                Toast t = Toast.makeText(this.getActivity().getApplicationContext(), R.string.toast_empty_soft_check, Toast.LENGTH_LONG);
                t.show();
            } else {
                SharedPreferences sPref = this.getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
                PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

                Command<ConfirmSoftCheckProductsPojo> command = new ConfirmSoftCheckProductsCommandWithCardCode(
                            new ConfirmSoftCheckProductsCommandWithUserIdentifier(
                                    new ConfirmSoftCheckProductsCommandWithType(
                                            new ConfirmSoftCheckProductsCommandWithData(
                                                    new ConfirmSoftCheckProductsCommandWithSoftCheckRecords(
                                                            new ConfirmSoftCheckProductsCommandWithToken(
                                                                    new ConfirmSoftCheckProductsCommandSimple(),
                                                                    prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                                                            ),
                                                            adapter.getData()
                                                    ),
                                                    adapter.getData()
                                                            .stream()
                                                            .map(rec ->
                                                                    new ProductWithId(
                                                                            new ProductWithAmount(
                                                                                    new ProductSimple(),
                                                                                    rec.currentAmount()
                                                                            ),
                                                                            rec.barcode()
                                                                    )).collect(Collectors.toList())
                                            ),
                                            ConfirmTypes.SOFT_CHECK
                                    ),
                                    prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER)
                            ),
                            prefManager.load(PreferencesManagerType.CARD_CODE_MANAGER));

                NetworkAsyncTask<ConfirmSoftCheckProductsPojo> networkAsyncTask = new NetworkAsyncTask<>(
                        managerActivityHandler,
                        queryDialog);

                networkAsyncTask.execute(command);
            }

        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityUI = (ManagerActivityUI) this.getActivity();
        managerActivityHandler = (ManagerActivityHandler) this.getActivity();
    }

    public void init(List<SoftCheckRecord> softCheckRecords) {
        this.softCheckRecords = softCheckRecords;
    }

    private void initRecyclerView(TextView tvTotalCost) {
        adapter = new RecyclerViewAdapter(this.getContext(), softCheckRecords, tvTotalCost,
                UnitsEnum.CURRENT_PRICE_UNIT.stringValue());
        recyclerView.setAdapter(adapter);
        counterFabCart.setCount(adapter.getItemCount());
    }

    private void initSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final SoftCheckRecord item = adapter.getData().get(position);
                adapter.removeItem(position);
                counterFabCart.decrease();

                SnackbarSoftCheckCreator snackbarCr = SnackbarSoftCheckCreatorInit.snackbarSoftCheckCreator(
                        SoftCheckFragment.this, counterFabCart, SoftCheckFragment.this.getString(R.string.snackbar_prod_deleted));
                Snackbar snackbar = snackbarCr.createSnackbar().setAction(SoftCheckFragment.this.getString(R.string.snackbar_cancel), view -> {
                    adapter.restoreItem(item, position);
                    if(position == (adapter.getItemCount() - 1) || position == 0)
                        recyclerView.scrollToPosition(position);
                    counterFabCart.increase();
                });
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void initSwipeToInfo() {
        SwipeToInfoCallback swipeToInfoCallback = new SwipeToInfoCallback(this.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                adapter.notifyItemChanged(position);
                InfoProductAlertDialogCreator infoProdADCr =
                        InfoProductAlertDialogCreatorInit.infoProductAlertDialogCreator(getActivity(),
                                adapter.getItem(position));
                infoProdADCr.create().show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToInfoCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSoftCheckBarcode(String barcode) {
        editTextSearch.setText(barcode);
        KeyboardHideToolInit.keyboardHideTool(this.getActivity()).hideKeyboard();

        SharedPreferences sPref = this.getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

        Command<SearchSoftCheckPojo> command = new SearchSoftCheckCommandWithUsername(
                new SearchSoftCheckCommandWithToken(
                        new SearchSoftCheckCommandWithBarcode(
                                new SearchSoftCheckCommandSimple(),
                                barcode
                        ), prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                ), prefManager.load(PreferencesManagerType.USERNAME_MANAGER));

//        Command<SearchPojo> command = new SearchCommandWithType(
//                new SearchCommandWithSklad(
//                        new SearchCommandWithTK(
//                                new SearchCommandWithBarcode(
//                                        new SearchCommandWithToken(
//                                            new SearchCommandSimple(),
//                                            prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
//                                        ), barcode
//                                ), "all"
//                        ), "all"
//                ), SearchCommandType.SOFT_CHECK);

        NetworkAsyncTask<SearchSoftCheckPojo> networkAsyncTask = new NetworkAsyncTask<>(
                managerActivityHandler,
                queryDialog);
        networkAsyncTask.execute(command);
    }

    @Override
    public void addSoftCheckRecord(SoftCheckRecord record) {
        adapter.addItem(record);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        counterFabCart.increase();
        SnackbarSoftCheckCreator snackbarCr = SnackbarSoftCheckCreatorInit.snackbarSoftCheckCreator(
                SoftCheckFragment.this, counterFabCart, SoftCheckFragment.this.getString(R.string.snackbar_prod_added));
        Snackbar snackbar = snackbarCr.createSnackbar();
        snackbar.show();
    }

    @Override
    public void switchToCart(List<SoftCheckRecord> cartRecords) {
        CartFragment cartFragment = new CartFragment();
        cartFragment.init(cartRecords);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_sc, cartFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(CartFragment.TAG);
        transaction.commit();
        this.getActivity().setTitle(R.string.fragment_cart);
    }
}
