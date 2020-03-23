/*
 * Copyright 2019 ViiSE.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.lightsearch.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.cmd.manager.task.v2.NetworkAsyncTask;
import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.DeliveryTypeEnum;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.UnitsEnum;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojo;
import ru.viise.lightsearch.data.pojo.ConfirmTypes;
import ru.viise.lightsearch.data.v2.CloseSoftCheckCommandSimple;
import ru.viise.lightsearch.data.v2.CloseSoftCheckCommandWithCardCode;
import ru.viise.lightsearch.data.v2.CloseSoftCheckCommandWithDelivery;
import ru.viise.lightsearch.data.v2.CloseSoftCheckCommandWithToken;
import ru.viise.lightsearch.data.v2.CloseSoftCheckCommandWithUserIdentifier;
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
import ru.viise.lightsearch.dialog.alert.InfoProductAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.InfoProductAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.UnconfirmedRecordAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.UnconfirmedRecordAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.fragment.adapter.RecyclerViewAdapter;
import ru.viise.lightsearch.fragment.adapter.SwipeToDeleteCallback;
import ru.viise.lightsearch.fragment.adapter.SwipeToInfoCallback;
import ru.viise.lightsearch.fragment.snackbar.SnackbarSoftCheckCreator;
import ru.viise.lightsearch.fragment.snackbar.SnackbarSoftCheckCreatorInit;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class CartFragment extends Fragment implements ICartFragment {

    public static final String TAG = "cartFragment";

    private final String NO                   = DeliveryTypeEnum.NO.stringUIValue();
//    private final String DOSTAVKA_SO_SKLADOV  = DeliveryTypeEnum.DOSTAVKA_SO_SKLADOV.stringUIValue();
//    private final String SAMOVYVOZ_SO_SKLADOV = DeliveryTypeEnum.SAMOVYVOZ_SO_SKLADOV.stringUIValue();
    private final String SAMOVYVOZ_S_TK       = DeliveryTypeEnum.SAMOVYVOZ_S_TK.stringUIValue();

    private final String PREF = "pref";

    private ManagerActivityHandler managerActivityHandler;

    private List<SoftCheckRecord> cartRecords;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private TextView tvTotalAmount;
    private Spinner spinnerDeliveryType;
    private AlertDialog queryDialog;
    private Button closeSoftCheckButton;

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        closeSoftCheckButton = view.findViewById(R.id.buttonCloseSC);
        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();

        recyclerView = view.findViewById(R.id.recyclerViewCart);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        TextView tvTotalCost = view.findViewById(R.id.textViewCartTotalCostDynamic);
        tvTotalAmount = view.findViewById(R.id.textViewCartTotalAmountDynamic);

        spinnerDeliveryType = view.findViewById(R.id.spinnerCartDeliveryType);
        fillSpinnerDeliveryType();

        initRecycleView(tvTotalCost);
        initSwipeToDeleteAndUndo();
        initSwipeToInfo();

        closeSoftCheckButton.setOnClickListener((view1) -> {
            if(adapter.getItemCount() == 0) {
                Toast t = Toast.makeText(this.getActivity().getApplicationContext(), R.string.toast_empty_cart, Toast.LENGTH_LONG);
                t.show();
            } else if(spinnerDeliveryType.getSelectedItem().toString().equals(NO)) {
                Toast t = Toast.makeText(this.getActivity().getApplicationContext(), R.string.toast_delivery_not_chosen, Toast.LENGTH_LONG);
                t.show();
            } else {
                boolean isRun = true;

                for(SoftCheckRecord record: adapter.getData()) {
                    if(record.currentAmount() == 0.0f) {
                        Toast t = Toast.makeText(this.getActivity().getApplicationContext(), R.string.toast_cart_with_empty_products, Toast.LENGTH_LONG);
                        t.show();
                        isRun = false;
                        break;
                    }
                }

                if(isRun) {
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
                                                            ), adapter.getData()
                                                    ), adapter.getData()
                                                    .stream()
                                                    .map(rec ->
                                                            new ProductWithId(
                                                                    new ProductWithAmount(
                                                                            new ProductSimple(),
                                                                            rec.currentAmount()
                                                                    ),
                                                                    rec.barcode()
                                                            ))
                                                    .collect(Collectors.toList())
                                            ), ConfirmTypes.CART
                                    ), prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER)
                            ), prefManager.load(PreferencesManagerType.CARD_CODE_MANAGER));

                    NetworkAsyncTask<ConfirmSoftCheckProductsPojo> networkAsyncTask = new NetworkAsyncTask<>(
                            managerActivityHandler,
                            queryDialog);

                    networkAsyncTask.execute(command);
                }
            }
        });

        checkUnconfirmedRecord();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityHandler = (ManagerActivityHandler) this.getActivity();
    }

    private void fillSpinnerDeliveryType() {
        String[] data = new String[2];
        data[0] = NO;
//        data[1] = DOSTAVKA_SO_SKLADOV;
//        data[2] = SAMOVYVOZ_SO_SKLADOV;
        data[1] = SAMOVYVOZ_S_TK;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                R.layout.spinner_cart_delivery_type, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDeliveryType.setAdapter(adapter);
    }

    public void init(List<SoftCheckRecord> cartRecords) {
        this.cartRecords = cartRecords;
    }

    @SuppressLint("SetTextI18n")
    private void initRecycleView(TextView tvTotalCost) {
        adapter = new RecyclerViewAdapter(this.getContext(), cartRecords, tvTotalCost,
                UnitsEnum.CURRENT_PRICE_UNIT.stringValue());
        recyclerView.setAdapter(adapter);
        tvTotalAmount.setText(adapter.getItemCount() + " " + UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue());
    }

    private void initSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this.getContext()) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final SoftCheckRecord item = adapter.getData().get(position);
                adapter.removeItem(position);
                tvTotalAmount.setText(String.format("%d %s", adapter.getItemCount(), CartFragment.this.getString(R.string.current_amount_cart_unit)));

                SnackbarSoftCheckCreator snackbarCr = SnackbarSoftCheckCreatorInit.snackbarSoftCheckCreator(
                        CartFragment.this, closeSoftCheckButton, CartFragment.this.getString(R.string.snackbar_prod_deleted));
                Snackbar snackbar = snackbarCr.createSnackbar().setAction(CartFragment.this.getString(R.string.snackbar_cancel), view -> {
                    adapter.restoreItem(item, position);
                    if(position == (adapter.getItemCount() - 1) || position == 0)
                        recyclerView.scrollToPosition(position);
                    tvTotalAmount.setText(String.format("%d %s", adapter.getItemCount(), CartFragment.this.getString(R.string.current_amount_cart_unit)));
                });
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onShown(Snackbar sb) {
                        closeSoftCheckButton.animate().translationY(-70.f).setDuration(100);
                    }

                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        closeSoftCheckButton.animate().translationY(10.f).setDuration(100);
                    }
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
                                (CartRecord)adapter.getItem(position));
                infoProdADCr.create().show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToInfoCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    @SuppressWarnings("unchecked")
    private void tryToCloseSoftCheck() {
        if(checkUnconfirmedRecord()) {
            DeliveryTypeEnum deliveryType = getDeliveryType();
            SharedPreferences sPref = this.getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

            Command<CloseSoftCheckPojo> command = new CloseSoftCheckCommandWithUserIdentifier(
                    new CloseSoftCheckCommandWithCardCode(
                            new CloseSoftCheckCommandWithDelivery(
                                    new CloseSoftCheckCommandWithToken(
                                            new CloseSoftCheckCommandSimple(),
                                            prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                                    ), deliveryType.stringCommandValue()
                            ), prefManager.load(PreferencesManagerType.CARD_CODE_MANAGER)
                    ), prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER));

            NetworkAsyncTask<CloseSoftCheckPojo> networkAsyncTask = new NetworkAsyncTask<>(
                    managerActivityHandler,
                    queryDialog);
            networkAsyncTask.execute(command);
        }
    }

    private boolean checkUnconfirmedRecord() {
        int position = 0;
        for(SoftCheckRecord record : cartRecords) {
            CartRecord cartRecord = (CartRecord) record;
            if(!cartRecord.isConfirmed()) {
                adapter.refreshItem(position, cartRecord);
                callDialogUnconfirmed();
                return false;
            }
            position++;
        }
        return true;
    }

    private void callDialogUnconfirmed() {
        UnconfirmedRecordAlertDialogCreator unconRecADCr =
                UnconfirmedRecordAlertDialogCreatorInit.unconfirmedRecordAlertDialogCreator(this.getActivity());
        unconRecADCr.create().show();
    }

    private DeliveryTypeEnum getDeliveryType() {
//        String delivery = spinnerDeliveryType.getSelectedItem().toString();
//        if(delivery.equals(DOSTAVKA_SO_SKLADOV))
//            return DeliveryTypeEnum.DOSTAVKA_SO_SKLADOV;
//        else if(delivery.equals(SAMOVYVOZ_SO_SKLADOV))
//            return DeliveryTypeEnum.SAMOVYVOZ_SO_SKLADOV;
//        else
            return DeliveryTypeEnum.SAMOVYVOZ_S_TK;
    }

    @Override
    public void refreshCartRecords(List<SoftCheckRecord> cartRecords) {
        this.cartRecords = cartRecords;
        adapter.notifyDataSetChanged();
        tryToCloseSoftCheck();
    }
}
