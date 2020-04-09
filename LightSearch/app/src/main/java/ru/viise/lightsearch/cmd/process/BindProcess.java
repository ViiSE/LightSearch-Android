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
import ru.viise.lightsearch.data.entity.BindCommandResult;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.BindPojo;
import ru.viise.lightsearch.data.pojo.BindPojoResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;

public class BindProcess implements Process<BindPojo, BindPojoResult> {

    private final NetworkService networkService;

    public BindProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<BindPojo, BindPojoResult> apply(Command<BindPojo> command) {
        BindPojo bp = command.formForSend();
        try {
            Response<BindPojoResult> response = networkService
                    .getLightSearchAPI()
                    .bindProduct(bp.getToken(), bp)
                    .execute();
            if(response.isSuccessful()) {
                BindPojoResult brp = response.body() == null ? new BindPojoResult() : response.body();
                brp.setSelected(bp.getSelected());
                brp.setFactoryBarcode(bp.getFactoryBarcode());
                return new BindCommandResult(brp);
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
            else if(message.equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(message);
        }
    }

    private CommandResult<BindPojo, BindPojoResult> errorResult(String message) {
        BindPojoResult br = errorPojo(message);
        return new BindCommandResult(br);
    }

    private CommandResult<BindPojo, BindPojoResult> errorResult(Command<BindPojo> lastCommand, String message) {
        BindPojoResult br = errorPojo(message);
        return new BindCommandResult(lastCommand, br);
    }

    private BindPojoResult errorPojo(String message) {
        BindPojoResult br = new BindPojoResult();
        br.setIsDone(false);
        br.setMessage(message);
        return br;
    }
}
