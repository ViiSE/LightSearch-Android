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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultCancelSoftCheckCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckResultPojo;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.v2.Command;

public class CancelSoftCheckProcess implements Process<CancelSoftCheckPojo> {

    private final NetworkService networkService;

    public CancelSoftCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<CancelSoftCheckPojo> command) {
        CancelSoftCheckPojo csc = command.formForSend();
        try {
            Response<CancelSoftCheckResultPojo> response = networkService
                    .getLightSearchAPI()
                    .cancelSoftCheck(csc.getToken(), csc)
                    .execute();
            if(response.isSuccessful()) {
                CancelSoftCheckResultPojo cscr = response.body();
                cscr.setIsCart(csc.getIsCart());
                return new CommandResultCancelSoftCheckCreatorV2Impl(cscr).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(
                        ePojo.getMessage(),
                        csc.getIsCart());
            }
        } catch (IOException ex) {
            String message = ex.getMessage();
            if(ex.getMessage().equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(ex.getMessage().equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(
                    message,
                    csc.getIsCart());
        }

    }

    private CommandResult errorResult(String message, boolean isCart) {
        CancelSoftCheckResultPojo cscr = new CancelSoftCheckResultPojo();
        cscr.setIsDone(false);
        cscr.setMessage(message);
        cscr.setIsCart(isCart);
        return new CommandResultCancelSoftCheckCreatorV2Impl(cscr).create();
    }
}
