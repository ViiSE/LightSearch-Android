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

package ru.viise.lightsearch.dialog.alert;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.cmd.process.Processes;
import ru.viise.lightsearch.cmd.process.ProcessesImpl;
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
import ru.viise.lightsearch.data.pojo.SendForm;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorDmaxImpl;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerImpl;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.util.IPAddressProvider;
import ru.viise.lightsearch.util.IPAddressProviderInit;

public class ReconnectAlertDialogCreatorImpl implements SuccessAlertDialogCreator {

    private final Activity activity;
    private final NetworkCallback<? extends SendForm, ? extends SendForm> networkCallback;
    private final Command<? extends SendForm> command;

    public ReconnectAlertDialogCreatorImpl(
            Activity activity,
            NetworkCallback<? extends SendForm, ? extends SendForm> networkCallback,
            Command<? extends SendForm> command) {
        this.activity = activity;
        this.networkCallback = networkCallback;
        this.command = command;
    }

    @Override
    public AlertDialog create() {
        View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_reconnect, null);
        CircularProgressButton buttonConnect = dialogView.findViewById(R.id.buttonDialogReconnect);
        EditText etUsername = dialogView.findViewById(R.id.etUsername);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        EditText etUserIdent = dialogView.findViewById(R.id.etUserIdent);
        TextView tvAuthFailed = dialogView.findViewById(R.id.twAuthFailed);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(dialogView).create();
        buttonConnect.setOnClickListener(viewOK -> {
            if(etUsername.getText().toString().isEmpty()  ||
                    etPassword.getText().toString().isEmpty()) {
                Toast t = Toast.makeText(activity.getApplicationContext(),
                        R.string.toast_not_enough_auth_data, Toast.LENGTH_LONG);
                t.show();
            } else {
                SharedPreferences sPref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
                PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
                prefManager.save(PreferencesManagerType.USERNAME_MANAGER, etUsername.getText().toString());
                prefManager.save(PreferencesManagerType.PASS_MANAGER, etPassword.getText().toString());
                prefManager.save(PreferencesManagerType.USER_IDENT_MANAGER, etUserIdent.getText().toString());
                IPAddressProvider ipAddrProvider = IPAddressProviderInit.ipAddressProvider();
                String ip = ipAddrProvider.ipAddress(true);
                String os = Build.VERSION.RELEASE;
                String model = Build.MODEL;
                Command<LoginPojo> commandLogin = new LoginCommandWithIMEI(
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
                                                ), etUserIdent.getText().toString()
                                        ), etPassword.getText().toString()
                                ), etUsername.getText().toString()
                        ), ((ManagerActivityUI) activity).getIMEI());

                new ReconnectTask(
                        new WeakReference<>(networkCallback),
                        new WeakReference<>(activity),
                        new WeakReference<>(tvAuthFailed),
                        new WeakReference<>(buttonConnect),
                        new WeakReference<>(dialog),
                        new WeakReference<>(command))
                        .execute((Command) commandLogin);
            }});
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }

    private static class ReconnectTask extends AsyncTask<Command<LoginPojo>, Void, CommandResult<LoginPojo, LoginPojoResult>> {

        private final WeakReference<NetworkCallback<? extends SendForm, ? extends SendForm>> fragmentCallback;
        private final WeakReference<Activity> activity;
        private final WeakReference<TextView> tvAuthFailed;
        private final WeakReference<CircularProgressButton> button;
        private final WeakReference<AlertDialog> dialog;
        private final WeakReference<Command<? extends SendForm>> command;

        ReconnectTask(
                WeakReference<NetworkCallback<? extends SendForm, ? extends SendForm>> fragmentCallback,
                WeakReference<Activity> activity,
                WeakReference<TextView> tvAuthFailed,
                WeakReference<CircularProgressButton> button,
                WeakReference<AlertDialog> dialog,
                WeakReference<Command<? extends SendForm>> command) {
            this.fragmentCallback = fragmentCallback;
            this.activity = activity;
            this.tvAuthFailed = tvAuthFailed;
            this.button = button;
            this.dialog = dialog;
            this.command = command;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            button.get().startAnimation();
        }

        @SafeVarargs
        @Override
        protected final CommandResult<LoginPojo, LoginPojoResult> doInBackground(Command<LoginPojo>... commands) {
            Processes processes = new ProcessesImpl(NetworkService.getInstance());
            Command<LoginPojo> command = commands[0];
            return processes.process(commands[0].name()).apply((Command) command);
        }

        @Override
        protected void onPostExecute(CommandResult<LoginPojo, LoginPojoResult> cmdRes) {
            super.onPostExecute(cmdRes);
            button.get().revertAnimation();
            if(cmdRes.isDone()) {
                tvAuthFailed.get().setVisibility(View.INVISIBLE);
                dialog.get().dismiss();
                SharedPreferences sPref = activity.get().getSharedPreferences("pref", Context.MODE_PRIVATE);
                PreferencesManager prefManager = new PreferencesManagerImpl(sPref);
                prefManager.save(PreferencesManagerType.TOKEN_MANAGER, cmdRes.data().getToken());
                if(cmdRes.data().getUserIdentifier().equals("0")) {
                    prefManager.save(
                            PreferencesManagerType.USER_IDENT_MANAGER,
                            prefManager.load(PreferencesManagerType.USERNAME_MANAGER));
                } else {
                    prefManager.save(
                            PreferencesManagerType.USER_IDENT_MANAGER,
                            cmdRes.data().getUserIdentifier());
                }
                NetworkAsyncTask<? extends SendForm, ? extends SendForm> networkAsyncTask =
                        new NetworkAsyncTask<>(
                                fragmentCallback.get(),
                                new SpotsDialogCreatorDmaxImpl(activity.get(), R.string.spots_dialog_query_exec).create());
                networkAsyncTask.execute((Command) command.get());
            } else {
                tvAuthFailed.get().setText(cmdRes.data().getMessage());
                tvAuthFailed.get().setVisibility(View.VISIBLE);
            }
        }
    }
}
