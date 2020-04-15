
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivity;


public class TasksFragment extends Fragment {

    private static final String TAG = "TasksFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        ImageButton scButton = view.findViewById(R.id.buttonTaskSoftCheck);
        ImageButton bindingButton = view.findViewById(R.id.buttonTaskBinding);

        scButton.setOnClickListener(view1 -> {
            view1.startAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.alpha));
            ManagerActivity managerActivity = (ManagerActivity) this.getActivity();
            managerActivity.doSoftCheckContainerFragmentTransaction();
        });

        bindingButton.setOnClickListener(view2 ->  {
            view2.startAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.alpha));
            ManagerActivity managerActivity = (ManagerActivity) this.getActivity();
            managerActivity.doBindingContainerFragmentTransaction();
        });

        return view;
    }
}
