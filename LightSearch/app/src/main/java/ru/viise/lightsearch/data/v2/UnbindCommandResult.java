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

package ru.viise.lightsearch.data.v2;

import ru.viise.lightsearch.data.pojo.UnbindResultPojo;

public class UnbindCommandResult implements CommandResult<UnbindResultPojo> {

    private final boolean isDone;
    private final UnbindResultPojo resultPojo;

    public UnbindCommandResult(boolean isDone, UnbindResultPojo resultPojo) {
        this.isDone = isDone;
        this.resultPojo = resultPojo;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public UnbindResultPojo data() {
        return resultPojo;
    }
}
