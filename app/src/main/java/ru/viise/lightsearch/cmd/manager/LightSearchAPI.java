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

package ru.viise.lightsearch.cmd.manager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckResultPojo;
import ru.viise.lightsearch.data.pojo.BindPojo;
import ru.viise.lightsearch.data.pojo.BindResultPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckResultPojo;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckResultPojo;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojo;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsResultPojo;
import ru.viise.lightsearch.data.pojo.LoginPojo;
import ru.viise.lightsearch.data.pojo.LoginResultPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckResultPojo;
import ru.viise.lightsearch.data.pojo.SearchResultPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckResultPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojo;
import ru.viise.lightsearch.data.pojo.UnbindResultPojo;

public interface LightSearchAPI {
    @POST("/clients/login")
    Call<LoginResultPojo> login(@Body LoginPojo data);

    @GET("/clients/products")
    Call<SearchResultPojo> searchProduct(
            @Header("authorization") String token,
            @Query("barcode") String barcode,
            @Query("sklad") String sklad,
            @Query("tk") String tk);

    @POST("/clients/products/actions/bind_check")
    Call<BindCheckResultPojo> bindCheckProduct(
            @Header("authorization") String token,
            @Body BindCheckPojo data);

    @POST("/clients/products/actions/bind")
    Call<BindResultPojo> bindProduct(
            @Header("authorization") String token,
            @Body BindPojo data);

    @POST("/clients/products/actions/unbind_check")
    Call<UnbindCheckResultPojo> unbindCheckProduct(
            @Header("authorization") String token,
            @Body UnbindCheckPojo data);

    @POST("/clients/products/actions/unbind")
    Call<UnbindResultPojo> unbindProduct(
            @Header("authorization") String token,
            @Body UnbindPojo data);

    @POST("/clients/softChecks/actions/open")
    Call<OpenSoftCheckResultPojo> openSoftCheck(
            @Header("authorization") String token,
            @Body OpenSoftCheckPojo data);

    @POST("/clients/softChecks/actions/cancel")
    Call<CancelSoftCheckResultPojo> cancelSoftCheck(
            @Header("authorization") String token,
            @Body CancelSoftCheckPojo data);

    @POST("/clients/softChecks/actions/confirm-products")
    Call<ConfirmSoftCheckProductsResultPojo> confirmSoftCheckProducts(
            @Header("authorization") String token,
            @Body ConfirmSoftCheckProductsPojo data);

    @POST("/clients/softChecks/actions/close")
    Call<CloseSoftCheckResultPojo> closeSoftCheck(
            @Header("authorization") String token,
            @Body CloseSoftCheckPojo data);
}
