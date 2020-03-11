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

public class InputPasswordAlertDialogCreatorDTOInit {

    public static InputPasswordAlertDialogCreatorDTO inputPasswordAlertDialogCreatorDTO(
            AlertDialogCreatorDTO alertDialogCreatorDTO, HashAlgorithm hashAlgorithm,
            TextView twHost, TextView twPort, EditText etHost, EditText etPort, Button bChangePassword,
            CheckBox cbSettings) {
        return new InputPasswordAlertDialogCreatorDTODefaultImpl(alertDialogCreatorDTO, hashAlgorithm,
                twHost, twPort, etHost, etPort, bChangePassword, cbSettings);
    }
}