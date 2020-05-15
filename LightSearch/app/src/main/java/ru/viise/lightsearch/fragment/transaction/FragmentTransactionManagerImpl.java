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

package ru.viise.lightsearch.fragment.transaction;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckPojoResult;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojoResult;
import ru.viise.lightsearch.fragment.AuthorizationFragment;
import ru.viise.lightsearch.fragment.ContainerFragment;
import ru.viise.lightsearch.fragment.ResultBindFragment;
import ru.viise.lightsearch.fragment.ResultSearchFragment;
import ru.viise.lightsearch.fragment.ResultUnbindFragment;
import ru.viise.lightsearch.fragment.StackFragmentTitle;

public class FragmentTransactionManagerImpl implements FragmentTransactionManager {

    private final FragmentActivity activity;

    public FragmentTransactionManagerImpl(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void doAuthorizationFragmentTransaction(boolean isNeedAnimation) {
        AuthorizationFragment authorizationFragment = new AuthorizationFragment();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(isNeedAnimation)
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.activity_manager, authorizationFragment);
        transaction.commit();
        activity.setTitle(activity.getString(R.string.fragment_authorization));
    }

    @Override
    public void doContainerFragmentTransaction(String[] skladArray, String[] TKArray, boolean isNeedAnimation) {
        ContainerFragment containerFragment = new ContainerFragment();
        containerFragment.setupSearchFragment(skladArray, TKArray);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(isNeedAnimation)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.activity_manager, containerFragment, ContainerFragment.TAG);
        transaction.addToBackStack(ContainerFragment.TAG);
        transaction.commit();
        activity.setTitle(activity.getString(R.string.fragment_container));
        StackFragmentTitle.push(activity.getString(R.string.fragment_authorization));
    }

    @Override
    public void doResultSearchFragmentTransaction(String title, List<SearchRecord> searchRecords) {
        ResultSearchFragment resultSearchFragment = new ResultSearchFragment();
        resultSearchFragment.setTitle(title);
        resultSearchFragment.init(searchRecords);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
        transaction.replace(R.id.activity_manager, resultSearchFragment, activity.getString(R.string.fragment_result_search));
        transaction.addToBackStack(activity.getString(R.string.fragment_result_search));
        transaction.commit();
        activity.setTitle(title);
        StackFragmentTitle.push(activity.getString(R.string.fragment_container));
    }

    @Override
    public void doResultBindFragmentTransaction(String title, CommandResult<BindCheckPojo, BindCheckPojoResult> result) {
        ResultBindFragment resultBindFragment = new ResultBindFragment();
        resultBindFragment.setTitle(title);
        resultBindFragment.init(result.data().getRecords(), result.data().getFactoryBarcode(), result.data().getSelected());
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
        transaction.replace(R.id.activity_manager, resultBindFragment, activity.getString(R.string.fragment_result_bind));
        transaction.addToBackStack(activity.getString(R.string.fragment_result_bind));
        transaction.commit();
        activity.setTitle(title);
        StackFragmentTitle.push(activity.getString(R.string.fragment_container));
    }

    @Override
    public void doBindingContainerFragmentTransactionFromResultBind() {
        activity.getSupportFragmentManager().popBackStackImmediate();
        activity.setTitle(activity.getString(R.string.fragment_container));
        StackFragmentTitle.pop();
    }

    @Override
    public void doResultUnbindFragmentTransaction(String title, CommandResult<UnbindCheckPojo, UnbindCheckPojoResult> result) {
        ResultUnbindFragment resultUnbindFragment = new ResultUnbindFragment();
        resultUnbindFragment.init(result.data().getRecords(), result.data().getFactoryBarcode());
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
        transaction.replace(R.id.activity_manager, resultUnbindFragment);
        transaction.addToBackStack(activity.getString(R.string.fragment_result_unbind));
        transaction.commit();
        activity.setTitle(title);
        StackFragmentTitle.push(activity.getString(R.string.fragment_container));
    }
}
