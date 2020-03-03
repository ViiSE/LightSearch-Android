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

package ru.viise.lightsearch.dialog.spots;

import android.app.Activity;
import android.app.AlertDialog;

import dmax.dialog.SpotsDialog;
import ru.viise.lightsearch.R;
import ru.viise.lightsearch.dialog.alert.AlertDialogUtil;

public class SpotsDialogCreatorDmaxImpl implements SpotsDialogCreator {

    private final Activity activity;
    private final int messageId;

    public SpotsDialogCreatorDmaxImpl(Activity activity, int messageId) {
        this.activity = activity;
        this.messageId = messageId;
    }

    @Override
    public AlertDialog create() {
        AlertDialog spotsDialog = new SpotsDialog.Builder().setContext(activity)
                .setCancelable(false).setMessage(messageId).setTheme(R.style.SpotsDialogRoundCorner)
                .build();
        AlertDialogUtil.setTransparentBackground(spotsDialog);
        spotsDialog.setCanceledOnTouchOutside(false);

        return spotsDialog;
    }
}
