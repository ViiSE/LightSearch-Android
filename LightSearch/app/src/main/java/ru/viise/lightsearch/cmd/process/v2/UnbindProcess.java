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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultBindCreatorV2Impl;
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultUnbindCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.BindResultPojo;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojo;
import ru.viise.lightsearch.data.pojo.UnbindResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class UnbindProcess implements Process<UnbindPojo> {

    private final NetworkService networkService;

    public UnbindProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<UnbindPojo> command) {
        UnbindPojo up = command.formForSend();
        try {
            Response<UnbindResultPojo> response = networkService
                    .getLightSearchAPI()
                    .unbindProduct(up.getToken(), up)
                    .execute();
            if(response.isSuccessful()) {
                UnbindResultPojo brp = response.body();
                return new CommandResultUnbindCreatorV2Impl(brp).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(ePojo.getMessage());
            }
        } catch (IOException ex) {
            return errorResult(ex.getMessage());
        }

    }

    private CommandResult errorResult(String message) {
        BindResultPojo brp = new BindResultPojo();
        brp.setIsDone(false);
        brp.setMessage(message);
        return new CommandResultBindCreatorV2Impl(brp).create();
    }
}
