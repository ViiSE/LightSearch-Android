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
import java.util.ArrayList;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.manager.NetworkService;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultSkladListCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.SkladListPojo;
import ru.viise.lightsearch.data.pojo.SkladListResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class SkladListProcess implements Process<SkladListPojo> {

    private final NetworkService networkService;

    public SkladListProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<SkladListPojo> command) {
        SkladListPojo sp = command.formForSend();
        try {
            Response<SkladListResultPojo> response = networkService
                    .getLightSearchAPI()
                    .skladList(sp.getToken())
                    .execute();
            if(response.isSuccessful()) {
                SkladListResultPojo srp = response.body();
                return result(srp);
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(ePojo.getMessage());
            }
        } catch (IOException ex) {
            String message = ex.getMessage();
            if(ex.getMessage().equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(ex.getMessage().equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(message);
        }

    }

    private CommandResult errorResult(String message) {
        SkladListResultPojo srp = new SkladListResultPojo();
        srp.setIsDone(false);
        srp.setMessage(message);
        srp.setSkladList(new ArrayList<>());
        return result(srp);
    }

    private CommandResult result(SkladListResultPojo srp) {
        return new CommandResultSkladListCreatorV2Impl(srp).create();
    }
}
