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

import java.util.List;

public class SearchRecordImpl implements SearchRecord {

    private final String name;
    private final String barcode;
    private final float price;
    private final String amountUnit;
    private final String priceUnit = UnitsEnum.CURRENT_PRICE_UNIT.stringValue();
    private final SubdivisionList subdivisions;

    private float maxAmount;
    private boolean isRender = false;

    public SearchRecordImpl(String name, String barcode, String price, String amountUnit,
                            SubdivisionList subdivisions) {
        this.name = name;
        this.barcode = barcode;
        this.price = Float.parseFloat(price);
        this.amountUnit = amountUnit;
        if(subdivisions == null)
            this.subdivisions = new SubdivisionListImpl(this.amountUnit);
        else
            this.subdivisions = subdivisions;

        int tempMaxAmount = 0;
        for(Subdivision subdivision : this.subdivisions.collection())
            tempMaxAmount += subdivision.productAmount();

        maxAmount = tempMaxAmount;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String barcode() {
        return barcode;
    }

    @Override
    public float price() {
        return price;
    }

    @Override
    public String priceWithUnit() {
        return price + " " + priceUnit;
    }

    @Override
    public String amountUnit() {
        return amountUnit;
    }

    @Override
    public String maxAmountWithUnit() {
        return calculateMaxAmount() + " " + amountUnit;
    }

    @Override
    public SubdivisionList subdivisions() {
        return subdivisions;
    }

    @Override
    public Subdivision getSubdivision(int index) {
        if(index > subdivisions.collection().size())
            return null;
        if(index < 0)
            return null;
        return ((List<Subdivision>) subdivisions.collection()).get(index);
    }

    @Override
    public boolean isRender() {
        return isRender;
    }

    @Override
    public void setIsRender(boolean isRender) {
        this.isRender = isRender;
    }

    private float calculateMaxAmount() {
        int tempMaxAmount = 0;
        for(Subdivision subdivision : this.subdivisions.collection())
            tempMaxAmount += subdivision.productAmount();

        maxAmount = tempMaxAmount;
        return maxAmount;
    }
}
