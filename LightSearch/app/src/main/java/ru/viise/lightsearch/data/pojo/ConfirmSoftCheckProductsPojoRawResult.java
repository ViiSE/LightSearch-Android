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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.viise.lightsearch.data.SoftCheckRecord;

public class ConfirmSoftCheckProductsPojoRawResult implements SendForm {

    @SerializedName("is_done")
    @Expose
    private boolean isDone;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ProductPojo> data;

    private List<SoftCheckRecord> softCheckRecords;

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<ProductPojo> getData() {
        return data;
    }

    public void setData(List<ProductPojo> data) {
        this.data = data;
    }

    public void setSoftCheckRecords(List<SoftCheckRecord> softCheckRecords) {
        this.softCheckRecords = softCheckRecords;
    }

    public List<SoftCheckRecord> getSoftCheckRecords() {
        return softCheckRecords;
    }
}
