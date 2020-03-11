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
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.result.holder.ResultCommandHolderUI;
import ru.viise.lightsearch.activity.result.holder.ResultCommandUICreator;
import ru.viise.lightsearch.activity.result.holder.ResultCommandUICreatorInit;
import ru.viise.lightsearch.cmd.result.BindCommandResult;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.UnbindCommandResult;
import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.UnbindRecord;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.find.ImplFinderFragmentFromFragmentDefaultImpl;
import ru.viise.lightsearch.fragment.ContainerFragment;
import ru.viise.lightsearch.fragment.IBindingContainerFragment;
import ru.viise.lightsearch.fragment.IContainerFragment;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManager;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerInit;
import ru.viise.lightsearch.request.PhonePermission;
import ru.viise.lightsearch.request.PhonePermissionInit;
import ru.viise.lightsearch.util.UpdateChecker;
import ru.viise.lightsearch.util.UpdateCheckerInit;

public class ManagerActivity extends AppCompatActivity implements ManagerActivityHandler, ManagerActivityUI {

    private String IMEI;
    private ScanType scanType;
    private ResultCommandHolderUI commandHolderUI;

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

        ResultCommandUICreator resCmdUiCr = ResultCommandUICreatorInit.resultCommandUICreator(this);
        commandHolderUI = resCmdUiCr.createResultCommandHolderUI();

        doAuthorizationFragmentTransaction();

