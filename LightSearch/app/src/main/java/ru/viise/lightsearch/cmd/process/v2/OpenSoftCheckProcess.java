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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultOpenSoftCheckCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class OpenSoftCheckProcess implements Process<OpenSoftCheckPojo> {

    private final NetworkService networkService;

    public OpenSoftCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<OpenSoftCheckPojo> command) {
        OpenSoftCheckPojo osc = command.formForSend();

        try {
            Response<OpenSoftCheckResultPojo> response = networkService
                    .getLightSearchAPI()
                    .openSoftCheck(osc.getToken(), osc)
                    .execute();
            if(response.isSuccessful()) {
                OpenSoftCheckResultPojo oscr = response.body();
                return new CommandResultOpenSoftCheckCreatorV2Impl(oscr, false).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                if(ePojo.getMessage().contains("уже открыт"))
                    return errorResult(ePojo.getMessage(),true);
                else
                    return errorResult(ePojo.getMessage(),false);
            }
        } catch (IOException ex) {
            String message = ex.getMessage();
            if(ex.getMessage().equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(ex.getMessage().equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(message, false);
        }
    }

    private CommandResult errorResult(String message, boolean isCancel) {
        OpenSoftCheckResultPojo oscr = new OpenSoftCheckResultPojo();
        oscr.setIsDone(false);
        oscr.setMessage(message);
        return new CommandResultOpenSoftCheckCreatorV2Impl(oscr, isCancel).create();
    }
}
