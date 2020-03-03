/*
 *  Copyright 2020 ViiSE.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ru.viise.lightsearch.pref;

import android.content.SharedPreferences;

import ru.viise.lightsearch.data.AuthorizationPreferenceEnum;

public class TokenPreferencesManagerDefaultImpl implements TokenPreferencesManager {

    private final String TOKEN = AuthorizationPreferenceEnum.TOKEN.stringValue();

    private final SharedPreferences sPref;

    public TokenPreferencesManagerDefaultImpl(SharedPreferences sPref) {
        this.sPref = sPref;
    }


    @Override
    public String loadToken() {
        return sPref.getString(TOKEN, "");
    }

    @Override
    public void saveToken(String token) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(TOKEN, token);
        ed.apply();
    }
}
