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

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogSettingsContainerImpl implements DialogSettingsContainer {

    private final View dialogSettingsView;
    private final EditText editText;
    private final TextView textViewTitle;
    private final TextView textViewResult;
    private final Button buttonOK;
    private final Button buttonCancel;

    public DialogSettingsContainerImpl(
            View dialogSettingsView, EditText editText, TextView textViewTitle,
            TextView textViewResult, Button buttonOK, Button buttonCancel) {
        this.dialogSettingsView = dialogSettingsView;
        this.editText = editText;
        this.textViewTitle = textViewTitle;
        this.textViewResult = textViewResult;
        this.buttonOK = buttonOK;
        this.buttonCancel = buttonCancel;
    }

    @Override
    public View dialogSettingsView() {
        return dialogSettingsView;
    }

    @Override
    public TextView textViewTitle() {
        return textViewTitle;
    }

    @Override
    public TextView textViewResult() {
        return textViewResult;
    }

    @Override
    public EditText editText() {
        return editText;
    }

    @Override
    public Button buttonOK() {
        return buttonOK;
    }

    @Override
    public Button buttonCancel() {
        return buttonCancel;
    }
}
