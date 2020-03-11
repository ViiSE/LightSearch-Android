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

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import ru.viise.lightsearch.R;

public class SnackbarSoftCheckCreatorDefaultImpl implements SnackbarSoftCheckCreator {

    private final Fragment fragment;
    private final CoordinatorLayout coordLayout;
    private final String message;

    public SnackbarSoftCheckCreatorDefaultImpl(Fragment fragment, CoordinatorLayout coordLayout, String message) {
        this.fragment = fragment;
        this.coordLayout = coordLayout;
        this.message = message;
    }

    @Override
    public Snackbar createSnackbar() {
        Snackbar snackbar = Snackbar.make(coordLayout, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorSnackbar));
        snackbar.setActionTextColor(ContextCompat.getColor(fragment.getContext(), R.color.colorUndo));
        return snackbar;
    }
}
