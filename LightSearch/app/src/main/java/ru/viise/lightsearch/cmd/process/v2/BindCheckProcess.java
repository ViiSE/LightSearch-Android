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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultBindCheckCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckResultPojo;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.v2.Command;

public class BindCheckProcess implements Process<BindCheckPojo> {

    private final NetworkService networkService;

    public BindCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<BindCheckPojo> command) {
        BindCheckPojo bcp = command.formForSend();

        try {
            Response<BindCheckResultPojo> response = networkService
                    .getLightSearchAPI()
                    .bindCheckProduct(bcp.getToken(), bcp)
                    .execute();
            if(response.isSuccessful()) {
                BindCheckResultPojo bcrp = response.body();
                bcrp.setFactoryBarcode(bcp.getFactoryBarcode());
                bcrp.setSelected(bcp.getSelected());
                return new CommandResultBindCheckCreatorV2Impl(bcrp).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(
                        ePojo.getMessage(),
                        bcp.getSelected(),
                        bcp.getFactoryBarcode());
            }
        } catch (IOException ex) {
            String message = ex.getMessage();
            if(ex.getMessage().equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(ex.getMessage().equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(
                    message,
                    bcp.getSelected(),
                    bcp.getFactoryBarcode());
        }

    }

    private CommandResult errorResult(String message, int selected, String factoryBarcode) {
        BindCheckResultPojo bcrp = new BindCheckResultPojo();
        bcrp.setIsDone(false);
        bcrp.setMessage(message);
        bcrp.setSelected(selected);
        bcrp.setFactoryBarcode(factoryBarcode);
        return new CommandResultBindCheckCreatorV2Impl(bcrp).create();
    }
}
