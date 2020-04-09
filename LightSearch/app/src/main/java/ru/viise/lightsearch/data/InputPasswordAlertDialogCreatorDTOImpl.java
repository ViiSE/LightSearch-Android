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

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import ru.viise.lightsearch.security.HashAlgorithm;

public class InputPasswordAlertDialogCreatorDTOImpl implements InputPasswordAlertDialogCreatorDTO {

    private final AlertDialogCreatorDTO alertDialogCreatorDTO;
    private final HashAlgorithm hashAlgorithm;
    private final TextView twHost;
    private final TextView twPort;
    private final EditText etHost;
    private final EditText etPort;
    private final Button bChangePassword;
    private final CheckBox cbSettings;

    public InputPasswordAlertDialogCreatorDTOImpl(AlertDialogCreatorDTO alertDialogCreatorDTO,
                                                  HashAlgorithm hashAlgorithm, TextView twHost, TextView twPort, EditText etHost,
                                                  EditText etPort, Button bChangePassword, CheckBox cbSettings) {
        this.alertDialogCreatorDTO = alertDialogCreatorDTO;
        this.hashAlgorithm = hashAlgorithm;
        this.twHost = twHost;
        this.twPort = twPort;
        this.etHost = etHost;
        this.etPort = etPort;
        this.bChangePassword = bChangePassword;
        this.cbSettings = cbSettings;
    }

    @Override
    public AlertDialogCreatorDTO alertDialogCreatorDTO() {
        return alertDialogCreatorDTO;
    }

    @Override
    public HashAlgorithm hashAlgorithms() {
        return hashAlgorithm;
    }

    @Override
    public TextView twHost() {
        return twHost;
    }

    @Override
    public TextView twPort() {
        return twPort;
    }

    @Override
    public EditText etHost() {
        return etHost;
    }

    @Override
    public EditText etPort() {
        return etPort;
    }

    @Override
    public Button bChangePassword() {
        return bChangePassword;
    }

    @Override
    public CheckBox cbSettings() {
        return cbSettings;
    }
}
