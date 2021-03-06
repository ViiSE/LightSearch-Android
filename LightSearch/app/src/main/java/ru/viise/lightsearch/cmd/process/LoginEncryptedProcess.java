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

import retrofit2.Response;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.LoginEncryptedCommandResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.LoginEncryptedPojo;
import ru.viise.lightsearch.data.pojo.LoginPojoResult;

public class LoginEncryptedProcess implements Process<LoginEncryptedPojo, LoginPojoResult> {

    private final NetworkService networkService;

    public LoginEncryptedProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<LoginEncryptedPojo, LoginPojoResult> apply(Command<LoginEncryptedPojo> command) {
        try {
            Response<LoginPojoResult> response = networkService
                    .getLightSearchAPI()
                    .loginEncrypted(command.formForSend())
                    .execute();
            if(response.isSuccessful()) {
                LoginPojoResult lrp = response.body();
                return new LoginEncryptedCommandResult(lrp);
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

                    return errorResult(ePojo.getMessage());
                } catch (JsonParseException ex) {
                    return errorResult("Неизвестная ошибка. Попробуйте выполнить запрос позже.");
                }
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

    private CommandResult<LoginEncryptedPojo, LoginPojoResult> errorResult(String message) {
        LoginPojoResult lrp = new LoginPojoResult();
        lrp.setIsDone(false);
        lrp.setMessage(message);
        return new LoginEncryptedCommandResult(lrp);
    }
}
