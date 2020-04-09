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

package ru.viise.lightsearch.cmd.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckPojoRawResult;
import ru.viise.lightsearch.data.pojo.BindPojo;
import ru.viise.lightsearch.data.pojo.BindPojoResult;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.CheckAuthPojoResult;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojo;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojoRawResult;
import ru.viise.lightsearch.data.pojo.LoginPojo;
import ru.viise.lightsearch.data.pojo.LoginPojoResult;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.SearchPojoRawResult;
import ru.viise.lightsearch.data.pojo.SkladListPojoRawResult;
import ru.viise.lightsearch.data.pojo.TKListPojoRawResult;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojoRawResult;
import ru.viise.lightsearch.data.pojo.UnbindPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojoResult;

public interface LightSearchAPI {
    @GET("/clients/checkAuth")
    Call<CheckAuthPojoResult> checkAuth(@Header("authorization") String token);

    @POST("/clients/login")
    Call<LoginPojoResult> login(@Body LoginPojo data);

    @GET("/clients/products")
    Call<SearchPojoRawResult> searchProduct(
            @Header("authorization") String token,
            @Query("barcode") String barcode,
            @Query("sklad") String sklad,
            @Query("tk") String tk);

    @POST("/clients/products/actions/bind_check")
    Call<BindCheckPojoRawResult> bindCheckProduct(
            @Header("authorization") String token,
            @Body BindCheckPojo data);

    @POST("/clients/products/actions/bind")
    Call<BindPojoResult> bindProduct(
            @Header("authorization") String token,
            @Body BindPojo data);

    @POST("/clients/products/actions/unbind_check")
    Call<UnbindCheckPojoRawResult> unbindCheckProduct(
            @Header("authorization") String token,
            @Body UnbindCheckPojo data);

    @POST("/clients/products/actions/unbind")
    Call<UnbindPojoResult> unbindProduct(
            @Header("authorization") String token,
            @Body UnbindPojo data);

    @GET("/clients/softChecks/products")
    Call<SearchPojoRawResult> searchSoftCheckProducts(
            @Header("authorization") String token,
            @Query("barcode") String barcode,
            @Query("username") String username);

    @POST("/clients/softChecks/actions/open")
    Call<OpenSoftCheckPojoResult> openSoftCheck(
            @Header("authorization") String token,
            @Body OpenSoftCheckPojo data);

    @POST("/clients/softChecks/actions/cancel")
    Call<CancelSoftCheckPojoResult> cancelSoftCheck(
            @Header("authorization") String token,
            @Body CancelSoftCheckPojo data);

    @POST("/clients/softChecks/actions/confirm-products")
    Call<ConfirmSoftCheckProductsPojoRawResult> confirmSoftCheckProducts(
            @Header("authorization") String token,
            @Body ConfirmSoftCheckProductsPojo data);

    @POST("/clients/softChecks/actions/close")
    Call<CloseSoftCheckPojoResult> closeSoftCheck(
            @Header("authorization") String token,
            @Body CloseSoftCheckPojo data);

    @GET("/clients/skladList")
    Call<SkladListPojoRawResult> skladList(@Header("authorization") String token);

    @GET("/clients/tkList")
    Call<TKListPojoRawResult> TKList(@Header("authorization") String token);
}
