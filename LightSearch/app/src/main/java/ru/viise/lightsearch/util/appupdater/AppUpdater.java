/*
 * Copyright 2016 javiersantos.
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

package ru.viise.lightsearch.util.appupdater;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import ru.viise.lightsearch.dialog.alert.UpdateAlertDialogCreatorImpl;
import ru.viise.lightsearch.util.appupdater.enums.AppUpdaterError;
import ru.viise.lightsearch.util.appupdater.interfaces.IAppUpdater;
import ru.viise.lightsearch.util.appupdater.objects.Update;

public class AppUpdater implements IAppUpdater {

    private Activity activity;
    private LibraryPreferences libraryPreferences;
    private String JsonUrl;
    private UtilsAsync.LatestAppVersion latestAppVersion;

    public AppUpdater(Activity activity) {
        this.activity = activity;
        this.libraryPreferences = new LibraryPreferences(activity);
    }

    @Override
    public AppUpdater setUpdateJSON(@NonNull String jsonUrl) {
        this.JsonUrl = jsonUrl;
        return this;
    }

    @Override
    public AppUpdater init() {
        start();
        return this;
    }

    @Override
    public void start() {
        latestAppVersion = new UtilsAsync.LatestAppVersion(activity, JsonUrl, new LibraryListener() {
            @Override
            public void onSuccess(Update update) {
                if (activity != null && (activity).isFinishing())
                    return;

                Update installedUpdate = new Update(UtilsLibrary.getAppInstalledVersion(activity),
                        UtilsLibrary.getAppInstalledVersionCode(activity));

                if (UtilsLibrary.isUpdateAvailable(installedUpdate, update)) {
                    Integer successfulChecks = libraryPreferences.getSuccessfulChecks();

                    AlertDialog alertDialog =
                            new UpdateAlertDialogCreatorImpl(activity, update)
                            .create();

                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                    libraryPreferences.setSuccessfulChecks(successfulChecks + 1);
                }
            }

            @Override
            public void onFailed(AppUpdaterError error) {
                    throw new IllegalArgumentException("JSON file is not valid!");
            }
        });

        latestAppVersion.execute();
    }
}
