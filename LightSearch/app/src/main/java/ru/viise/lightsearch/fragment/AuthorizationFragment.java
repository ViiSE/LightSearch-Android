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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.check.FirstRunAppChecker;
import ru.viise.lightsearch.check.FirstRunAppCheckerInit;
import ru.viise.lightsearch.cmd.holder.v2.ProcessesDefaultImpl;
import ru.viise.lightsearch.cmd.manager.NetworkService;
import ru.viise.lightsearch.cmd.manager.task.v2.NetworkAsyncTask;
import ru.viise.lightsearch.data.AlertDialogCreatorDTO;
import ru.viise.lightsearch.data.AlertDialogCreatorDTOInit;
import ru.viise.lightsearch.data.CreatePasswordInFirstTimeAlertDialogCreatorDTO;
import ru.viise.lightsearch.data.CreatePasswordInFirstTimeAlertDialogCreatorDTOInit;
import ru.viise.lightsearch.data.InputPasswordAlertDialogCreatorDTO;
import ru.viise.lightsearch.data.InputPasswordAlertDialogCreatorDTOInit;
import ru.viise.lightsearch.data.SettingsViewChangePasswordAlertDialogCreatorDTO;
import ru.viise.lightsearch.data.SettingsViewChangePasswordAlertDialogCreatorDTOInit;
import ru.viise.lightsearch.data.pojo.LoginPojo;
import ru.viise.lightsearch.data.v2.Command;
import ru.viise.lightsearch.data.v2.LoginCommandSimple;
import ru.viise.lightsearch.data.v2.LoginCommandWithIMEI;
import ru.viise.lightsearch.data.v2.LoginCommandWithIP;
import ru.viise.lightsearch.data.v2.LoginCommandWithModel;
import ru.viise.lightsearch.data.v2.LoginCommandWithOs;
import ru.viise.lightsearch.data.v2.LoginCommandWithPassword;
import ru.viise.lightsearch.data.v2.LoginCommandWithUserIdentifier;
import ru.viise.lightsearch.data.v2.LoginCommandWithUsername;
import ru.viise.lightsearch.dialog.alert.CreatePasswordInFirstTimeAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.CreatePasswordInFirstTimeDialogAlertCreatorInit;
import ru.viise.lightsearch.dialog.alert.InputPasswordAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.InputPasswordAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.SettingsViewChangePasswordAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.SettingsViewChangePasswordAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.security.HashAlgorithm;
import ru.viise.lightsearch.security.HashAlgorithmInit;
import ru.viise.lightsearch.util.IPAddressProvider;
import ru.viise.lightsearch.util.IPAddressProviderInit;

import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;


public class AuthorizationFragment extends Fragment implements OnClickListener {

    public static final String TAG = "AuthorizationFragment";
    private final String PREF       = "pref";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextUserIdent;
    private EditText editTextHost;
    private EditText editTextPort;

    private TextView twHost;
    private TextView twPort;

    private CheckBox cbSettings;
    private Button buttonChPass;

    private AlertDialog inputPassword;
    private AlertDialog createPass;
    private AlertDialog createPassFirst;

    private PreferencesManager prefManager;
    private ManagerActivityUI mIManagerActivity;
    private ManagerActivityHandler managerActivityHandler;

    private Animation animAlpha;

