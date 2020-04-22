package ru.viise.lightsearch.dialog.alert;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.security.HashAlgorithm;

public class AlertDialogSettingsCreatorImpl implements AlertDialogCreator {

    private final Activity activity;
    private final HashAlgorithm hashAlgorithm;
    private final PreferencesManager prefManager;

    public AlertDialogSettingsCreatorImpl(Activity activity, HashAlgorithm hashAlgorithm, PreferencesManager prefManager) {
        this.activity = activity;
        this.hashAlgorithm = hashAlgorithm;
        this.prefManager = prefManager;
    }

    @Override
    public AlertDialog create() {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_settings_ex, null);
        androidx.appcompat.app.AlertDialog dialogResult = new androidx.appcompat.app.AlertDialog
                .Builder(activity, R.style.FSDialogTheme)
                .setView(dialogView)
                .create();
        dialogResult.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        EditText etHostServer = dialogView.findViewById(R.id.etHostServer);
        EditText etPortServer = dialogView.findViewById(R.id.etPortServer);
        EditText etHostUpdater = dialogView.findViewById(R.id.etHostUpdater);
        EditText etPortUpdater = dialogView.findViewById(R.id.etPortUpdater);
        Button chPassButton = dialogView.findViewById(R.id.btnChangePass);
        Button saveSettingsButton = dialogView.findViewById(R.id.btnSaveSettings);

        etHostServer.setText(prefManager.load(PreferencesManagerType.HOST_SERVER_MANAGER));
        etPortServer.setText(prefManager.load(PreferencesManagerType.PORT_SERVER_MANAGER));

        etHostUpdater.setText(prefManager.load(PreferencesManagerType.HOST_UPDATER_MANAGER));
        etPortUpdater.setText(prefManager.load(PreferencesManagerType.PORT_UPDATER_MANAGER));

        chPassButton.setOnClickListener(vCh -> {
            new SettingsViewChangePasswordAlertDialogCreatorImpl(activity, hashAlgorithm, prefManager)
                    .create()
                    .show();
        });

        saveSettingsButton.setOnClickListener(vSave -> {
            if(etHostServer.getText().toString().isEmpty() ||
                    etPortServer.getText().toString().isEmpty() ||
                    etHostUpdater.getText().toString().isEmpty() ||
                    etPortUpdater.getText().toString().isEmpty())
                Toast.makeText(activity.getApplicationContext(), R.string.toast_not_enough_settings_data, Toast.LENGTH_SHORT).show();
            else {
                prefManager.save(PreferencesManagerType.HOST_SERVER_MANAGER, etHostServer.getText().toString());
                prefManager.save(PreferencesManagerType.PORT_SERVER_MANAGER, etPortServer.getText().toString());
                prefManager.save(PreferencesManagerType.HOST_UPDATER_MANAGER, etHostUpdater.getText().toString());
                prefManager.save(PreferencesManagerType.PORT_UPDATER_MANAGER, etPortUpdater.getText().toString());
                NetworkService.setBaseUrl(etHostServer.getText().toString(), etPortServer.getText().toString());
                Toast.makeText(activity.getApplicationContext(), R.string.toast_settings_is_saved, Toast.LENGTH_SHORT).show();
            }
        });

        return dialogResult;
    }
}
