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

public class CartRecordImpl implements CartRecord {

    private final String name;
    private final String barcode;
    private final float oldMaxAmount;
    private final float price;
    private final String amountUnit;
    private final String priceUnit = UnitsEnum.CURRENT_PRICE_UNIT.stringValue();
    private final SubdivisionList subdivisions;
    private boolean isConfirmed;

    private float newMaxAmount;
    private float currentAmount;
    private float totalCost;

    public CartRecordImpl(String name, String barcode, float price, String amountUnit,
                          SubdivisionList subdivisions, float currentAmount, float oldMaxAmount, float newMaxAmount) {
        this.name = name;
        this.barcode = barcode;
        this.price = price;
        this.amountUnit = amountUnit;
        this.subdivisions = subdivisions;

        this.oldMaxAmount = oldMaxAmount;
        this.newMaxAmount = newMaxAmount;

        if(currentAmount <= this.newMaxAmount) {
            this.currentAmount = currentAmount;
            isConfirmed = true;
        } else {
            this.currentAmount = this.newMaxAmount;
            isConfirmed = false;
        }

        totalCost = calculateTotalCost();
    }

    private float calculateTotalCost() {
        CostFormat cFormat = CostFormatInit.costFormat();
        return cFormat.format(this.currentAmount * this.price);
    }

    @Override
    public boolean isConfirmed() {
        return isConfirmed;
    }

    @Override
    public String oldMaxAmountWithUnit() {
        return oldMaxAmount + " " + amountUnit;
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
    public float currentAmount() {
        if(currentAmount > newMaxAmount) {
            currentAmount = newMaxAmount;
            totalCost = calculateTotalCost();
        }
        return currentAmount;
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
        return newMaxAmount;
    }

    @Override
    public float refreshMaxAmount() {
        return maxAmount();
    }

    @Override
    public void setMaxAmount(float maxAmount) {
        newMaxAmount = maxAmount;
    }

    @Override
    public String maxAmountWithUnit() {
        return newMaxAmount + " " + amountUnit;
    }

    @Override
    public void setProductsCount(float count) {
        currentAmount = count;
        if(currentAmount > newMaxAmount)
            currentAmount = newMaxAmount;
        if(currentAmount < 0)
            currentAmount = 0;
        totalCost = calculateTotalCost();
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
        parcel.writeFloat(newMaxAmount);
        parcel.writeString(amountUnit);
        parcel.writeParcelable(subdivisions, i);
        parcel.writeFloat(oldMaxAmount);
    }

    private CartRecordImpl(Parcel in) {
        name = in.readString();
        barcode = in.readString();
        price = in.readFloat();
        newMaxAmount = in.readFloat();
        amountUnit = in.readString();
        subdivisions = in.readParcelable(SubdivisionListImpl.class.getClassLoader());
        oldMaxAmount = in.readFloat();
    }

    public static final Parcelable.Creator<SoftCheckRecord> CREATOR
            = new Parcelable.Creator<SoftCheckRecord>() {

        @Override
        public SoftCheckRecord createFromParcel(Parcel in) {
            return new CartRecordImpl(in);
        }

        @Override
        public SoftCheckRecord[] newArray(int size) {
            return new CartRecordImpl[size];
        }
    };
}
