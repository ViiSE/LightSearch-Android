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

package ru.viise.lightsearch.cmd.network.task;

import android.app.AlertDialog;
import android.os.AsyncTask;

import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.cmd.process.Processes;
import ru.viise.lightsearch.cmd.process.ProcessesImpl;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.SendForm;

public class NetworkAsyncTask<C extends SendForm, R extends SendForm> extends AsyncTask<Command<C>, Void, CommandResult<C, R>> {

    private final NetworkCallback<C, R> networkCallback;
    private final AlertDialog spotsDialog;

    public NetworkAsyncTask(NetworkCallback<C, R> networkCallback, AlertDialog spotsDialog) {
        this.networkCallback = networkCallback;
        this.spotsDialog = spotsDialog;
    }

    public NetworkAsyncTask(NetworkCallback<C, R> networkCallback) {
        this.networkCallback = networkCallback;
        this.spotsDialog = null;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    @Override
    protected final CommandResult<C, R> doInBackground(Command<C>... commands) {
        Processes processes = new ProcessesImpl(NetworkService.getInstance());
        Command<C> command = commands[0];
        return processes.process(commands[0].name()).apply((Command)command);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(spotsDialog != null)
            spotsDialog.show();
    }

    @Override
    protected void onPostExecute(CommandResult<C, R> cmdRes) {
        super.onPostExecute(cmdRes);
        if(spotsDialog != null)
            spotsDialog.dismiss();
        networkCallback.handleResult(cmdRes);
    }
}
