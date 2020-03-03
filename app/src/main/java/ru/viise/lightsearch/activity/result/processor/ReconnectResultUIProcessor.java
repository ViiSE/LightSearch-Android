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

import ru.viise.lightsearch.activity.ManagerActivity;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.ReconnectCommandResult;

public class ReconnectResultUIProcessor implements Function<CommandResult, Void> {

    private final ManagerActivity activity;

    public ReconnectResultUIProcessor(ManagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public Void apply(CommandResult commandResult) {
        ReconnectCommandResult recCmdRes = (ReconnectCommandResult)commandResult;
        if(!recCmdRes.isDone())
            activity.callDialogError(recCmdRes.message());
        else {
            // FIXME: 22.02.20 DO IT LATER
//            AlertDialog queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(activity, R.string.spots_dialog_query_exec)
//                    .create();
//            CommandManagerAsyncTaskDTO cmdManagerATDTO =
//                    CommandManagerAsyncTaskDTOInit.commandManagerAsyncTaskDTO(activity.commandManager(),
//                            recCmdRes.reconnectDTO().lastCommandType(), recCmdRes.reconnectDTO().lastCommand());
//            CommandManagerAsyncTask cmdManagerAT = new CommandManagerAsyncTask(activity, queryDialog);
//            cmdManagerAT.execute(cmdManagerATDTO);
        }

        return null;
    }
}

