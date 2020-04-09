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

package ru.viise.lightsearch.cmd.process;

import java.io.IOException;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.data.entity.CheckAuthCommandResult;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.CheckAuthPojo;
import ru.viise.lightsearch.data.pojo.CheckAuthPojoResult;

public class CheckAuthProcess implements Process<CheckAuthPojo, CheckAuthPojoResult> {

    private final NetworkService networkService;

    public CheckAuthProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<CheckAuthPojo, CheckAuthPojoResult> apply(Command<CheckAuthPojo> command) {
        CheckAuthPojo sp = command.formForSend();
        try {
            Response<CheckAuthPojoResult> response = networkService
                    .getLightSearchAPI()
                    .checkAuth(sp.getToken())
                    .execute();
            if(response.isSuccessful()) {
                CheckAuthPojoResult carp = response.body();
                return result(carp);
            } else {
                return errorResult();
            }
        } catch (IOException ex) {
            return errorResult();
        }
    }

    private CommandResult<CheckAuthPojo, CheckAuthPojoResult> errorResult() {
        CheckAuthPojoResult carp = new CheckAuthPojoResult();
        carp.setOk(false);
        return result(carp);
    }

    private CommandResult<CheckAuthPojo, CheckAuthPojoResult> result(CheckAuthPojoResult carp) {
        return new CheckAuthCommandResult(carp);
    }
}
