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

package ru.viise.lightsearch.cmd.process;

import com.google.gson.Gson;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.TKListCommandResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.TKListPojo;
import ru.viise.lightsearch.data.pojo.TKListPojoRawResult;
import ru.viise.lightsearch.data.pojo.TKListPojoResult;

public class TKListProcess implements Process<TKListPojo, TKListPojoResult> {

    private final NetworkService networkService;

    public TKListProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<TKListPojo, TKListPojoResult> apply(Command<TKListPojo> command) {
        TKListPojo tp = command.formForSend();
        try {
            Response<TKListPojoRawResult> response = networkService
                    .getLightSearchAPI()
                    .TKList(tp.getToken())
                    .execute();
            if(response.isSuccessful()) {
                TKListPojoRawResult tlp = response.body() == null ? new TKListPojoRawResult() : response.body();
                return new TKListCommandResult(tlp);
            } else {
                String json = response.errorBody() == null ?
                        "{" +
                            "\"message\":\"Неизвестная ошибка. Попробуйте выполнить запрос позже.\"," +
                            "\"code\":\"502\"" +
                        "}"
                        : response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                if(ePojo == null) {
                    ePojo = new Gson().fromJson(
                            "{" +
                                    "\"message\":\"Неизвестная ошибка. Попробуйте выполнить запрос позже.\"," +
                                    "\"code\":\"502\"" +
                                    "}",
                            ErrorPojo.class);
                }

                if(response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    return errorResult(command, "");
                } else if(ePojo.getCode().equals(String.valueOf(HttpsURLConnection.HTTP_UNAUTHORIZED)))
                    return errorResult(command, ePojo.getMessage());
                else
                    return errorResult(ePojo.getMessage());
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

    private CommandResult<TKListPojo, TKListPojoResult> errorResult(String message) {
        TKListPojoRawResult tpr = errorPojo(message);
        return new TKListCommandResult(tpr);
    }

    private CommandResult<TKListPojo, TKListPojoResult> errorResult(Command<TKListPojo> lastCommand, String message) {
        TKListPojoRawResult tpr = errorPojo(message);
        return new TKListCommandResult(lastCommand, tpr);
    }

    private TKListPojoRawResult errorPojo(String message) {
        TKListPojoRawResult tpr = new TKListPojoRawResult();
        tpr.setIsDone(false);
        tpr.setMessage(message);
        return tpr;
    }
}
