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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ru.viise.lightsearch.activity.ManagerActivity;
import ru.viise.lightsearch.activity.result.processor.AuthorizationResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.BindResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.CancelSoftCheckResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.CloseSoftCheckResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.ConfirmCartProductsResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.ConfirmSoftCheckProductsResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.OpenSoftCheckResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.ReconnectResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.SearchResultSoftCheckUIProcessor;
import ru.viise.lightsearch.activity.result.processor.SearchResultUIProcessor;
import ru.viise.lightsearch.activity.result.processor.UnbindResultUIProcessor;
import ru.viise.lightsearch.cmd.CommandTypeEnum;
import ru.viise.lightsearch.cmd.result.CommandResult;

public class ResultCommandUICreatorDefaultImpl implements ResultCommandUICreator {

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

    private final ManagerActivity activity;

    public ResultCommandUICreatorDefaultImpl(ManagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public ResultCommandHolderUI createResultCommandHolderUI() {
        Map<CommandTypeEnum, Function<CommandResult, Void>> cmdHolder = new HashMap<>();
        cmdHolder.put(AUTHORIZATION, new AuthorizationResultUIProcessor(activity));
        cmdHolder.put(SEARCH, new SearchResultUIProcessor(activity));
        cmdHolder.put(SEARCH_SC, new SearchResultSoftCheckUIProcessor(activity));
        cmdHolder.put(OPEN_SOFT_CHECK, new OpenSoftCheckResultUIProcessor(activity));
        cmdHolder.put(CANCEL_SOFT_CHECK, new CancelSoftCheckResultUIProcessor(activity));
        cmdHolder.put(CONFIRM_SOFT_CHECK_PRODUCTS, new ConfirmSoftCheckProductsResultUIProcessor(activity));
        cmdHolder.put(CONFIRM_CART_PRODUCTS, new ConfirmCartProductsResultUIProcessor(activity));
        cmdHolder.put(CLOSE_SOFT_CHECK, new CloseSoftCheckResultUIProcessor(activity));
        cmdHolder.put(RECONNECT, new ReconnectResultUIProcessor(activity));
        //----------------------------------------------------------------------------------------//
        cmdHolder.put(BIND, new BindResultUIProcessor(activity));
        cmdHolder.put(UNBIND, new UnbindResultUIProcessor(activity));

        return ResultCommandHolderUIInit.resultCommandHolderUI(cmdHolder);
    }
}
