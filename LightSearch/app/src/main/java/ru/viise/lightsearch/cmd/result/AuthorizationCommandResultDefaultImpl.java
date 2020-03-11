/*
 * Copyright 2019 ViiSE.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.lightsearch.cmd.result;

import ru.viise.lightsearch.data.ReconnectDTO;

public class AuthorizationCommandResultDefaultImpl implements AuthorizationCommandResult {

    private final boolean isDone;
    private final String message;
    private final String userIdent;
    private final String token;
    private final String[] skladList;
    private final String[] TKList;

    public AuthorizationCommandResultDefaultImpl(
            boolean isDone,
            String message,
            String userIdent,
            String token,
            String[] skladList,
            String[] TKList) {
        this.isDone = isDone;
        this.message = message;
        this.userIdent = userIdent;
        this.token = token;
        this.skladList = skladList;
        this.TKList = TKList;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean isReconnect() {
        return false;
    }

    @Override
    public ReconnectDTO reconnectDTO() {
        return null;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String userIdent() {
        return userIdent;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public String[] skladList() {
        return skladList;
    }

    @Override
    public String[] TKList() {
        return TKList;
    }
}
