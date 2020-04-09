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
import ru.viise.lightsearch.data.entity.CloseSoftCheckCommandResult;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;

public class CloseSoftCheckProcess implements Process<CloseSoftCheckPojo, CloseSoftCheckPojoResult> {

    private final NetworkService networkService;

    public CloseSoftCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<CloseSoftCheckPojo, CloseSoftCheckPojoResult> apply(Command<CloseSoftCheckPojo> command) {
        CloseSoftCheckPojo csc = command.formForSend();
        try {
            Response<CloseSoftCheckPojoResult> response = networkService
                    .getLightSearchAPI()
                    .closeSoftCheck(csc.getToken(), csc)
                    .execute();
            if(response.isSuccessful()) {
                CloseSoftCheckPojoResult csr = response.body() == null ?
                        new CloseSoftCheckPojoResult()
                        : response.body();
                return new CloseSoftCheckCommandResult(csr);
            } else {
                String json = response.errorBody() == null ?
                        "{" +
                            "\"message\":\"Неизвестная ошибка. Попробуйте выполнить запрос позже.\"," +
                            "\"code\":\"502\"" +
                        "}"
                        : response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);

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

    private CommandResult<CloseSoftCheckPojo, CloseSoftCheckPojoResult> errorResult(String message) {
        CloseSoftCheckPojoResult srp = errorPojo(message);
        return new CloseSoftCheckCommandResult(srp);
    }

    private CommandResult<CloseSoftCheckPojo, CloseSoftCheckPojoResult> errorResult(
            Command<CloseSoftCheckPojo> lastCommand, String message) {
        CloseSoftCheckPojoResult csc = errorPojo(message);
        return new CloseSoftCheckCommandResult(lastCommand, csc);
    }

    private CloseSoftCheckPojoResult errorPojo(String message) {
        CloseSoftCheckPojoResult csr = new CloseSoftCheckPojoResult();
        csr.setIsDone(false);
        csr.setMessage(message);
        return csr;
    }
}
