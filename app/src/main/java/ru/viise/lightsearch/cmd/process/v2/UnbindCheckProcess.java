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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultUnbindCheckCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class UnbindCheckProcess implements Process<UnbindCheckPojo> {

    private final NetworkService networkService;

    public UnbindCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<UnbindCheckPojo> command) {
        UnbindCheckPojo ucp = command.formForSend();

        try {
            Response<UnbindCheckResultPojo> response = networkService
                    .getLightSearchAPI()
                    .unbindCheckProduct(ucp.getToken(), ucp)
                    .execute();
            if(response.isSuccessful()) {
                UnbindCheckResultPojo ucrp = response.body();
                return new CommandResultUnbindCheckCreatorV2Impl(ucrp).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(ePojo.getMessage(), ucp.getFactoryBarcode());
            }
        } catch (IOException ex) {
            return errorResult(ex.getMessage(), ucp.getFactoryBarcode());
        }

    }

    private CommandResult errorResult(String message, String factoryBarcode) {
        UnbindCheckResultPojo ucrp = new UnbindCheckResultPojo();
        ucrp.setIsDone(false);
        ucrp.setMessage(message);
        ucrp.setFactoryBarcode(factoryBarcode);
        return new CommandResultUnbindCheckCreatorV2Impl(ucrp).create();
    }
}
