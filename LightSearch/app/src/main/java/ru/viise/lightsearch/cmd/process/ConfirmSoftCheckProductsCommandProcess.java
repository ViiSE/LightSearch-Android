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
import ru.viise.lightsearch.data.entity.ConfirmSoftCheckProductsCommandResult;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojo;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojoRawResult;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojoResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;

public class ConfirmSoftCheckProductsCommandProcess implements Process<ConfirmSoftCheckProductsPojo, ConfirmSoftCheckProductsPojoResult> {

    private final NetworkService networkService;

    public ConfirmSoftCheckProductsCommandProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<ConfirmSoftCheckProductsPojo, ConfirmSoftCheckProductsPojoResult> apply(Command<ConfirmSoftCheckProductsPojo> command) {
        ConfirmSoftCheckProductsPojo cscpp = command.formForSend();
        try {
            Response<ConfirmSoftCheckProductsPojoRawResult> response = networkService
                    .getLightSearchAPI()
                    .confirmSoftCheckProducts(cscpp.getToken(), cscpp)
                    .execute();
            if(response.isSuccessful()) {
                ConfirmSoftCheckProductsPojoRawResult csc = response.body() == null ?
                        new ConfirmSoftCheckProductsPojoRawResult()
                        : response.body();
                csc.setSoftCheckRecords(cscpp.getSoftCheckRecords());
                return new ConfirmSoftCheckProductsCommandResult(csc);
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

    private CommandResult<ConfirmSoftCheckProductsPojo, ConfirmSoftCheckProductsPojoResult> errorResult(String message) {
        ConfirmSoftCheckProductsPojoRawResult srp = errorPojo(message);
        return new ConfirmSoftCheckProductsCommandResult(srp);
    }

    private CommandResult<ConfirmSoftCheckProductsPojo, ConfirmSoftCheckProductsPojoResult> errorResult(
            Command<ConfirmSoftCheckProductsPojo> lastCommand, String message) {
        ConfirmSoftCheckProductsPojoRawResult csc = errorPojo(message);
        return new ConfirmSoftCheckProductsCommandResult(lastCommand, csc);
    }

    private ConfirmSoftCheckProductsPojoRawResult errorPojo(String message) {
        ConfirmSoftCheckProductsPojoRawResult csc = new ConfirmSoftCheckProductsPojoRawResult();
        csc.setIsDone(false);
        csc.setMessage(message);
        return csc;
    }
}
