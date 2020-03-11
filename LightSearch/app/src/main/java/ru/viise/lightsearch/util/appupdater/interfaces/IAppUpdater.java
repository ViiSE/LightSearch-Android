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

package ru.viise.lightsearch.util.appupdater.interfaces;

import android.support.annotation.NonNull;

import ru.viise.lightsearch.util.appupdater.AppUpdater;
import ru.viise.lightsearch.util.appupdater.enums.AppUpdaterError;
import ru.viise.lightsearch.util.appupdater.objects.Update;

public interface IAppUpdater {

    AppUpdater setUpdateJSON(@NonNull String jsonUrl);

    /**
     * Execute AppUpdater in background.
     *
     * @return this
     * @deprecated use {@link #start()} instead
     */
    AppUpdater init();

    /**
     * Execute AppUpdater in background.
     */
    void start();

    interface LibraryListener {
        void onSuccess(Update update);

        void onFailed(AppUpdaterError error);
    }
}
