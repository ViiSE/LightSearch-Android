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
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SubdivisionListDefaultImpl implements SubdivisionList {

    private final List<Subdivision> subdivisions = new ArrayList<>();
    private final String amountUnit;

    public SubdivisionListDefaultImpl(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    @Override
    public void addSubdivision(Subdivision subdivision) {
        subdivisions.add(subdivision);
    }

    @Override
    public Collection<Subdivision> collection() {
        return subdivisions;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for(Subdivision subdivision : subdivisions) {
            res.append("<pre>\t</pre>") // \t
                    .append(subdivision.name())
                    .append(" - ")
                    .append(subdivision.productAmount())
                    .append(" ")
                    .append(amountUnit)
                    .append("<br>"); // \n
        }
        return res.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(amountUnit);
        parcel.writeTypedList(subdivisions);
    }

    private SubdivisionListDefaultImpl(Parcel in) {
        amountUnit = in.readString();
        in.readTypedList(subdivisions, SubdivisionDefaultImpl.CREATOR);
    }

    public static final Parcelable.Creator<SubdivisionList> CREATOR
            = new Parcelable.Creator<SubdivisionList>() {

        @Override
        public SubdivisionList createFromParcel(Parcel in) {
            return new SubdivisionListDefaultImpl(in);
        }

        @Override
        public SubdivisionList[] newArray(int size) {
            return new SubdivisionList[size];
        }
    };
}
