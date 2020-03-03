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
import ru.viise.lightsearch.cmd.result.creator.v2.CommandResultSearchCreatorV2Impl;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.SearchPojo;
import ru.viise.lightsearch.data.pojo.SearchResultPojo;
import ru.viise.lightsearch.data.v2.Command;

public class SearchProcess implements Process<SearchPojo> {

    private final NetworkService networkService;

    public SearchProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult apply(Command<SearchPojo> command) {
        SearchPojo sp = command.formForSend();
        try {
            Response<SearchResultPojo> response = networkService
                    .getLightSearchAPI()
                    .searchProduct(sp.getToken(), sp.getBarcode(), sp.getSklad(), sp.getTK())
                    .execute();
            if(response.isSuccessful()) {
                SearchResultPojo srp = response.body();
                srp.setSubdivision(sp.getSubdivision());
                return new CommandResultSearchCreatorV2Impl(srp).create();
            } else {
                String json = response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);
                return errorResult(
                        ePojo.getMessage(),
                        sp.getSubdivision());
            }
        } catch (IOException ex) {
            return errorResult(
                    ex.getMessage(),
                    sp.getSubdivision());
        }

    }

    private CommandResult errorResult(String message, String subdivision) {
        SearchResultPojo srp = new SearchResultPojo();
        srp.setIsDone(false);
        srp.setMessage(message);
        srp.setSubdivision(subdivision);
        return new CommandResultSearchCreatorV2Impl(srp).create();
    }
}