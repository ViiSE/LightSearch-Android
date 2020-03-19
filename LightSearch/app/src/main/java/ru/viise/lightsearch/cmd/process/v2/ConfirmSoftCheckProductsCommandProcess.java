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
import java.util.List;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.manager.NetworkService;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultConfirmCartProductsCreatorV2Impl;
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultConfirmSoftCheckProductsCreatorV2Impl;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojo;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsResultPojo;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.v2.Command;

public class ConfirmSoftCheckProductsCommandProcess implements Process<ConfirmSoftCheckProductsPojo> {

    private final NetworkService networkService;

    public ConfirmSoftCheckProductsCommandProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<ConfirmSoftCheckProductsPojo> command) {
        ConfirmSoftCheckProductsPojo cscpp = command.formForSend();

        try {
            Response<ConfirmSoftCheckProductsResultPojo> response = networkService
                    .getLightSearchAPI()
                    .confirmSoftCheckProducts(cscpp.getToken(), cscpp)
                    .execute();
            if(response.isSuccessful()) {
                ConfirmSoftCheckProductsResultPojo cscprp = response.body();
                cscprp.setSoftCheckRecords(cscpp.getSoftCheckRecords());
                if(cscpp.getType() == 1) {
                    return new CommandResultConfirmCartProductsCreatorV2Impl(cscprp).create();
                } else {
                    return new CommandResultConfirmSoftCheckProductsCreatorV2Impl(cscprp).create();
                }
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(
                        ePojo.getMessage(),
                        cscpp.getSoftCheckRecords(),
                        cscpp.getType());
            }
        } catch (IOException ex) {
            String message = ex.getMessage();
            if(ex.getMessage().equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(ex.getMessage().equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(
                    message,
                    cscpp.getSoftCheckRecords(),
                    cscpp.getType());
        }

    }

    private CommandResult errorResult(String message, List<SoftCheckRecord> records, int type) {
        ConfirmSoftCheckProductsResultPojo cscprp = new ConfirmSoftCheckProductsResultPojo();
        cscprp.setIsDone(false);
        cscprp.setMessage(message);
        cscprp.setSoftCheckRecords(records);
        if(type == 1) {
            return new CommandResultConfirmCartProductsCreatorV2Impl(cscprp).create();
        } else {
            return new CommandResultConfirmSoftCheckProductsCreatorV2Impl(cscprp).create();
        }
    }
}
