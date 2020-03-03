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
import ru.viise.lightsearch.data.pojo.BindPojo;
import ru.viise.lightsearch.data.pojo.BindResultPojo;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.v2.Command;

public class BindProcess implements Process<BindPojo> {

    private final NetworkService networkService;

    public BindProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<BindPojo> command) {
        BindPojo bp = command.formForSend();
        try {
            Response<BindResultPojo> response = networkService
                    .getLightSearchAPI()
                    .bindProduct(bp.getToken(), bp)
                    .execute();
            if(response.isSuccessful()) {
                BindResultPojo brp = response.body();
                brp.setSelected(bp.getSelected());
                brp.setFactoryBarcode(bp.getFactoryBarcode());
                return new CommandResultBindCreatorV2Impl(brp).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(
                        ePojo.getMessage(),
                        bp.getSelected(),
                        bp.getFactoryBarcode());
            }
        } catch (IOException ex) {
            return errorResult(
                    ex.getMessage(),
                    bp.getSelected(),
                    bp.getFactoryBarcode());
        }

    }

    private CommandResult errorResult(String message, int selected, String factoryBarcode) {
        BindResultPojo brp = new BindResultPojo();
        brp.setIsDone(false);
        brp.setMessage(message);
        brp.setSelected(selected);
        brp.setFactoryBarcode(factoryBarcode);
        return new CommandResultBindCreatorV2Impl(brp).create();
    }
}
