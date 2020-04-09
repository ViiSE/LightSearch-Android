
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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.activity.OnBackPressedListenerType;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromFragmentDefaultImpl;
import ru.viise.lightsearch.fragment.pager.ZoomOutPageTransformer;


public class ContainerFragment extends Fragment implements OnBackPressedListener, IContainerFragment {

    public static final String TAG = "containerFragment";

    private String[] skladArray;
    private String[] TKArray;

    private OnBackPressedListenerType onBackPressedListenerType;

    private static final String ON_BACK_TYPE = "OnBackType";
    private int selected = 0; //0 - CONTAINER_FRAGMENT, 1 - SOFT_CHECK_FRAGMENT, 2 - BINDING FRAGMENT

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            selected = savedInstanceState.getInt(ON_BACK_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_bottom_nav, container, false);
        ViewPager viewPager = view.findViewById(R.id.ViewPagerBtmNavBar);
        setupViewPager(viewPager);
        BottomNavigationView btmNavView = view.findViewById(R.id.btmNavViewCon);

        btmNavView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.action_tasks:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.action_more:
                    viewPager.setCurrentItem(2, true);
                    break;
            }
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                btmNavView.getMenu().getItem(position).setChecked(true);
            }
        });

        if(selected == 0)
            onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
        else if(selected == 1)
            onBackPressedListenerType = OnBackPressedListenerType.SOFT_CHECK_FRAGMENT;
        else if(selected == 2)
            onBackPressedListenerType = OnBackPressedListenerType.BINDING_FRAGMENT;

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ON_BACK_TYPE, selected);
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentPageAdapter fragmentPageAdapter = new FragmentPageAdapter(getChildFragmentManager());

        SearchFragment searchFragment = new SearchFragment();
        TasksFragment tasksFragment = new TasksFragment();
        MoreFragment moreFragment = new MoreFragment();

        fragmentPageAdapter.addFragment(searchFragment, getString(R.string.fragment_search));
        fragmentPageAdapter.addFragment(tasksFragment, getString(R.string.fragment_tasks));
        fragmentPageAdapter.addFragment(moreFragment, getString(R.string.fragment_more));

        searchFragment.init(skladArray, TKArray);

//        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(fragmentPageAdapter);

        onBackPressedListenerType = OnBackPressedListenerType.CONTAINER_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        if(onBackPressedListenerType == OnBackPressedListenerType.CONTAINER_FRAGMENT) {
            getActivity().finish();
        }
    }

    @Override
    public void setSearchBarcode(String barcode, boolean isRun) {
        try {
            ImplFinder<ISearchFragment> finder = new ImplFinderFragmentFromFragmentDefaultImpl<>(this);
            ISearchFragment searchFragment = finder.findImpl(ISearchFragment.class);
            searchFragment.setSearchBarcode(barcode, isRun);
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

    public void setupSearchFragment(String[] skladArray, String[] TKArray) {
        this.skladArray = skladArray;
        this.TKArray = TKArray;
    }
}
