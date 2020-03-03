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

package ru.viise.lightsearch.pref;

import android.content.SharedPreferences;

import ru.viise.lightsearch.data.AuthorizationPreferenceEnum;

public class HostPreferencesManagerDefaultImpl implements HostPreferencesManager {

    private final String HOST = AuthorizationPreferenceEnum.HOST.stringValue();
    private final String IP   = "127.0.0.1";

    private final SharedPreferences sPref;

    public HostPreferencesManagerDefaultImpl(SharedPreferences sPref) {
        this.sPref = sPref;
    }

    @Override
    public String loadHost() {
        return sPref.getString(HOST, IP);
    }

    @Override
    public void saveHost(String host) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(HOST, host);
        ed.apply();
    }
}
