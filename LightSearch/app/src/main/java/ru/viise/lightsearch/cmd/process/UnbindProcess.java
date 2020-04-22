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
import com.google.gson.JsonParseException;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.UnbindCommandResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojoResult;

public class UnbindProcess implements Process<UnbindPojo, UnbindPojoResult> {

    private final NetworkService networkService;

    public UnbindProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<UnbindPojo, UnbindPojoResult> apply(Command<UnbindPojo> command) {
        UnbindPojo up = command.formForSend();
        try {
            Response<UnbindPojoResult> response = networkService
                    .getLightSearchAPI()
                    .unbindProduct(up.getToken(), up)
                    .execute();
            if(response.isSuccessful()) {
                UnbindPojoResult ur = response.body() == null ? new UnbindPojoResult() : response.body();
                return new UnbindCommandResult(ur);
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
                        return errorResult(command, ePojo.getMessage());
                    else
                        return errorResult(ePojo.getMessage());
                } catch (JsonParseException ex) {
                    if(response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        return errorResult(command, "");
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

    private CommandResult<UnbindPojo, UnbindPojoResult> errorResult(String message) {
        UnbindPojoResult ur = errorPojo(message);
        return new UnbindCommandResult(ur);
    }

    private CommandResult<UnbindPojo, UnbindPojoResult> errorResult(Command<UnbindPojo> lastCommand, String message) {
        UnbindPojoResult ur = errorPojo(message);
        return new UnbindCommandResult(lastCommand, ur);
    }

    private UnbindPojoResult errorPojo(String message) {
        UnbindPojoResult ur = new UnbindPojoResult();
        ur.setIsDone(false);
        ur.setMessage(message);
        return ur;
    }
}
