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

public class SkladListResultPojo implements SendForm {

    @SerializedName("is_done")
    private boolean isDone;
    @SerializedName("message")
    private String message;
    @SerializedName("sklad_list")
    private List<String> skladList;

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

    public List<String> getSkladList() {
        return skladList;
    }

    public void setSkladList(List<String> skladList) {
        this.skladList = skladList;
    }
}