        UpdateChecker updateChecker = UpdateCheckerInit.updateChecker(ManagerActivity.this);
        updateChecker.checkUpdate();
    }

    @Override
    public void onBackPressed() {
        try {
            ImplFinder<OnBackPressedListener> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this);
            OnBackPressedListener backPressedListener = finder.findImpl(OnBackPressedListener.class);
            backPressedListener.onBackPressed();
        } catch(FindableException ex) { super.onBackPressed(); }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents();
            IContainerFragment containerFragment = getContainerFragment();

            if (containerFragment != null)
                if(scanType == ScanType.SEARCH)
                    containerFragment.setSearchBarcode(scanContent);
                else if(scanType == ScanType.OPEN_SOFT_CHECK)
                    containerFragment.setCardCode(scanContent);
                else if(scanType == ScanType.SEARCH_SOFT_CHECK)
                    containerFragment.setSoftCheckBarcode(scanContent);
                else if (scanType == ScanType.SEARCH_BIND)
                    containerFragment.setBindingBarcode(scanContent, true);
                else if(scanType == ScanType.UNBIND)
                    containerFragment.setUnbindingBarcode(scanContent, true);
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
        PhonePermission phonePermission = PhonePermissionInit.phonePermission(this);
        phonePermission.requestPhonePermission();
    }

    private void doAuthorizationFragmentTransaction() {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doAuthorizationFragmentTransaction();
    }

    public void doContainerFragmentTransaction(String[] skladArr, String[] TKArr) {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doContainerFragmentTransaction(skladArr, TKArr);
    }

    // TODO: 29.01.20 REMOVE THIS LATER
    public void doBindingContainerFragmentTransactionFromResultBind() {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doBindingContainerFragmentTransactionFromResultBind();
    }

    public void doContainerFragmentTransactionFromCart() {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doContainerFragmentTransactionFromCart();
    }

    public void doResultSearchFragmentTransaction(String title, List<SearchRecord> searchRecords) {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doResultSearchFragmentTransaction(title, searchRecords);
    }

    // TODO: 30.01.20 REMOVE THIS LATER
    public void doResultBindFragmentTransaction(String title, BindCommandResult bindCmdRes) {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doResultBindFragmentTransaction(title, bindCmdRes);
    }

    // TODO: 30.01.20 REMOVE THIS LATER
    public void doResultUnbindFragmentTransaction(String title, UnbindCommandResult unbindCmdRes) {
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doResultUnbindFragmentTransaction(title, unbindCmdRes);
    }

    public void doCartFragmentTransaction(List<SoftCheckRecord> cartRecords) {
        IContainerFragment containerFragment = getContainerFragment();
        containerFragment.switchToOpenSoftCheckFragment();
        FragmentTransactionManager fragmentTransactionManager =
                FragmentTransactionManagerInit.fragmentTransactionManager(this);
        fragmentTransactionManager.doCartFragmentTransaction(cartRecords);
    }

    public void callDialogError(String errorMessage) {
        ErrorAlertDialogCreator errADCr =
                ErrorAlertDialogCreatorInit.errorAlertDialogCreator(this, errorMessage);
        errADCr.create().show();
    }

    public void callDialogSuccess(String message) {
        SuccessAlertDialogCreator successADCr =
                SuccessAlertDialogCreatorInit.successAlertDialogCreator(this, message);
        successADCr.create().show();
    }

    public void callDialogNoResult() {
        String message = this.getString(R.string.dialog_no_result);
        NoResultAlertDialogCreator noResADCr =
                NoResultAlertDialogCreatorInit.noResultAlertDialogCreator(this, message);
        noResADCr.create().show();
    }


    // TODO: 30.01.20 REMOVE THIS LATER
    public void callBindCheckDialogNoResult() {
        String message = this.getString(R.string.dialog_bind_check_no_result);
        NoResultAlertDialogCreator noResADCr =
                NoResultAlertDialogCreatorInit.bindCheckNoResultAlertDialogCreator(this, message);
        noResADCr.create().show();
        getContainerFragment().switchToBind();
    }

    public void callSearchDialogOneResult(SearchRecord searchRecord) {
        OneResultAlertDialogCreator oneResADCr =
                OneResultAlertDialogCreatorInit.oneResultSearchAlertDialogCreator(this, searchRecord);
        android.support.v7.app.AlertDialog oneResAD = oneResADCr.create();
        oneResAD.setCanceledOnTouchOutside(false);
        oneResAD.show();
    }

    // TODO: 30.01.20 REMOVE THIS LATER
    public void callBindCheckDialogOneResult(BindRecord bindRecord) {
        IBindingContainerFragment bindingContainerFragment = getBindingContainerFragment();
        bindingContainerFragment.showResult(bindRecord);
    }

    // TODO: 30.01.20 REMOVE THIS LATER
    public void callUnbindCheckDialogOneResult(UnbindRecord unbindRecord) {
        IBindingContainerFragment bindingContainerFragment = getBindingContainerFragment();
        bindingContainerFragment.showResult(unbindRecord);
    }

    // TODO: 30.01.20 REMOVE THIS LATER
    public void callBindDialogOneResult(BindRecord bindRecord, String factoryBarcode) {
        OneResultAlertDialogCreator oneResADCr =
                OneResultAlertDialogCreatorInit.oneResultBindAlertDialogCreator(
                        this,
                        bindRecord,
                        SpotsDialogCreatorInit
                                .spotsDialogCreator(this, R.string.spots_dialog_query_exec)
                                .create(),
                        factoryBarcode);
        android.support.v7.app.AlertDialog oneResAD = oneResADCr.create();
        oneResAD.setCanceledOnTouchOutside(false);
        oneResAD.show();
    }

    public IContainerFragment getContainerFragment() {
        ImplFinder<IContainerFragment> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this);
        try { return finder.findImpl(IContainerFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    public IBindingContainerFragment getBindingContainerFragment() {
        try {
            ImplFinder<ContainerFragment> cfFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this);
            ContainerFragment cf = cfFinder.findImpl(ContainerFragment.class);

            ImplFinder<IBindingContainerFragment> bcfFinder = new ImplFinderFragmentFromFragmentDefaultImpl<>(cf);
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
        return IMEI;
    }

    @Override
    public void handleResult(CommandResult commandResult) {
        if(commandHolderUI.get(commandResult) != null)
            commandHolderUI.get(commandResult).apply(commandResult);
    }
}
