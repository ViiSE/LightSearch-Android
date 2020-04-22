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

package ru.viise.lightsearch.util;

import android.app.Activity;

import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.util.appupdater.AppUpdater;

public class UpdateCheckerAppUpdaterImpl implements UpdateChecker {

    private final Activity activity;
    private final PreferencesManager prefManager;

    public UpdateCheckerAppUpdaterImpl(Activity activity, PreferencesManager prefManager) {
        this.activity = activity;
        this.prefManager = prefManager;
    }

    @Override
    public void checkUpdate() {
        ApkFileDeleter apkFileDeleter = ApkFileDeleterInit.apkFileDeleter();
        apkFileDeleter.tryToDeleteApkFile();

        String URI = "http://" + prefManager.load(PreferencesManagerType.HOST_UPDATER_MANAGER) + ":"
                + prefManager.load(PreferencesManagerType.PORT_UPDATER_MANAGER)
                + "/update/info/update.json";

        new AppUpdater(activity)
                .setUpdateJSON(URI)
                .start();
    }
}
