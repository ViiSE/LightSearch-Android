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

package ru.viise.lightsearch.fragment.snackbar;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import ru.viise.lightsearch.R;

public class SnackbarSoftCheckCreatorDefaultImpl implements SnackbarSoftCheckCreator {

    private final Fragment fragment;
    private final View view;
    private final String message;

    public SnackbarSoftCheckCreatorDefaultImpl(Fragment fragment, View view, String message) {
        this.fragment = fragment;
        this.view = view;
        this.message = message;
    }

    @Override
    public Snackbar createSnackbar() {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorSnackbar));
        snackbar.setActionTextColor(ContextCompat.getColor(fragment.getContext(), R.color.colorUndo));
        return snackbar;
    }
}
