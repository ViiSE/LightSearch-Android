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

import android.os.Parcel;
import android.os.Parcelable;

import ru.viise.lightsearch.util.CostFormat;
import ru.viise.lightsearch.util.CostFormatInit;

public class SoftCheckRecordDefaultImpl implements SoftCheckRecord {

    private final String name;
    private final String barcode;
    private final float price;
    private final String amountUnit;
    private final String priceUnit = UnitsEnum.CURRENT_PRICE_UNIT.stringValue();
    private final SubdivisionList subdivisions;

    private float maxAmount;
    private float currentAmount;
    private float totalCost;

    public SoftCheckRecordDefaultImpl(String name, String barcode, String price, String amountUnit,
                                      SubdivisionList subdivisions) {
        this.name = name;
        this.barcode = barcode;
        this.price = Float.parseFloat(price);
        this.amountUnit = amountUnit;
        this.subdivisions = subdivisions;

        float tempMaxAmount = 0;
        for(Subdivision subdivision : subdivisions.collection())
            tempMaxAmount += subdivision.productAmount();

        maxAmount = tempMaxAmount;

        currentAmount = 1;
        totalCost = calculateTotalCost();
    }

    private float calculateTotalCost() {
        CostFormat cFormat = CostFormatInit.costFormat();
        return cFormat.format(this.currentAmount * this.price);
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
    public void setProductsCount(float count) {
        currentAmount = count;
        if(currentAmount > maxAmount)
            currentAmount = maxAmount;
        if(currentAmount < 0)
            currentAmount = 0;
        totalCost = calculateTotalCost();
    }

    @Override
    public String totalCostWithUnit() {
        return totalCost + " " + priceUnit;
    }

    @Override
    public float totalCost() {
        return totalCost;
    }

    @Override
    public float maxAmount() {
        return maxAmount;
    }

    @Override
    public void setMaxAmount(float maxAmount) {
        this.maxAmount = maxAmount;
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
    public float currentAmount() {
        if(currentAmount > maxAmount) {
            currentAmount = maxAmount;
            totalCost = calculateTotalCost();
        }
        return currentAmount;
    }

    @Override
    public String maxAmountWithUnit() {
        return maxAmount + " " + amountUnit;
    }

    @Override
    public SubdivisionList subdivisions() {
        return subdivisions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(barcode);
        parcel.writeFloat(price);
        parcel.writeFloat(maxAmount);
        parcel.writeString(amountUnit);
        parcel.writeParcelable(subdivisions, i);
    }

    private SoftCheckRecordDefaultImpl(Parcel in) {
        name = in.readString();
        barcode = in.readString();
        price = in.readFloat();
        maxAmount = in.readFloat();
        amountUnit = in.readString();
        subdivisions = in.readParcelable(SubdivisionListDefaultImpl.class.getClassLoader());
    }

    public static final Parcelable.Creator<SoftCheckRecord> CREATOR
            = new Parcelable.Creator<SoftCheckRecord>() {

        @Override
        public SoftCheckRecord createFromParcel(Parcel in) {
            return new SoftCheckRecordDefaultImpl(in);
        }

        @Override
        public SoftCheckRecord[] newArray(int size) {
            return new SoftCheckRecordDefaultImpl[size];
        }
    };
}
