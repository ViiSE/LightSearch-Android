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
import ru.viise.lightsearch.cmd.result.BindCommandResult;
import ru.viise.lightsearch.cmd.result.CommandResult;

public class BindResultUIProcessor implements Function<CommandResult, Void> {

    private final ManagerActivity activity;

    public BindResultUIProcessor(ManagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public Void apply(CommandResult commandResult) {
        BindCommandResult bindCmdRes = (BindCommandResult) commandResult;
        if(bindCmdRes.isDone()) {
            if(bindCmdRes.selected() == 0) { // check binding
                 if (bindCmdRes.records().size() != 0) {
                     if (bindCmdRes.records().size() == 1)
                         activity.callBindCheckDialogOneResult(bindCmdRes.records().get(0));
                     else {
                         // TODO: 30.01.20 HARDCODE
                         String title = activity.getString(R.string.fragment_result_bind);
                         activity.doResultBindFragmentTransaction(title, bindCmdRes);
                     }
                 } else
                     activity.callBindCheckDialogNoResult();
            } else if(bindCmdRes.selected() == 1) { // binding
                if (bindCmdRes.records().size() != 0) {
                    if (bindCmdRes.records().size() == 1)
                        activity.callBindDialogOneResult(bindCmdRes.records().get(0), bindCmdRes.factoryBarcode());
                    else {
                        // TODO: 30.01.20 HARDCODE
                        String title = "Привязка к №" + bindCmdRes.factoryBarcode();
                        activity.doResultBindFragmentTransaction(title, bindCmdRes);
                    }
                } else
                    activity.callDialogNoResult();
            } else if(bindCmdRes.selected() == 2) { //binding done
                // TODO: 30.01.20 HARDCODE
                if(activity.getBindingContainerFragment() == null)
                    activity.doBindingContainerFragmentTransactionFromResultBind();

                activity.getBindingContainerFragment().switchToCheckBind();

                activity.callDialogSuccess(bindCmdRes.message());
            }
        } else if(bindCmdRes.isReconnect()) {
            // FIXME: 22.02.20 DO IT LATER
//            SharedPreferences sPref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
//            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
//            String ip = prefManager.load(PreferencesManagerType.HOST_MANAGER);
//            String port = prefManager.load(PreferencesManagerType.PORT_MANAGER);
//            ConnectionDTO connDTO = ConnectionDTOInit.connectionDTO(ip, port);
//            activity.reconnect(connDTO, bindCmdRes.reconnectDTO());
        } else
            activity.callDialogError(bindCmdRes.message());

        return null;
    }
}
