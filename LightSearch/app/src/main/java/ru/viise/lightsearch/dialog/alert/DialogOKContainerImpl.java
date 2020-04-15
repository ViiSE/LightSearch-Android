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
import android.widget.TextView;

public class DialogOKContainerImpl implements DialogOKContainer {

    private final View dialogOKView;
    private final Button buttonOK;
    private final TextView textViewTitle;
    private final TextView textViewResult;

    public DialogOKContainerImpl(
            View dialogOKView, Button buttonOK, TextView textViewTitle, TextView textViewResult) {
        this.dialogOKView = dialogOKView;
        this.buttonOK = buttonOK;
        this.textViewTitle = textViewTitle;
        this.textViewResult = textViewResult;
    }

    @Override
    public View dialogOKView() {
        return dialogOKView;
    }

    @Override
    public Button buttonOK() {
        return buttonOK;
    }

    @Override
    public TextView textViewTitle() {
        return textViewTitle;
    }

    @Override
    public TextView textViewResult() {
        return textViewResult;
    }
}