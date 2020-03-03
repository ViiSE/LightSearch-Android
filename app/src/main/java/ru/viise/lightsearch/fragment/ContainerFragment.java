
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.activity.OnBackPressedListenerType;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.ExitToAuthorizationAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.ExitToAuthorizationAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromFragmentDefaultImpl;


public class ContainerFragment extends Fragment implements OnBackPressedListener, IContainerFragment {

    private static final String TAG = "ContainerFragment";

    private String[] skladArray;
    private String[] TKArray;

    private AlertDialog queryDialog;

    private ManagerActivityHandler managerActivityHandler;
    private OnBackPressedListenerType onBackPressedListenerType;

    private static final String ON_BACK_TYPE = "OnBackType";
    private int selected = 0; //0 - CONTAINER_FRAGMENT, 1 - SOFT_CHECK_FRAGMENT

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            selected = savedInstanceState.getInt(ON_BACK_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        ViewPager viewPager = view.findViewById(R.id.ViewPagerCon);

        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.TabLayoutCon);
        tabLayout.setupWithViewPager(viewPager);

        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();

        if(selected == 0)
            onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
        else if(selected == 1)
            onBackPressedListenerType = OnBackPressedListenerType.SOFT_CHECK_FRAGMENT;

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityHandler = (ManagerActivityHandler) this.getActivity();
        ManagerActivityUI managerActivityUI = (ManagerActivityUI) this.getActivity();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ON_BACK_TYPE, selected);
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentPageAdapter fragmentPageAdapter = new FragmentPageAdapter(getChildFragmentManager());
        SearchFragment searchFragment = new SearchFragment();
        SoftCheckContainerFragment rootFragment = new SoftCheckContainerFragment();
        fragmentPageAdapter.addFragment(searchFragment, getString(R.string.fragment_search));
        fragmentPageAdapter.addFragment(rootFragment, getString(R.string.fragment_soft_check));

        searchFragment.init(skladArray, TKArray);

        viewPager.setAdapter(fragmentPageAdapter);
        onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
    }

    private ISoftCheckContainerFragment getSoftCheckContainerFragment() {
        ImplFinder<ISoftCheckContainerFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
        try { return finder.findImpl(ISoftCheckContainerFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    private ISoftCheckFragment getSoftCheckFragment() {
        ImplFinder<ISoftCheckFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
        try { return finder.findImpl(ISoftCheckFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    @Override
    public void onBackPressed() {
        if(onBackPressedListenerType == OnBackPressedListenerType.CONTAINER_FRAGMENT) {
            ExitToAuthorizationAlertDialogCreator exitTAADCr =
                    ExitToAuthorizationAlertDialogCreatorInit.exitToAuthorizationAlertDialogCreator(
                            this.getActivity());
            exitTAADCr.create().show();
        } else if(onBackPressedListenerType == OnBackPressedListenerType.SOFT_CHECK_FRAGMENT) {
            CancelSoftCheckAlertDialogCreator cancelSCADCr =
                    CancelSoftCheckAlertDialogCreatorInit.cancelSoftCheckAlertDialogCreator(
                            this, managerActivityHandler, queryDialog);
            cancelSCADCr.create().show();
        }
    }

    @Override
    public void setSearchBarcode(String barcode) {
        try {
            ImplFinder<ISearchFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
            ISearchFragment searchFragment = finder.findImpl(ISearchFragment.class);
            searchFragment.setSearchBarcode(barcode);
        }
        catch(FindableException ignore) {}
    }

    @Override
    public void setCardCode(String cardCode) {
        try {
            ImplFinder<IOpenSoftCheckFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
            IOpenSoftCheckFragment openSoftCheckFragment = finder.findImpl(IOpenSoftCheckFragment.class);
            openSoftCheckFragment.setCardCode(cardCode);
        } catch(FindableException ignore) {}
    }

    @Override
    public void setSoftCheckBarcode(String barcode) {
        ISoftCheckFragment softCheckFragment = getSoftCheckFragment();
        if(softCheckFragment != null)
            softCheckFragment.setSoftCheckBarcode(barcode);
    }

    @Override
    public void switchToSoftCheckFragment() {
        ISoftCheckContainerFragment softCheckContainerFragment = getSoftCheckContainerFragment();
        if(softCheckContainerFragment != null) {
            softCheckContainerFragment.switchToSoftCheckFragment();
            onBackPressedListenerType = OnBackPressedListenerType.SOFT_CHECK_FRAGMENT;
            selected = 1;
        }
    }

    @Override
    public void switchToOpenSoftCheckFragment() {
        ISoftCheckContainerFragment softCheckContainerFragment = getSoftCheckContainerFragment();
        if(softCheckContainerFragment != null) {
            softCheckContainerFragment.switchToOpenSoftCheckFragment();
            onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
            selected = 0;
        }
    }

    @Override
    public void addSoftCheckRecord(SoftCheckRecord record) {
        ISoftCheckFragment softCheckFragment = getSoftCheckFragment();
        if(softCheckFragment != null)
            softCheckFragment.addSoftCheckRecord(record);
    }

    public void setupSearchFragment(String[] skladArray, String[] TKArray) {
        this.skladArray = skladArray;
        this.TKArray = TKArray;
    }
}
