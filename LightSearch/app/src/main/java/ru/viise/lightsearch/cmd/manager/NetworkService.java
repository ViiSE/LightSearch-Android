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

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private Retrofit retrofit;
    private static NetworkService networkService;
    private static String OLD_BASE_URL = "";
    private static String BASE_URL = "http://127.0.0.1:50000";

    public static void setBaseUrl(String host, String port) {
        OLD_BASE_URL = BASE_URL;
        BASE_URL = "http://" + host + ":" + port;
    }

    private NetworkService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if(networkService == null)
            networkService = new NetworkService();
        else if(!OLD_BASE_URL.isEmpty()) {
            networkService = new NetworkService();
            OLD_BASE_URL = "";
        }
        return networkService;
    }

    public LightSearchAPI getLightSearchAPI() {
        return retrofit.create(LightSearchAPI.class);
    }

    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        return okHttpClientBuilder.build();
    }
}
