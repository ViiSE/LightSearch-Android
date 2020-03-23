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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultSearchSoftCheckCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.SearchResultPojo;
import ru.viise.lightsearch.data.pojo.SearchSoftCheckPojo;
import ru.viise.lightsearch.data.v2.Command;

public class SearchSoftCheckProcess implements Process<SearchSoftCheckPojo> {

    private final NetworkService networkService;

    public SearchSoftCheckProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<SearchSoftCheckPojo> command) {
        SearchSoftCheckPojo sp = command.formForSend();
        try {
            Response<SearchResultPojo> response = networkService
                    .getLightSearchAPI()
                    .searchSoftCheckProducts(sp.getToken(), sp.getBarcode(), sp.getUsername())
                    .execute();
            if(response.isSuccessful()) {
                SearchResultPojo srp = response.body();
                srp.setSubdivision("all");
                return result(srp);
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(
                        ePojo.getMessage(),
                        "all");
            }
        } catch (IOException ex) {
            String message = ex.getMessage();
            if(ex.getMessage().equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(ex.getMessage().equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(
                    message,
                    "all");
        }

    }

    private CommandResult errorResult(String message, String subdivision) {
        SearchResultPojo srp = new SearchResultPojo();
        srp.setIsDone(false);
        srp.setMessage(message);
        srp.setSubdivision(subdivision);
        return result(srp);
    }

    private CommandResult result(SearchResultPojo srp) {
        return new CommandResultSearchSoftCheckCreatorV2Impl(srp).create();
    }
}
