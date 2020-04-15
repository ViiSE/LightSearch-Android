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
import ru.viise.lightsearch.data.entity.SkladListCommandResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.SkladListPojo;
import ru.viise.lightsearch.data.pojo.SkladListPojoRawResult;
import ru.viise.lightsearch.data.pojo.SkladListPojoResult;

public class SkladListProcess implements Process<SkladListPojo, SkladListPojoResult> {

    private final NetworkService networkService;

    public SkladListProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<SkladListPojo, SkladListPojoResult> apply(Command<SkladListPojo> command) {
        SkladListPojo tp = command.formForSend();
        try {
            Response<SkladListPojoRawResult> response = networkService
                    .getLightSearchAPI()
                    .skladList(tp.getToken())
                    .execute();
            if(response.isSuccessful()) {
                SkladListPojoRawResult tlp = response.body() == null ? new SkladListPojoRawResult() : response.body();
                return new SkladListCommandResult(tlp);
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

    private CommandResult<SkladListPojo, SkladListPojoResult> errorResult(String message) {
        SkladListPojoRawResult spr = errorPojo(message);
        return new SkladListCommandResult(spr);
    }

    private CommandResult<SkladListPojo, SkladListPojoResult> errorResult(Command<SkladListPojo> lastCommand, String message) {
        SkladListPojoRawResult spr = errorPojo(message);
        return new SkladListCommandResult(lastCommand, spr);
    }

    private SkladListPojoRawResult errorPojo(String message) {
        SkladListPojoRawResult spr = new SkladListPojoRawResult();
        spr.setIsDone(false);
        spr.setMessage(message);
        return spr;
    }
}
