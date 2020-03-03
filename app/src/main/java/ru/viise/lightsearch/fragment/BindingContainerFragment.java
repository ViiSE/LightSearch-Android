
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
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.activity.OnBackPressedListenerType;
import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.UnbindRecord;
import ru.viise.lightsearch.dialog.alert.CancelBindingAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.CancelBindingAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.ExitToAuthorizationAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.ExitToAuthorizationAlertDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromFragmentDefaultImpl;


public class BindingContainerFragment extends Fragment implements IBindingContainerFragment, OnBackPressedListener {

    private static final String TAG = "ContainerFragment";
    private int selected = 0; // 0 - CheckBindingMode, 1 - BindingMode, 2 - BindingDone

    private FragmentPageAdapter fragmentPageAdapter;


    private String[] skladArray;
    private String[] TKArray;

    private OnBackPressedListenerType onBackPressedListenerType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        ViewPager viewPager = view.findViewById(R.id.ViewPagerCon);

        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.TabLayoutCon);
        tabLayout.setupWithViewPager(viewPager);

        if(selected == 0)
            onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
        else if(selected == 1)
            onBackPressedListenerType = OnBackPressedListenerType.BINDING_FRAGMENT;

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager) {
        fragmentPageAdapter = new FragmentPageAdapter(getChildFragmentManager());
        SearchFragment searchFragment = new SearchFragment();
        BindingFragment bindingFragment = new BindingFragment();
        UnbindingFragment unbindingFragment = new UnbindingFragment();
        fragmentPageAdapter.addFragment(searchFragment, getString(R.string.fragment_search));
        fragmentPageAdapter.addFragment(bindingFragment, getString(R.string.fragment_binding));
        fragmentPageAdapter.addFragment(unbindingFragment, getString(R.string.fragment_unbinding));

        searchFragment.init(skladArray, TKArray);

        viewPager.setAdapter(fragmentPageAdapter);
        onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
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
    public void setUnbindingBarcode(String barcode, boolean isRun) {
        IUnbindingFragment unbindingFragment = getIUnbindingFragment();
        if(unbindingFragment != null) {
            if (isRun)
                unbindingFragment.setSearchBarcodeAndRun(barcode);
            else
                unbindingFragment.setSearchBarcode(barcode);
        }
    }

    @Override
    public void setBindingBarcode(String barcode, boolean isRun) {
        IBindingFragment bindingFragment = getIBindingFragment();
        if(bindingFragment != null)
            if(isRun)
                bindingFragment.setSearchBarcodeAndRun(barcode);
            else
                bindingFragment.setSearchBarcode(barcode);
    }

    private IBindingFragment getIBindingFragment() {
        ImplFinder<IBindingFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
        try { return finder.findImpl(IBindingFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    private IUnbindingFragment getIUnbindingFragment() {
        ImplFinder<IUnbindingFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
        try { return finder.findImpl(IUnbindingFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    private BindingFragment getBindingFragment() {
        ImplFinder<BindingFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
        try { return finder.findImpl(BindingFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    @Override
    public void switchToBind() {
        BindingFragment bindingFragment = getBindingFragment();
        if(bindingFragment != null) {
            if(bindingFragment.isVisible()) {
                bindingFragment.switchToBind();
                onBackPressedListenerType = OnBackPressedListenerType.BINDING_FRAGMENT;
                selected = 1;
            }
        }
    }

    @Override
    public void switchToCheckBind() {
        IBindingFragment bindingFragment = getIBindingFragment();
        if(bindingFragment != null) {
            bindingFragment.switchToCheckBind();
            onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
            selected = 0;
        }
    }

    @Override
    public void showResult(BindRecord record) {
        IBindingFragment bindingFragment = getIBindingFragment();
        if(bindingFragment != null) {
            bindingFragment.showResult(record);
        }
    }

    @Override
    public void showResult(UnbindRecord record) {
        IUnbindingFragment unbindingFragment = getIUnbindingFragment();
        if(unbindingFragment != null) {
            unbindingFragment.showResult(record);
        }
    }

    public void setupSearchFragment(String[] skladArray, String[] TKArray) {
        this.skladArray = skladArray;
        this.TKArray = TKArray;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListenerType == OnBackPressedListenerType.CONTAINER_FRAGMENT) {
            ExitToAuthorizationAlertDialogCreator exitTAADCr =
                    ExitToAuthorizationAlertDialogCreatorInit.exitToAuthorizationAlertDialogCreator(this.getActivity());
            exitTAADCr.create().show();
        } else if(onBackPressedListenerType == OnBackPressedListenerType.BINDING_FRAGMENT) {
            CancelBindingAlertDialogCreator cancelBADCr =
                    CancelBindingAlertDialogCreatorInit.cancelBindingAlertDialogCreator(this);
            cancelBADCr.create().show();
        }
    }
}
