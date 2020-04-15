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
import ru.viise.lightsearch.data.entity.OpenSoftCheckCommandResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojoResult;

public class OpenSoftCheckProcess implements Process<OpenSoftCheckPojo, OpenSoftCheckPojoResult> {

    private final NetworkService networkService;

    public OpenSoftCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<OpenSoftCheckPojo, OpenSoftCheckPojoResult> apply(Command<OpenSoftCheckPojo> command) {
        OpenSoftCheckPojo osc = command.formForSend();

        try {
            Response<OpenSoftCheckPojoResult> response = networkService
                    .getLightSearchAPI()
                    .openSoftCheck(osc.getToken(), osc)
                    .execute();
            if(response.isSuccessful()) {
                OpenSoftCheckPojoResult oscr = response.body() == null ? new OpenSoftCheckPojoResult() : response.body();
                return new OpenSoftCheckCommandResult(oscr);
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

    private CommandResult<OpenSoftCheckPojo, OpenSoftCheckPojoResult> errorResult(String message) {
        OpenSoftCheckPojoResult ucr = errorPojo(message);
        return new OpenSoftCheckCommandResult(ucr);
    }

    private CommandResult<OpenSoftCheckPojo, OpenSoftCheckPojoResult> errorResult(Command<OpenSoftCheckPojo> lastCommand, String message) {
        OpenSoftCheckPojoResult ucr = errorPojo(message);
        return new OpenSoftCheckCommandResult(lastCommand, ucr);
    }

    private OpenSoftCheckPojoResult errorPojo(String message) {
        OpenSoftCheckPojoResult ucr = new OpenSoftCheckPojoResult();
        ucr.setIsDone(false);
        ucr.setMessage(message);
        return ucr;
    }
}
