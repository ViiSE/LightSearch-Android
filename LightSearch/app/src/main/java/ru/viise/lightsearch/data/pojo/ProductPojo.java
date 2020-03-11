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

public class ProductPojo implements SendForm {

    @SerializedName("id")
    private String id;
    @SerializedName("subdiv")
    private String subdiv;
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private String amount;
    @SerializedName("price")
    private String price;
    @SerializedName("ei")
    private String ei;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSubdiv(String subdiv) {
        this.subdiv = subdiv;
    }

    public String getSubdiv() {
        return subdiv;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAmount(float amount) {
        this.amount = String.valueOf(amount);
    }

    public String getAmount() {
        return amount;
    }

    public void setPrice(float price) {
        this.price = String.valueOf(price);
    }

    public String getPrice() {
        return price;
    }

    public void setEi(String ei) {
        this.ei = ei;
    }

    public String getEi() {
        return ei;
    }
}
