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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultTKListCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.TKListPojo;
import ru.viise.lightsearch.data.pojo.TKListResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class TKListProcess implements Process<TKListPojo> {

    private final NetworkService networkService;

    public TKListProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<TKListPojo> command) {
        TKListPojo tp = command.formForSend();
        try {
            Response<TKListResultPojo> response = networkService
                    .getLightSearchAPI()
                    .TKList(tp.getToken())
                    .execute();
            if(response.isSuccessful()) {
                TKListResultPojo tlp = response.body();
                return result(tlp);
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
        TKListResultPojo trp = new TKListResultPojo();
        trp.setIsDone(false);
        trp.setMessage(message);
        trp.setTKList(new ArrayList<>());
        return result(trp);
    }

    private CommandResult result(TKListResultPojo trp) {
        return new CommandResultTKListCreatorV2Impl(trp).create();
    }
}
