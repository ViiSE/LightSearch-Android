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

public class ConfirmSoftCheckProductsPojo implements SendForm {

    @SerializedName("data")
    @Expose
    private List<ProductPojo> data;
    @SerializedName("user_ident")
    @Expose
    private String userIdentifier;
    @SerializedName("card_code")
    @Expose
    private String cardCode;

    private List<SoftCheckRecord> softCheckRecords;
    private String token;
    private int type; // 0 - soft check, 1 - cart

    public void setData(List<ProductPojo> data) {
        this.data = data;
    }

    public List<ProductPojo> getData() {
        return data;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setSoftCheckRecords(List<SoftCheckRecord> softCheckRecords) {
        this.softCheckRecords = softCheckRecords;
    }

    public List<SoftCheckRecord> getSoftCheckRecords() {
        return softCheckRecords;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
