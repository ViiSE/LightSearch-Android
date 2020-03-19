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

package ru.viise.lightsearch.cmd.process.v2;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.manager.NetworkService;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultAuthorizationCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.LoginPojo;
import ru.viise.lightsearch.data.pojo.LoginResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class AuthorizationProcess implements Process<LoginPojo> {

    private final NetworkService networkService;

    public AuthorizationProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<LoginPojo> command) {
        try {
            Response<LoginResultPojo> response = networkService
                    .getLightSearchAPI()
                    .login(command.formForSend())
                    .execute();
            if(response.isSuccessful()) {
                LoginResultPojo lrp = response.body();
                return new CommandResultAuthorizationCreatorV2Impl(lrp).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);

                return errorResult(ePojo.getMessage());
            }
        } catch (IOException ex) {
            if(ex.getMessage().equals("timeout"))
                return errorResult("Не удалось установить связь с сервером.");

            return errorResult(ex.getMessage());
        }

    }

    private CommandResult errorResult(String message) {
        LoginResultPojo lrp = new LoginResultPojo();
        lrp.setIsDone(false);
        lrp.setMessage(message);
        return new CommandResultAuthorizationCreatorV2Impl(lrp).create();
    }
}
