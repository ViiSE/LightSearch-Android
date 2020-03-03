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

public class SubdivisionDefaultImpl implements Subdivision {

    private final String name;
    private final float productAmount;

    public SubdivisionDefaultImpl(String name, String productAmount) {
        this.name = name;
        this.productAmount = Float.parseFloat(productAmount);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public float productAmount() {
        return productAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeFloat(productAmount);
    }

    private SubdivisionDefaultImpl(Parcel in) {
        name = in.readString();
        productAmount = in.readFloat();
    }

    public static final Parcelable.Creator<Subdivision> CREATOR
            = new Parcelable.Creator<Subdivision>() {

        @Override
        public Subdivision createFromParcel(Parcel in) {
            return new SubdivisionDefaultImpl(in);
        }

        @Override
        public Subdivision[] newArray(int size) {
            return new SubdivisionDefaultImpl[size];
        }
    };
}
