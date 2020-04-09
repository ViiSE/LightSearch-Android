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
import ru.viise.lightsearch.data.entity.CancelSoftCheckCommandResult;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;

public class CancelSoftCheckProcess implements Process<CancelSoftCheckPojo, CancelSoftCheckPojoResult> {

    private final NetworkService networkService;

    public CancelSoftCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<CancelSoftCheckPojo, CancelSoftCheckPojoResult> apply(Command<CancelSoftCheckPojo> command) {
        CancelSoftCheckPojo csc = command.formForSend();
        try {
            Response<CancelSoftCheckPojoResult> response = networkService
                    .getLightSearchAPI()
                    .cancelSoftCheck(csc.getToken(), csc)
                    .execute();
            if(response.isSuccessful()) {
                CancelSoftCheckPojoResult csr = response.body() == null ?
                        new CancelSoftCheckPojoResult()
                        : response.body();
                csr.setIsCart(csc.getIsCart());
                return new CancelSoftCheckCommandResult(csr);
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

    private CommandResult<CancelSoftCheckPojo, CancelSoftCheckPojoResult> errorResult(String message) {
        CancelSoftCheckPojoResult srp = errorPojo(message);
        return new CancelSoftCheckCommandResult(srp);
    }

    private CommandResult<CancelSoftCheckPojo, CancelSoftCheckPojoResult> errorResult(
            Command<CancelSoftCheckPojo> lastCommand, String message) {
        CancelSoftCheckPojoResult csc = errorPojo(message);
        return new CancelSoftCheckCommandResult(lastCommand, csc);
    }

    private CancelSoftCheckPojoResult errorPojo(String message) {
        CancelSoftCheckPojoResult csr = new CancelSoftCheckPojoResult();
        csr.setIsDone(false);
        csr.setMessage(message);
        return csr;
    }
}
