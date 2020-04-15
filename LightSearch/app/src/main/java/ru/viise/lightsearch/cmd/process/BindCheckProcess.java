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
import ru.viise.lightsearch.data.entity.BindCheckCommandResult;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckPojoRawResult;
import ru.viise.lightsearch.data.pojo.BindCheckPojoResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;

public class BindCheckProcess implements Process<BindCheckPojo, BindCheckPojoResult> {

    private final NetworkService networkService;

    public BindCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<BindCheckPojo, BindCheckPojoResult> apply(Command<BindCheckPojo> command) {
        BindCheckPojo bcp = command.formForSend();

        try {
            Response<BindCheckPojoRawResult> response = networkService
                    .getLightSearchAPI()
                    .bindCheckProduct(bcp.getToken(), bcp)
                    .execute();
            if(response.isSuccessful()) {
                BindCheckPojoRawResult bcrp = response.body() == null ? new BindCheckPojoRawResult() : response.body();
                bcrp.setFactoryBarcode(bcp.getFactoryBarcode());
                bcrp.setSelected(bcp.getSelected());
                return new BindCheckCommandResult(command, bcrp);
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
            else if(message.contains("timed out"))
                message = "Не удалось установить связь с сервером.";
            else if(message.equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(message);
        }

    }

    private CommandResult<BindCheckPojo, BindCheckPojoResult> errorResult(String message) {
        BindCheckPojoRawResult bcr = errorPojo(message);
        return new BindCheckCommandResult(bcr);
    }

    private CommandResult<BindCheckPojo, BindCheckPojoResult> errorResult(Command<BindCheckPojo> lastCommand, String message) {
        BindCheckPojoRawResult bcr = errorPojo(message);
        return new BindCheckCommandResult(lastCommand, bcr);
    }

    private BindCheckPojoRawResult errorPojo(String message) {
        BindCheckPojoRawResult bcr = new BindCheckPojoRawResult();
        bcr.setIsDone(false);
        bcr.setMessage(message);
        return bcr;
    }
}
