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

package ru.viise.lightsearch.data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

public class AlertDialogCreatorDTOImpl implements AlertDialogCreatorDTO {

    private final Activity rootActivity;
    private final LayoutInflater inflater;
    private final SharedPreferences sPref;

    public AlertDialogCreatorDTOImpl(Activity rootActivity,
                                     @NonNull LayoutInflater inflater, SharedPreferences sPref) {
        this.rootActivity = rootActivity;
        this.inflater = inflater;
        this.sPref = sPref;
    }

    @Override
    public Activity rootActivity() {
        return rootActivity;
    }

    @Override
    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public SharedPreferences sharedPreferences() {
        return sPref;
    }
}
