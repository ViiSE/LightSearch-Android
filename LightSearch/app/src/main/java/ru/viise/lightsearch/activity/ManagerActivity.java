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

package ru.viise.lightsearch.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.cmd.ClientCommands;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.cmd.process.Processes;
import ru.viise.lightsearch.cmd.process.ProcessesImpl;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.entity.CheckAuthCommandSimple;
import ru.viise.lightsearch.data.entity.CheckAuthCommandWithToken;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.CheckAuthPojo;
import ru.viise.lightsearch.data.pojo.CheckAuthPojoResult;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.exception.JWTException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.BindingContainerFragment;
import ru.viise.lightsearch.fragment.IBindingContainerFragment;
import ru.viise.lightsearch.fragment.IContainerFragment;
import ru.viise.lightsearch.fragment.ISoftCheckContainerFragment;
import ru.viise.lightsearch.fragment.SoftCheckContainerFragment;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManager;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerInit;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.request.PhonePermission;
import ru.viise.lightsearch.request.PhonePermissionImpl;
import ru.viise.lightsearch.security.JWTClient;
import ru.viise.lightsearch.security.JWTClientWithPrefManager;
import ru.viise.lightsearch.util.UpdateChecker;
import ru.viise.lightsearch.util.UpdateCheckerInit;

public class ManagerActivity extends AppCompatActivity implements ManagerActivityUI {

    private String IMEI;
    private ScanType scanType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(ManagerActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
            IMEI = tm.getDeviceId();
        else
            reqPhonePermission();

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);

        SharedPreferences sPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);

        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
        JWTClient jwtClient = new JWTClientWithPrefManager(prefManager);
        try {
            jwtClient.check();
            NetworkService.setBaseUrl(
                    prefManager.load(PreferencesManagerType.HOST_MANAGER),
                    prefManager.load(PreferencesManagerType.PORT_MANAGER));
            boolean isDone = false;
            try {
                isDone = new CheckAuthAsyncTask(new WeakReference<>(prefManager)).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            if(isDone) {
                doContainerFragmentTransaction(new String[0], new String[0], false);
            } else {
                doAuthorizationFragmentTransaction(false);
            }
        } catch (JWTException ex) {
            doAuthorizationFragmentTransaction(false);
        }

        UpdateChecker updateChecker = UpdateCheckerInit.updateChecker(ManagerActivity.this);
        updateChecker.checkUpdate();
    }

    @Override
    public void onBackPressed() {
        try {
            ImplFinder<OnBackPressedListener> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this);
            OnBackPressedListener backPressedListener = finder.findImpl(OnBackPressedListener.class);
            backPressedListener.onBackPressed();
        } catch(FindableException ex) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents();
            IContainerFragment containerFragment = getContainerFragment();

            if (containerFragment != null)
                if(scanType == ScanType.SEARCH)
                    containerFragment.setSearchBarcode(scanContent, true);

            IBindingContainerFragment bindingContainerFragment = getBindingContainerFragment();
            if(bindingContainerFragment != null)
                if (scanType == ScanType.SEARCH_BIND)
                    bindingContainerFragment.setBindingBarcode(scanContent, true);
                else if(scanType == ScanType.UNBIND)
                    bindingContainerFragment.setUnbindingBarcode(scanContent, true);

            ISoftCheckContainerFragment softCheckContainerFragment = getSoftCheckContainerFragment();
            if(softCheckContainerFragment != null) {
                if(scanType == ScanType.OPEN_SOFT_CHECK)
                    softCheckContainerFragment.setCardCode(scanContent);
                else if(scanType == ScanType.SEARCH_SOFT_CHECK)
                    softCheckContainerFragment.setSoftCheckBarcode(scanContent);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if(view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int)event.getRawX();
                int rawY = (int)event.getRawY();
                if(!r.contains(rawX, rawY)) {
                    view.clearFocus();
                    KeyboardHideToolInit.keyboardHideTool(this).hideKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void reqPhonePermission() {
        PhonePermission phonePermission = new PhonePermissionImpl(this);
        phonePermission.requestPhonePermission();
    }

    private void doAuthorizationFragmentTransaction(boolean isNeedAnimation) {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doAuthorizationFragmentTransaction(isNeedAnimation);
    }

    public void doContainerFragmentTransaction(String[] skladArr, String[] TKArr, boolean isNeedAnimation) {
        SharedPreferences sPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
        if(prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER).equals("0")) {
            prefManager.save(
                    PreferencesManagerType.USER_IDENT_MANAGER,
                    prefManager.load(PreferencesManagerType.USERNAME_MANAGER));
        }
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doContainerFragmentTransaction(skladArr, TKArr, isNeedAnimation);
    }

    public void doSoftCheckContainerFragmentTransaction() {
        SoftCheckContainerFragment scfr = new SoftCheckContainerFragment();
        scfr.switchToOpenSoftCheckFragment();
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
        transaction.replace(R.id.activity_manager, scfr, SoftCheckContainerFragment.TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(SoftCheckContainerFragment.TAG);
        this.setTitle(this.getString(R.string.fragment_soft_check));
        transaction.commit();
    }

    public void doBindingContainerFragmentTransaction() {
        BindingContainerFragment bcf = new BindingContainerFragment();
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
        transaction.replace(R.id.activity_manager, bcf, this.getString(R.string.fragment_binding));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(this.getString(R.string.fragment_binding_container));
        this.setTitle(this.getString(R.string.fragment_binding_container));
        transaction.commit();
    }

    public IContainerFragment getContainerFragment() {
        ImplFinder<IContainerFragment> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this);
        try { return finder.findImpl(IContainerFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    public ISoftCheckContainerFragment getSoftCheckContainerFragment() {
        ImplFinder<ISoftCheckContainerFragment> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this);
        try { return finder.findImpl(ISoftCheckContainerFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    public IBindingContainerFragment getBindingContainerFragment() {
        try {
            ImplFinder<IBindingContainerFragment> bcfFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this);
            return bcfFinder.findImpl(IBindingContainerFragment.class);
        } catch (FindableException ignore) {
            return null;
        }
    }

    @Override
    public void setScanType(ScanType type) {
        scanType = type;
    }

    @Override
    public String getIMEI() {
        if(IMEI == null) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if(ContextCompat.checkSelfPermission(ManagerActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                IMEI = tm.getDeviceId();
        }

        return IMEI;
    }

    private static class CheckAuthAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<PreferencesManager> prefManager;

        CheckAuthAsyncTask(WeakReference<PreferencesManager> prefManager) {
            this.prefManager = prefManager;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Boolean doInBackground(Void... voids) {
            Processes processes = new ProcessesImpl(NetworkService.getInstance());
            Command<CheckAuthPojo> command = new CheckAuthCommandWithToken(
                    new CheckAuthCommandSimple(),
                    prefManager.get().load(PreferencesManagerType.TOKEN_MANAGER));

            CommandResult<CheckAuthPojo, CheckAuthPojoResult> cmdRes = processes
                    .process(ClientCommands.CHECK_AUTH)
                    .apply((Command) command);
            return cmdRes.isDone();
        }
    }
}
