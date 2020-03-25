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

import java.io.IOException;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.manager.NetworkService;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultCheckAuthCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.CheckAuthPojo;
import ru.viise.lightsearch.data.pojo.CheckAuthResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class CheckAuthProcess implements Process<CheckAuthPojo> {

    private final NetworkService networkService;

    public CheckAuthProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<CheckAuthPojo> command) {
        CheckAuthPojo sp = command.formForSend();
        try {
            Response<CheckAuthResultPojo> response = networkService
                    .getLightSearchAPI()
                    .checkAuth(sp.getToken())
                    .execute();
            if(response.isSuccessful()) {
                CheckAuthResultPojo carp = response.body();
                return result(carp);
            } else {
                return errorResult();
            }
        } catch (IOException ex) {
            return errorResult();
        }
    }

    private CommandResult errorResult() {
        CheckAuthResultPojo carp = new CheckAuthResultPojo();
        carp.setOk(false);
        return result(carp);
    }

    private CommandResult result(CheckAuthResultPojo carp) {
        return new CommandResultCheckAuthCreatorV2Impl(carp).create();
    }
}
