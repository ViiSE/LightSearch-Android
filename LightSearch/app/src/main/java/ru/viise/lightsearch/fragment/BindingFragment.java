
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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

import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.KeyboardHideToolInit;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.scan.ScannerInit;
import ru.viise.lightsearch.cmd.manager.task.v2.NetworkAsyncTask;
import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.v2.BindCheckCommandSimple;
import ru.viise.lightsearch.data.v2.BindCheckCommandWithBarcode;
import ru.viise.lightsearch.data.v2.BindCheckCommandWithCheckEAN13;
import ru.viise.lightsearch.data.v2.BindCheckCommandWithFactoryBarcode;
import ru.viise.lightsearch.data.v2.BindCheckCommandWithSelected;
import ru.viise.lightsearch.data.v2.BindCheckCommandWithToken;
import ru.viise.lightsearch.data.v2.Command;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class BindingFragment extends Fragment implements IBindingFragment {

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

    private ManagerActivityHandler managerActivityHandler;
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
            ScannerInit.scanner(this.getActivity()).scan();
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
                            SharedPreferences sPref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

                            factoryBarcode = input;
                            textViewFactoryBarcode.setText("");
                            isCheckEan13 = true;

                            KeyboardHideToolInit.keyboardHideTool(getActivity()).hideKeyboard();

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

                            NetworkAsyncTask<BindCheckPojo> networkAsyncTask = new NetworkAsyncTask<>(
                                    managerActivityHandler,
                                    queryDialog);

                            networkAsyncTask.execute(command);
                        }
                    } else
                        searchMode = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
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
        managerActivityHandler = (ManagerActivityHandler) this.getActivity();
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
            SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

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

            NetworkAsyncTask<BindCheckPojo> networkAsyncTask = new NetworkAsyncTask<>(
                    managerActivityHandler,
                    queryDialog);

            networkAsyncTask.execute(command);
        }
        searchEditText.clearFocus();
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
                public void onAnimationRepeat(Animation animation) {

                }
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
                public void onAnimationRepeat(Animation animation) {

                }
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
                public void onAnimationRepeat(Animation animation) {

                }
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
}
