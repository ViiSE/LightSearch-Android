
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.KeyboardHideToolInit;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.scan.ScannerZXingImpl;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.entity.BindCheckCommandSimple;
import ru.viise.lightsearch.data.entity.BindCheckCommandWithBarcode;
import ru.viise.lightsearch.data.entity.BindCheckCommandWithCheckEAN13;
import ru.viise.lightsearch.data.entity.BindCheckCommandWithFactoryBarcode;
import ru.viise.lightsearch.data.entity.BindCheckCommandWithSelected;
import ru.viise.lightsearch.data.entity.BindCheckCommandWithToken;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckPojoResult;
import ru.viise.lightsearch.data.pojo.BindPojo;
import ru.viise.lightsearch.data.pojo.BindPojoResult;
import ru.viise.lightsearch.dialog.alert.BindCheckNoResultAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorBindImpl;
import ru.viise.lightsearch.dialog.alert.ReconnectAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManager;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerImpl;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class BindingFragment extends Fragment implements IBindingFragment, NetworkCallback<BindCheckPojo, BindCheckPojoResult> {

    private final static String MODE = "mode";
    private final static String SEARCH_MODE = "searchMode";
    private final static String FACTORY_BARCODE = "factoryBarcode";

    private String factoryBarcode = "";
    private int selected = 0; // 0 - CheckBindingMode, 1 - BindingMode, 2 - BindingDone
    private int searchMode = 0; // 0 - Keyboard typing, 1 - barcode
    private boolean isCheckEan13 = true;

    private AlertDialog queryDialog;
    private EditText searchEditText;
    private TextView textViewFactoryBarcode;
    private CardView bindingOKCardView;

    private ManagerActivityUI managerActivityUI;

    private LinearLayout linearLayoutBindFactoryBarcode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            selected = savedInstanceState.getInt(MODE);
            searchMode = savedInstanceState.getInt(SEARCH_MODE);
            factoryBarcode = savedInstanceState.getString(FACTORY_BARCODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binding, container, false);

        bindingOKCardView = view.findViewById(R.id.cardViewBindingContainerOK);
        searchEditText = view.findViewById(R.id.editTextSearchBinding);
        textViewFactoryBarcode = view.findViewById(R.id.textViewBindBarcode);
        linearLayoutBindFactoryBarcode = view.findViewById(R.id.linearLayoutBindFactoryBarcode);
        FloatingActionButton barcodeButton = view.findViewById(R.id.floatingActionButtonBindingBarcode);

        if(selected == 0) {
            linearLayoutBindFactoryBarcode.setVisibility(View.GONE);
            searchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if(selected == 1) {
            linearLayoutBindFactoryBarcode.setVisibility(View.VISIBLE);
            textViewFactoryBarcode.setText(factoryBarcode);
            searchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if(selected == 2) {
            switchToCheckBind();
        }

        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();

        barcodeButton.setOnClickListener(view2 -> {
            KeyboardHideToolInit.keyboardHideTool(this.getActivity()).hideKeyboard();

            searchEditText.clearFocus();
            view2.requestFocus();

            managerActivityUI.setScanType(ScanType.SEARCH_BIND);
            new ScannerZXingImpl(this.getActivity()).scan();
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String input = searchEditText.getText().toString();

                if(input.length() < 2) {
                    Toast t = Toast.makeText(Objects.requireNonNull(this.getActivity()).getApplicationContext(),
                            "Введите не менее двух символов!", Toast.LENGTH_LONG);
                    t.show();
                } else {
                    run();
                }
                v.requestFocus();

                return true;
            }
            return false;
        });

        searchMode = 1;
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (selected == 0) {
                    if(searchMode == 0) {
                        String input = searchEditText.getText().toString();
                        if (searchEditText.getText().toString().length() == 13) {
                            factoryBarcode = input;
                            textViewFactoryBarcode.setText("");
                            isCheckEan13 = true;

                            KeyboardHideToolInit.keyboardHideTool(getActivity()).hideKeyboard();
                            sendBindCheck(input);
                        }
                    } else
                        searchMode = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MODE, selected);
        outState.putInt(SEARCH_MODE, searchMode);
        outState.putString(FACTORY_BARCODE, factoryBarcode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityUI = (ManagerActivityUI) this.getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setSearchBarcode(String barcode) {
        searchMode = 1;
        searchEditText.setText(barcode);
    }

    @Override
    public void setSearchBarcodeAndRun(String barcode) {
        searchMode = 1;
        searchEditText.setText(barcode);
        run();
    }

    private void run() {
        String input = searchEditText.getText().toString();

        if(input.length() < 2) {
            Toast t = Toast.makeText(Objects.requireNonNull(this.getActivity()).getApplicationContext(),
                    "Введите не менее двух символов!", Toast.LENGTH_LONG);
            t.show();
        } else {
            if(selected == 0) {
                factoryBarcode = input;
                textViewFactoryBarcode.setText("");
                isCheckEan13 = true;
            } else if(selected == 1) {
                isCheckEan13 = false;
            } else if(selected == 2) {
                isCheckEan13 = false;
            }
            KeyboardHideToolInit.keyboardHideTool(this.getActivity()).hideKeyboard();
            sendBindCheck(input);
        }
        searchEditText.clearFocus();
    }

    private void sendBindCheck(String input) {
        SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

        Command<BindCheckPojo> command = new BindCheckCommandWithFactoryBarcode(
                new BindCheckCommandWithBarcode(
                        new BindCheckCommandWithCheckEAN13(
                                new BindCheckCommandWithSelected(
                                        new BindCheckCommandWithToken(
                                                new BindCheckCommandSimple(),
                                                prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                                        ), selected
                                ), isCheckEan13
                        ), input
                ), factoryBarcode);

        NetworkAsyncTask<BindCheckPojo, BindCheckPojoResult> networkAsyncTask = new NetworkAsyncTask<>(
                this,
                queryDialog);

        networkAsyncTask.execute((Command) command);
    }

    @Override
    public void switchToBind() {
        // TODO: 31.01.20 CIRCULAR ANIMATION (USE FOR TASK)
        // ==================== ANIMATION  START ==================== //
//        int x = linearLayoutBindFactoryBarcode.getRight();
//        int y = linearLayoutBindFactoryBarcode.getBottom();
//
//        int startRadius = 0;
//        int endRadius = (int) Math.hypot(linearLayoutBindFactoryBarcode.getWidth(), linearLayoutBindFactoryBarcode.getHeight());

//            Animation out = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_out);
//        Animator animator = ViewAnimationUtils.createCircularReveal(linearLayoutBindFactoryBarcode, x, y, startRadius, endRadius);
//        animator.start();
        // ==================== ANIMATION  END ==================== //

        if(bindingOKCardView.getVisibility() == View.VISIBLE) {
            Animation out = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.slide_out_right);
            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    bindingOKCardView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            bindingOKCardView.startAnimation(out);
        }

        selected = 1;
        Animation in = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_in);
        linearLayoutBindFactoryBarcode.startAnimation(in);

        linearLayoutBindFactoryBarcode.setVisibility(View.VISIBLE);

        textViewFactoryBarcode.setText(factoryBarcode);
        searchEditText.setText("");
        searchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void switchToCheckBind() {
        // TODO: 31.01.20 CIRCULAR ANIMATION (USE FOR TASK)
        // ==================== ANIMATION  START ==================== //
//        int x = linearLayoutBindFactoryBarcode.getRight();
//        int y = linearLayoutBindFactoryBarcode.getBottom();
//
//        int startRadius = Math.max(linearLayoutBindFactoryBarcode.getWidth(), linearLayoutBindFactoryBarcode.getHeight());
//        int endRadius = 0;
//        Animator animator = ViewAnimationUtils.createCircularReveal(linearLayoutBindFactoryBarcode, x, y, startRadius, endRadius);
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                linearLayoutBindFactoryBarcode.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });

//        animator.start();
        // ==================== ANIMATION  END ==================== //

        selected = 0;

        Animation out = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_out);
        linearLayoutBindFactoryBarcode.startAnimation(out);

        linearLayoutBindFactoryBarcode.setVisibility(View.GONE);

        factoryBarcode = "";
        searchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchEditText.setText("");
    }

    @Override
    public void showResult(BindRecord record) {
        if(bindingOKCardView.getVisibility() == View.GONE) {
            Animation in = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.slide_out_right);
            in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    TextView barcodeTV = bindingOKCardView.findViewById(R.id.textViewCardIDBindingContainerOK);
                    TextView nameTV = bindingOKCardView.findViewById(R.id.textViewCardNameBindingContainerOK);
                    barcodeTV.setText(record.barcode());
                    nameTV.setText(record.name());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bindingOKCardView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            bindingOKCardView.startAnimation(in);
            showSnackbar();
        } else {
            Animation in = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.slide_in_left);
            Animation out = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.slide_out_right);
            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    bindingOKCardView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bindingOKCardView.startAnimation(in);
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });

            in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    TextView barcodeTV = bindingOKCardView.findViewById(R.id.textViewCardIDBindingContainerOK);
                    TextView nameTV = bindingOKCardView.findViewById(R.id.textViewCardNameBindingContainerOK);
                    barcodeTV.setText(record.barcode());
                    nameTV.setText(record.name());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bindingOKCardView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });

            bindingOKCardView.startAnimation(out);
            showSnackbar();
        }
    }

    public void showSnackbar() {
        Snackbar snackbar = Snackbar.make(
                bindingOKCardView,
                BindingFragment.this.getString(R.string.snackbar_bind_check_ok),
                Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this.getActivity(), R.color.colorChange));
        snackbar.show();
    }

    @Override
    public void handleResult(CommandResult<BindCheckPojo, BindCheckPojoResult> result) {
        if(result.isDone()) {
            BindCheckPojoResult resPojo = result.data();
            if(resPojo.getSelected() == 0) { // check binding
                List<BindRecord> records = resPojo.getRecords();
                if (records.size() != 0) {
                    if (records.size() == 1) {
                        showResult(records.get(0));
                    } else {
                        String title = this.getString(R.string.fragment_result_bind);
                        doResultBindFragmentTransaction(title, result);
                    }
                } else {
                    String message = this.getString(R.string.dialog_bind_check_no_result);
                    NoResultAlertDialogCreator noResADCr =
                            new BindCheckNoResultAlertDialogCreatorImpl(this.getActivity(), message);
                    noResADCr.create().show();

                    if(getBindingContainerFragment() != null)
                        getBindingContainerFragment().switchToBind();
                }
            } else if(resPojo.getSelected() == 1) { // binding
                List<BindRecord> records = resPojo.getRecords();
                if (records.size() != 0) {
                    if (records.size() == 1) {
                        NetworkCallback<BindPojo, BindPojoResult> bindCallback = new NetworkCallback<BindPojo, BindPojoResult>() {
                            @Override
                            public void handleResult(CommandResult<BindPojo, BindPojoResult> resultBind) {
                                if(resultBind.data().getSelected() == 2) { //binding done
                                    if(getBindingContainerFragment() == null)
                                        doBindingContainerFragmentTransactionFromResultBind();

                                    if(getBindingContainerFragment() != null)
                                        getBindingContainerFragment().switchToCheckBind();

                                    SuccessAlertDialogCreator successADCr =
                                            new SuccessAlertDialogCreatorImpl(getActivity(), resultBind.data().getMessage());
                                    successADCr.create().show();
                                } else if(resultBind.lastCommand() != null) {
                                    new ReconnectAlertDialogCreatorImpl(
                                            getActivity(),
                                            this,
                                            resultBind.lastCommand()
                                    ).create().show();
                                } else
                                    new ErrorAlertDialogCreatorImpl(
                                            getActivity(),
                                            resultBind.data().getMessage()
                                    ).create().show();
                            }
                        };

                        OneResultAlertDialogCreator oneResADCr =
                                new OneResultAlertDialogCreatorBindImpl(
                                        this.getActivity(),
                                        bindCallback,
                                        records.get(0),
                                        queryDialog,
                                        factoryBarcode);
                        androidx.appcompat.app.AlertDialog oneResAD = oneResADCr.create();
                        oneResAD.setCanceledOnTouchOutside(false);
                        oneResAD.show();
                    } else {
                        String title = "Привязка к №" + resPojo.getFactoryBarcode();
                        doResultBindFragmentTransaction(title, result);
                    }
                } else {
                    NoResultAlertDialogCreator noResADCr =
                            new NoResultAlertDialogCreatorImpl(this.getActivity());
                    noResADCr.create().show();
                }
            }
        } else if(result.lastCommand() != null) {
            new ReconnectAlertDialogCreatorImpl(
                    this.getActivity(),
                    this,
                    result.lastCommand()
            ).create().show();
        } else
            new ErrorAlertDialogCreatorImpl(
                    this.getActivity(),
                    result.data().getMessage()
            ).create().show();
    }

    private IBindingContainerFragment getBindingContainerFragment() {
        try {
            ImplFinder<IBindingContainerFragment> bcfFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
            return bcfFinder.findImpl(IBindingContainerFragment.class);
        } catch (FindableException ignore) {
            return null;
        }
    }

    private void doResultBindFragmentTransaction(String title, CommandResult<BindCheckPojo, BindCheckPojoResult> result) {
        FragmentTransactionManager fragmentTransactionManager =
                new FragmentTransactionManagerImpl(this.getActivity());
        fragmentTransactionManager.doResultBindFragmentTransaction(title, result);
    }

    private void doBindingContainerFragmentTransactionFromResultBind() {
        FragmentTransactionManager fragmentTransactionManager =
                new FragmentTransactionManagerImpl(this.getActivity());
        fragmentTransactionManager.doBindingContainerFragmentTransactionFromResultBind();
    }
}
