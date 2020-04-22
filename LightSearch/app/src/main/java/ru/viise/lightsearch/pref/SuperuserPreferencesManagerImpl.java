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

public class SuperuserPreferencesManagerImpl implements SuperuserPreferencesManager {

    private final String SUPERUSER = AuthorizationPreferenceEnum.SUPERUSER.stringValue();
    private final SharedPreferences sPref;

    public SuperuserPreferencesManagerImpl(SharedPreferences sPref) {
        this.sPref = sPref;
    }

    @Override
    public String loadPassHash() {
        return sPref.getString(SUPERUSER, "");
    }

    @Override
    public void savePassHash(String passHash) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SUPERUSER, passHash);
        ed.apply();
    }
}
