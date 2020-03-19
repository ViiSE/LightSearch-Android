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

public class CardCodePreferencesManagerDefaultImpl implements CardCodePreferencesManager {

    private final SharedPreferences sPref;

    public CardCodePreferencesManagerDefaultImpl(SharedPreferences sPref) {
        this.sPref = sPref;
    }

    @Override
    public String loadCardCode() {
        return sPref.getString("cardCode", "");
    }

    @Override
    public void saveCardCode(String cardCode) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("cardCode", cardCode);
        ed.apply();
    }
}
