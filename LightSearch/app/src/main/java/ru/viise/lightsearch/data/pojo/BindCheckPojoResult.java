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

package ru.viise.lightsearch.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.viise.lightsearch.data.BindRecord;

public class BindCheckPojoResult implements SendForm {

    @SerializedName("is_done")
    private boolean isDone;
    @SerializedName("records")
    private List<BindRecord> records;
    @SerializedName("message")
    private String message;

    private String factoryBarcode;
    private int selected;

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setRecords(List<BindRecord> records) {
        this.records = records;
    }

    public List<BindRecord> getRecords() {
        return records;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setFactoryBarcode(String factoryBarcode) {
        this.factoryBarcode = factoryBarcode;
    }

    public String getFactoryBarcode() {
        return factoryBarcode;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
    }
}
