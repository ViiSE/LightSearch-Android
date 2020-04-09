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

package ru.viise.lightsearch.data.entity;

import ru.viise.lightsearch.data.pojo.CheckAuthPojo;
import ru.viise.lightsearch.data.pojo.CheckAuthPojoResult;

public class CheckAuthCommandResult implements CommandResult<CheckAuthPojo, CheckAuthPojoResult> {

    private final CheckAuthPojoResult resultPojo;

    public CheckAuthCommandResult(CheckAuthPojoResult resultPojo) {
        this.resultPojo = resultPojo;
    }

    @Override
    public boolean isDone() {
        return resultPojo.isOk();
    }

    @Override
    public CheckAuthPojoResult data() {
        return resultPojo;
    }

    @Override
    public Command<CheckAuthPojo> lastCommand() {
        return null;
    }
}
