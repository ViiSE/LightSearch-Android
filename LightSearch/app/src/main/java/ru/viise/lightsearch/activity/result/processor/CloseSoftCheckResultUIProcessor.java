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

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivity;
import ru.viise.lightsearch.cmd.result.CloseSoftCheckCommandResult;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.fragment.ISoftCheckContainerFragment;

public class CloseSoftCheckResultUIProcessor implements Function<CommandResult, Void> {

    private final ManagerActivity activity;

    public CloseSoftCheckResultUIProcessor(ManagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public Void apply(CommandResult commandResult) {
        CloseSoftCheckCommandResult closeSCCmdRes = (CloseSoftCheckCommandResult)commandResult;
        if(closeSCCmdRes.isDone()) {
            activity.callDialogSuccess(closeSCCmdRes.message());
            activity.setTitle(activity.getString(R.string.fragment_container));
            ISoftCheckContainerFragment containerFragment = activity.getSoftCheckContainerFragment();
            if(containerFragment != null)
                containerFragment.switchToOpenSoftCheckFragment();
        } else if(closeSCCmdRes.isReconnect()) {
            // FIXME: 22.02.20 DO IT LATER
//            SharedPreferences sPref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
//            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
//            String ip = prefManager.load(PreferencesManagerType.HOST_MANAGER);
//            String port = prefManager.load(PreferencesManagerType.PORT_MANAGER);
//            ConnectionDTO connDTO = ConnectionDTOInit.connectionDTO(ip, port);
//            activity.reconnect(connDTO, closeSCCmdRes.reconnectDTO());
        } else
            activity.callDialogError(closeSCCmdRes.message());

        return null;
    }
}
