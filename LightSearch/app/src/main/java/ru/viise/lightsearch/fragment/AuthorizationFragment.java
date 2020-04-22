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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.check.FirstRunAppChecker;
import ru.viise.lightsearch.check.FirstRunAppCheckerImpl;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.AlertDialogCreatorDTO;
import ru.viise.lightsearch.data.AlertDialogCreatorDTOImpl;
import ru.viise.lightsearch.data.CreatePasswordInFirstTimeAlertDialogCreatorDTO;
import ru.viise.lightsearch.data.CreatePasswordInFirstTimeAlertDialogCreatorDTOImpl;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.LoginCommandSimple;
import ru.viise.lightsearch.data.entity.LoginCommandWithIMEI;
import ru.viise.lightsearch.data.entity.LoginCommandWithIP;
import ru.viise.lightsearch.data.entity.LoginCommandWithModel;
import ru.viise.lightsearch.data.entity.LoginCommandWithOs;
import ru.viise.lightsearch.data.entity.LoginCommandWithPassword;
import ru.viise.lightsearch.data.entity.LoginCommandWithUserIdentifier;
import ru.viise.lightsearch.data.entity.LoginCommandWithUsername;
import ru.viise.lightsearch.data.pojo.LoginPojo;
import ru.viise.lightsearch.data.pojo.LoginPojoResult;
import ru.viise.lightsearch.dialog.alert.CreatePasswordInFirstTimeAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.CreatePasswordInFirstTimeAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.InputPasswordAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.InputPasswordAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManager;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerImpl;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.security.HashAlgorithm;
import ru.viise.lightsearch.security.HashAlgorithmInit;
import ru.viise.lightsearch.util.IPAddressProvider;
import ru.viise.lightsearch.util.IPAddressProviderInit;

import static android.view.View.OnClickListener;


public class AuthorizationFragment extends Fragment implements OnClickListener, NetworkCallback<LoginPojo, LoginPojoResult> {

    public static final String TAG = "AuthorizationFragment";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextUserIdent;

    private AlertDialog inputPassword;
    private AlertDialog createPassFirst;

    private PreferencesManager prefManager;
    private ManagerActivityUI mIManagerActivity;

    private Animation animAlpha;

    private android.app.AlertDialog queryDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization,container,false);

        animAlpha = AnimationUtils.loadAnimation(this.getActivity(), R.anim.alpha);

        SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        prefManager = PreferencesManagerInit.preferencesManager(sPref);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextUserIdent = view.findViewById(R.id.editTextUserIdent);
        Button buttonConnect = view.findViewById(R.id.buttonConnect);

        editTextUsername.setText(prefManager.load(PreferencesManagerType.USERNAME_MANAGER));

        buttonConnect.setOnClickListener(this);

        AlertDialogCreatorDTO aDCreatorDTO =
                new AlertDialogCreatorDTOImpl(this.getActivity(), inflater, sPref);
        HashAlgorithm hashAlgorithm = HashAlgorithmInit.hashAlgorithm();

        createPassFirstDialog(aDCreatorDTO, hashAlgorithm);
        createInputPassDialog(prefManager, hashAlgorithm);

        ImageButton buttonSettings = view.findViewById(R.id.ibSettings);
        buttonSettings.setOnClickListener(v -> inputPassword.show());

        FirstRunAppChecker firstRunAppChecker = new FirstRunAppCheckerImpl(sPref);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
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
                    if(editTextUserIdent.getText().toString().isEmpty())
                        prefManager.save(PreferencesManagerType.USER_IDENT_MANAGER, "0");
                    else
                        prefManager.save(PreferencesManagerType.USER_IDENT_MANAGER, editTextUserIdent.getText().toString());
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

                    NetworkAsyncTask<LoginPojo, LoginPojoResult> networkAsyncTask = new NetworkAsyncTask<>(
                            this,
                            queryDialog);
                    networkAsyncTask.execute(command);
                }
                break;
        }
    }

    private void createPassFirstDialog(AlertDialogCreatorDTO aDCreatorDTO, HashAlgorithm hashAlgorithm) {
        CreatePasswordInFirstTimeAlertDialogCreatorDTO crPIFTADCreatorDTO =
                new CreatePasswordInFirstTimeAlertDialogCreatorDTOImpl(
                        aDCreatorDTO, hashAlgorithm);
        CreatePasswordInFirstTimeAlertDialogCreator crPIFTADCreator =
                new CreatePasswordInFirstTimeAlertDialogCreatorImpl(
                        crPIFTADCreatorDTO);
        createPassFirst = crPIFTADCreator.create();
    }

    private void createInputPassDialog(PreferencesManager prefManager, HashAlgorithm hashAlgorithm) {
        InputPasswordAlertDialogCreator iPADCreator =
                new InputPasswordAlertDialogCreatorImpl(
                        this.getActivity(),
                        prefManager,
                        hashAlgorithm);
        inputPassword = iPADCreator.create();
    }

    @Override
    public void handleResult(CommandResult<LoginPojo, LoginPojoResult> result) {
        if(result.isDone()) {
            prefManager.save(PreferencesManagerType.TOKEN_MANAGER, result.data().getToken());
            if(result.data().getUserIdentifier().equals("0")) {
                prefManager.save(
                        PreferencesManagerType.USER_IDENT_MANAGER,
                        prefManager.load(PreferencesManagerType.USERNAME_MANAGER));
            } else {
                prefManager.save(
                        PreferencesManagerType.USER_IDENT_MANAGER,
                        result.data().getUserIdentifier());
            }

            SuccessAlertDialogCreator successADCr = new SuccessAlertDialogCreatorImpl(
                    this.getActivity(),
                    result.data().getMessage());
            successADCr.create().show();

            FragmentTransactionManager fragmentTransactionManager =
                    new FragmentTransactionManagerImpl(this.getActivity());
            fragmentTransactionManager.doContainerFragmentTransaction(
                    result.data().getSkladList().toArray(new String[0]),
                    result.data().getTKList().toArray(new String[0]),
                    true);
        } else
             new ErrorAlertDialogCreatorImpl(
                     this.getActivity(),
                     result.data().getMessage()
             ).create().show();
    }
}
