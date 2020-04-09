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

import com.google.gson.Gson;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Response;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.SearchCommandResult;
import ru.viise.lightsearch.data.pojo.ErrorPojo;
import ru.viise.lightsearch.data.pojo.SearchPojo;
import ru.viise.lightsearch.data.pojo.SearchPojoRawResult;
import ru.viise.lightsearch.data.pojo.SearchPojoResult;

public class SearchProcess implements Process<SearchPojo, SearchPojoResult> {

    private final NetworkService networkService;

    public SearchProcess(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public CommandResult<SearchPojo, SearchPojoResult> apply(Command<SearchPojo> command) {
        SearchPojo sp = command.formForSend();
        try {
            Response<SearchPojoRawResult> response = networkService
                    .getLightSearchAPI()
                    .searchProduct(sp.getToken(), sp.getBarcode(), sp.getSklad(), sp.getTK())
                    .execute();
            if(response.isSuccessful()) {
                SearchPojoRawResult srp = response.body() == null ? new SearchPojoRawResult() : response.body();
                srp.setSubdivision(sp.getSubdivision());
                return new SearchCommandResult(srp);
            } else {
                String json = response.errorBody() == null ?
                        "{" +
                            "\"message\":\"Неизвестная ошибка. Попробуйте выполнить запрос позже.\"," +
                            "\"code\":\"502\"" +
                        "}"
                        : response.errorBody().string();
                ErrorPojo ePojo = new Gson().fromJson(json, ErrorPojo.class);

                System.out.println(response.code());

                if(response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    return errorResult(command, "");
                } else if(ePojo.getCode().equals(String.valueOf(HttpsURLConnection.HTTP_UNAUTHORIZED)))
                    return errorResult(command, ePojo.getMessage());
                else
                    return errorResult(ePojo.getMessage());
            }
        } catch (IOException ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if(message.equals("timeout"))
                message = "Не удалось установить связь с сервером.";
            else if(message.contains("timed out"))
                message = "Не удалось установить связь с сервером.";
            else if(message.equals(""))
                message = "Неизвестная ошибка. Попробуйте выполнить запрос позже.";

            return errorResult(message);
        }
    }

    private CommandResult<SearchPojo, SearchPojoResult> errorResult(String message) {
        SearchPojoRawResult srp = errorPojo(message);
        return new SearchCommandResult(srp);
    }

    private CommandResult<SearchPojo, SearchPojoResult> errorResult(Command<SearchPojo> lastCommand, String message) {
        SearchPojoRawResult srp = errorPojo(message);
        return new SearchCommandResult(lastCommand, srp);
    }

    private SearchPojoRawResult errorPojo(String message) {
        SearchPojoRawResult srp = new SearchPojoRawResult();
        srp.setIsDone(false);
        srp.setMessage(message);
        return srp;
    }
}
