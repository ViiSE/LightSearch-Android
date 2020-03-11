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

package ru.viise.lightsearch.cmd.manager.task.v2;

import android.app.AlertDialog;
import android.os.AsyncTask;

import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.cmd.holder.v2.Processes;
import ru.viise.lightsearch.cmd.holder.v2.ProcessesDefaultImpl;
import ru.viise.lightsearch.cmd.manager.NetworkService;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.data.pojo.SendForm;
import ru.viise.lightsearch.data.v2.Command;

public class NetworkAsyncTask<T extends SendForm> extends AsyncTask<Command<T>, Void, CommandResult> {

    private final ManagerActivityHandler managerActivityHandler;
    private final AlertDialog spotsDialog;

    public NetworkAsyncTask(ManagerActivityHandler managerActivityHandler, AlertDialog spotsDialog) {
        this.managerActivityHandler = managerActivityHandler;
        this.spotsDialog = spotsDialog;
    }


    @SuppressWarnings("unchecked")
    @SafeVarargs
    @Override
    protected final CommandResult doInBackground(Command<T>... commands) {
        Processes processes = new ProcessesDefaultImpl(NetworkService.getInstance());
        Command<T> command = commands[0];
        return processes.process(commands[0].name()).apply((Command) command);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        spotsDialog.show();
    }

    @Override
    protected void onPostExecute(CommandResult cmdRes) {
        super.onPostExecute(cmdRes);
        spotsDialog.dismiss();
        managerActivityHandler.handleResult(cmdRes);
    }
}
