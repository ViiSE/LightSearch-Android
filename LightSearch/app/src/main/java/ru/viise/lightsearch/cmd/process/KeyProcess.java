/*
 * Copyright 2020 ViiSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.lightsearch.cmd.process;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.KeyCommandResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.KeyPojo;
import ru.viise.lightsearch.data.pojo.KeyPojoResult;

public class KeyProcess implements Process<KeyPojo, KeyPojoResult> {

    private final NetworkService networkService;

    public KeyProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<KeyPojo, KeyPojoResult> apply(Command<KeyPojo> ignore) {
        try {
            Response<KeyPojoResult> response = networkService
                    .getLightSearchAPI()
                    .key()
                    .execute();
            if(response.isSuccessful()) {
                KeyPojoResult kpr = response.body() == null ? new KeyPojoResult() : response.body();
                kpr.setIsDone(true);
                return new KeyCommandResult(kpr);
            } else {
                String json = response.errorBody() == null ?
                        "{" +
                            "\"message\":\"Неизвестная ошибка. Попробуйте выполнить запрос позже.\"," +
                            "\"code\":\"502\"" +
                        "}"
                        : response.errorBody().string();
                ErrorPojo ePojo;
                try {
                    ePojo = new Gson().fromJson(json, ErrorPojo.class);

                    if(ePojo == null)
                        throw new JsonParseException("Null object");

                    if(ePojo.getCode().equals(String.valueOf(HttpsURLConnection.HTTP_UNAUTHORIZED)))
                        return errorResult(ePojo.getMessage());
                    else
                        return errorResult(ePojo.getMessage());
                } catch (JsonParseException ex) {
                    if(response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        return errorResult("");
                    } else {
                        return errorResult("Неизвестная ошибка. Попробуйте выполнить запрос позже.");
                    }
                }
            }
        } catch (IOException ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if(message.equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(message.equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(message);
        }

    }

    private CommandResult<KeyPojo, KeyPojoResult> errorResult(String message) {
        KeyPojoResult kcr = errorPojo(message);
        return new KeyCommandResult(kcr);
    }

    private KeyPojoResult errorPojo(String message) {
        KeyPojoResult kpr = new KeyPojoResult();
        kpr.setIsDone(false);
        kpr.setMessage(message);
        return kpr;
    }
}
