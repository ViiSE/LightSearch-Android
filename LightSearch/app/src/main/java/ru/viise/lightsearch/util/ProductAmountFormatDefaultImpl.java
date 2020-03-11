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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ProductAmountFormatDefaultImpl implements ProductAmountFormat {

    @Override
    public float format(String currentAmount) {
        if(currentAmount.contains("-"))
            return 0.2f;
        if(currentAmount.isEmpty())
            return 0.2f;
        if(currentAmount.equals("."))
            return 0.2f;
        else {
            Locale currentLocale = Locale.getDefault();
            DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(currentLocale);
            formatSymbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#.#", formatSymbols);
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            float tempCurrentAmount = Float.parseFloat(currentAmount);
            float curAmount = Float.parseFloat(decimalFormat.format(tempCurrentAmount));

            if (curAmount < 0.2f)
                return 0.2f;

            return curAmount;
        }
    }
}
