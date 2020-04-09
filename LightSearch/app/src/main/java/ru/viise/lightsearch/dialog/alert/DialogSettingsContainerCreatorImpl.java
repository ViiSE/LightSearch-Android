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

package ru.viise.lightsearch.dialog.alert;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.viise.lightsearch.R;

public class DialogSettingsContainerCreatorImpl implements DialogSettingsContainerCreator {

    private final Activity activity;

    public DialogSettingsContainerCreatorImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public DialogSettingsContainer createDialogSettingsContainer() {
        View settingsViewCreate = activity.getLayoutInflater().inflate(R.layout.dialog_settings, null);
        EditText editText = settingsViewCreate.findViewById(R.id.editTextPasswordSettings);
        TextView textViewTitle = settingsViewCreate.findViewById(R.id.textViewSettingsDialogTitle);
        TextView textViewResult = settingsViewCreate.findViewById(R.id.textViewSign);
        Button buttonOK = settingsViewCreate.findViewById(R.id.buttonDialogOKSettings);
        Button buttonCancel = settingsViewCreate.findViewById(R.id.buttonDialogCancelSettings);

        return DialogSettingsContainerInit.dialogSettingsContainer(
                settingsViewCreate, editText, textViewTitle, textViewResult,  buttonOK, buttonCancel);
    }
}
