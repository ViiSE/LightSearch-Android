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

package ru.viise.lightsearch.activity.result.holder;

import java.util.Map;
import java.util.function.Function;

import ru.viise.lightsearch.activity.result.processor.ResultProcessorUI;
import ru.viise.lightsearch.activity.result.processor.ResultProcessorUIInit;
import ru.viise.lightsearch.cmd.CommandTypeEnum;
import ru.viise.lightsearch.cmd.result.AuthorizationCommandResult;
import ru.viise.lightsearch.cmd.result.BindCommandResult;
import ru.viise.lightsearch.cmd.result.CancelSoftCheckCommandResult;
import ru.viise.lightsearch.cmd.result.CloseSoftCheckCommandResult;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.ConfirmCartProductsResult;
import ru.viise.lightsearch.cmd.result.ConfirmSoftCheckProductsResult;
import ru.viise.lightsearch.cmd.result.OpenSoftCheckCommandResult;
import ru.viise.lightsearch.cmd.result.ReconnectCommandResult;
import ru.viise.lightsearch.cmd.result.SearchCommandResult;
import ru.viise.lightsearch.cmd.result.SearchSoftCheckCommandResult;
import ru.viise.lightsearch.cmd.result.UnbindCommandResult;

public class ResultCommandHolderUIDefaultImpl implements ResultCommandHolderUI {

    private final CommandTypeEnum AUTHORIZATION               = CommandTypeEnum.AUTHORIZATION;
    private final CommandTypeEnum SEARCH                      = CommandTypeEnum.SEARCH;
    private final CommandTypeEnum SEARCH_SC                   = CommandTypeEnum.SEARCH_SC;
    private final CommandTypeEnum OPEN_SOFT_CHECK             = CommandTypeEnum.OPEN_SOFT_CHECK;
    private final CommandTypeEnum CANCEL_SOFT_CHECK           = CommandTypeEnum.CANCEL_SOFT_CHECK;
    private final CommandTypeEnum CONFIRM_SOFT_CHECK_PRODUCTS = CommandTypeEnum.CONFIRM_SOFT_CHECK_PRODUCTS;
    private final CommandTypeEnum CONFIRM_CART_PRODUCTS       = CommandTypeEnum.CONFIRM_CART_PRODUCTS;
    private final CommandTypeEnum CLOSE_SOFT_CHECK            = CommandTypeEnum.CLOSE_SOFT_CHECK;
    private final CommandTypeEnum RECONNECT                   = CommandTypeEnum.RECONNECT;

    private final CommandTypeEnum BIND                        = CommandTypeEnum.BIND;
    private final CommandTypeEnum UNBIND                      = CommandTypeEnum.UNBIND;

    private final Map<CommandTypeEnum, Function<CommandResult, Void>> commandHolderUI;

    public ResultCommandHolderUIDefaultImpl(Map<CommandTypeEnum, Function<CommandResult, Void>> commandHolderUI) {
        this.commandHolderUI = commandHolderUI;
    }

    @Override
    public ResultProcessorUI get(CommandResult cmdRes) {
        if(cmdRes instanceof AuthorizationCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(AUTHORIZATION));
        else if(cmdRes instanceof SearchCommandResult)
                return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(SEARCH));
        else if(cmdRes instanceof SearchSoftCheckCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(SEARCH_SC));
        else if(cmdRes instanceof OpenSoftCheckCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(OPEN_SOFT_CHECK));
        else if(cmdRes instanceof CancelSoftCheckCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(CANCEL_SOFT_CHECK));
        else if(cmdRes instanceof ConfirmCartProductsResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(CONFIRM_CART_PRODUCTS));
        else if(cmdRes instanceof ConfirmSoftCheckProductsResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(CONFIRM_SOFT_CHECK_PRODUCTS));
        else if(cmdRes instanceof CloseSoftCheckCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(CLOSE_SOFT_CHECK));
        else if(cmdRes instanceof ReconnectCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(RECONNECT));
        //----------------------------------------------------------------------------------------//
        else if(cmdRes instanceof BindCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(BIND));
        else if(cmdRes instanceof UnbindCommandResult)
            return ResultProcessorUIInit.resultProcessorUI(commandHolderUI.get(UNBIND));

        return null;
    }
}