    private android.app.AlertDialog queryDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization,container,false);

        animAlpha = AnimationUtils.loadAnimation(this.getActivity(), R.anim.alpha);

        SharedPreferences sPref = this.getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefManager = PreferencesManagerInit.preferencesManager(sPref);

        // Блок авторизации
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextUserIdent = view.findViewById(R.id.editTextUserIdent);
        Button buttonConnect = view.findViewById(R.id.buttonConnect);

        // Блок с расширенными настройками
        editTextHost = view.findViewById(R.id.editTextHost);
        editTextPort = view.findViewById(R.id.editTextPort);

        twHost = view.findViewById(R.id.textViewHost);
        twPort = view.findViewById(R.id.textViewPort);

        buttonChPass = view.findViewById(R.id.buttonChangePass);

        cbSettings = view.findViewById(R.id.checkBoxSettings);
        cbSettings.setChecked(false);

        editTextHost.setVisibility(INVISIBLE);
        editTextPort.setVisibility(INVISIBLE);

        twHost.setVisibility(INVISIBLE);
        twPort.setVisibility(INVISIBLE);

        buttonChPass.setVisibility(INVISIBLE);

        editTextHost.setText(prefManager.load(PreferencesManagerType.HOST_MANAGER));
        editTextPort.setText(prefManager.load(PreferencesManagerType.PORT_MANAGER));
        editTextUsername.setText(prefManager.load(PreferencesManagerType.USERNAME_MANAGER));

        // Устанавливаем ClickListener на кнопки
        buttonConnect.setOnClickListener(this);
        buttonChPass.setOnClickListener(this);
        cbSettings.setOnClickListener(this);

        AlertDialogCreatorDTO aDCreatorDTO =
                AlertDialogCreatorDTOInit.alertDialogCreatorDTO(this.getActivity(), inflater, sPref);
        HashAlgorithm hashAlgorithm = HashAlgorithmInit.hashAlgorithm();

        createPassFirstDialog(aDCreatorDTO, hashAlgorithm);
        createInputPassDialog(aDCreatorDTO, hashAlgorithm);
        createSettingsViewChangePassDialog(aDCreatorDTO, hashAlgorithm);

        FirstRunAppChecker firstRunAppChecker = FirstRunAppCheckerInit.firstRunAppChecker(sPref);
        if(firstRunAppChecker.check())
            createPassFirst.show();


        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIManagerActivity = (ManagerActivityUI) this.getActivity();
        managerActivityHandler = (ManagerActivityHandler) this.getActivity();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonConnect:
                v.startAnimation(animAlpha);
                if(editTextUsername.getText().toString().isEmpty()  ||
                    editTextPassword.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(this.getActivity().getApplicationContext(),
                            R.string.toast_not_enough_auth_data, Toast.LENGTH_LONG);
                    t.show();
                } else {
                    prefManager.save(PreferencesManagerType.USERNAME_MANAGER, editTextUsername.getText().toString());
                    prefManager.save(PreferencesManagerType.PASS_MANAGER, editTextPassword.getText().toString());
                    prefManager.save(PreferencesManagerType.HOST_MANAGER, editTextHost.getText().toString());
                    prefManager.save(PreferencesManagerType.PORT_MANAGER, editTextPort.getText().toString());
                    if(editTextUserIdent.getText().toString().isEmpty())
                        prefManager.save(PreferencesManagerType.USER_IDENT_MANAGER, "0");
                    else
                        prefManager.save(PreferencesManagerType.USER_IDENT_MANAGER, editTextUserIdent.getText().toString());
                    NetworkService.setBaseUrl(editTextHost.getText().toString(), editTextPort.getText().toString());
                    ProcessesDefaultImpl.isChange = true;
                    IPAddressProvider ipAddrProvider = IPAddressProviderInit.ipAddressProvider();
                    String ip = ipAddrProvider.ipAddress(true);
                    String os = Build.VERSION.RELEASE;
                    String model = Build.MODEL;
                    Command<LoginPojo> command = new LoginCommandWithIMEI(
                            new LoginCommandWithUsername(
                                    new LoginCommandWithPassword(
                                            new LoginCommandWithUserIdentifier(
                                                    new LoginCommandWithIP(
                                                            new LoginCommandWithOs(
                                                                    new LoginCommandWithModel(
                                                                            new LoginCommandSimple(),
                                                                            model
                                                                    ), os
                                                            ), ip
                                                    ), prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER)
                                            ), editTextPassword.getText().toString()
                                    ), editTextUsername.getText().toString()
                            ), mIManagerActivity.getIMEI());

                    NetworkAsyncTask<LoginPojo> networkAsyncTask = new NetworkAsyncTask<>(
                            managerActivityHandler,
                            queryDialog);
                    networkAsyncTask.execute(command);

                    cbSettings.setChecked(false);
                }
                break;
            case R.id.checkBoxSettings:
                if(cbSettings.isChecked()) {
                    inputPassword.show();
                }
                else {
                    twHost.setVisibility(INVISIBLE);
                    twPort.setVisibility(INVISIBLE);
                    editTextHost.setVisibility(INVISIBLE);
                    editTextPort.setVisibility(INVISIBLE);
                    buttonChPass.setVisibility(INVISIBLE);
                }
                break;
            case R.id.buttonChangePass:
                v.startAnimation(animAlpha);
                createPass.show();
                break;
        }
    }

    private void createPassFirstDialog(AlertDialogCreatorDTO aDCreatorDTO, HashAlgorithm hashAlgorithm) {
        CreatePasswordInFirstTimeAlertDialogCreatorDTO crPIFTADCreatorDTO =
                CreatePasswordInFirstTimeAlertDialogCreatorDTOInit.createPasswordInFirstTimeAlertDialogCreatorDTO(
                        aDCreatorDTO, hashAlgorithm);
        CreatePasswordInFirstTimeAlertDialogCreator crPIFTADCreator =
                CreatePasswordInFirstTimeDialogAlertCreatorInit.createPasswordInFirstTimeAlertDialogCreator(
                        crPIFTADCreatorDTO);
        createPassFirst = crPIFTADCreator.create();
    }

    private void createInputPassDialog(AlertDialogCreatorDTO aDCreatorDTO, HashAlgorithm hashAlgorithm) {
        InputPasswordAlertDialogCreatorDTO iPADCreatorDTO =
                InputPasswordAlertDialogCreatorDTOInit.inputPasswordAlertDialogCreatorDTO(aDCreatorDTO,
                        hashAlgorithm, twHost, twPort, editTextHost, editTextPort, buttonChPass,
                        cbSettings);
        InputPasswordAlertDialogCreator iPADCreator =
                InputPasswordAlertDialogCreatorInit.inputPasswordAlertDialogCreator(iPADCreatorDTO);
        inputPassword = iPADCreator.create();
    }

    private void createSettingsViewChangePassDialog(AlertDialogCreatorDTO aDCreatorDTO, HashAlgorithm hashAlgorithm) {
        SettingsViewChangePasswordAlertDialogCreatorDTO sVCPADCreatorDTO =
                SettingsViewChangePasswordAlertDialogCreatorDTOInit.settingsViewChangePasswordAlertDialogCreatorDTO(
                        aDCreatorDTO, hashAlgorithm);
        SettingsViewChangePasswordAlertDialogCreator sVCPADCreator =
                SettingsViewChangePasswordAlertDialogCreatorInit.settingsViewChangePasswordAlertDialogCreator(
                        sVCPADCreatorDTO);
        createPass = sVCPADCreator.create();
    }
}