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

import android.content.Context;
import android.content.SharedPreferences;

import java.util.function.Function;

import ru.viise.lightsearch.activity.ManagerActivity;
import ru.viise.lightsearch.cmd.result.AuthorizationCommandResult;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class AuthorizationResultUIProcessor implements Function<CommandResult, Void> {

    private final ManagerActivity activity;

    public AuthorizationResultUIProcessor(ManagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public Void apply(CommandResult commandResult) {
        AuthorizationCommandResult authCmdRes = (AuthorizationCommandResult)commandResult;
        if(authCmdRes.isDone()) {
            String pref = "pref";
            SharedPreferences sPref = activity.getSharedPreferences(pref, Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
            prefManager.save(PreferencesManagerType.USER_IDENT_MANAGER, authCmdRes.userIdent());
            prefManager.save(PreferencesManagerType.TOKEN_MANAGER, authCmdRes.token());
            activity.callDialogSuccess(authCmdRes.message());
            // TODO: 29.01.20 UNCOMMENT LATER
//            activity.doContainerFragmentTransaction(authCmdRes.skladList(), authCmdRes.TKList());
            activity.doBindingContainerFragmentTransaction(authCmdRes.skladList(), authCmdRes.TKList());
        } else
            activity.callDialogError(authCmdRes.message());

        return null;
    }
}
