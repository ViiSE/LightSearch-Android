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

package ru.viise.lightsearch.activity.result.processor;

import java.util.function.Function;

import ru.viise.lightsearch.cmd.result.CommandResult;

public class ResultProcessorUIDefaultImpl implements ResultProcessorUI {

    private final Function<CommandResult, Void> processor;

    public ResultProcessorUIDefaultImpl(Function<CommandResult, Void> processor) {
        this.processor = processor;
    }

    @Override
    public Void apply(CommandResult cmdRes) {
        return processor.apply(cmdRes);
    }
}
