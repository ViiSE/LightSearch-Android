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

package ru.viise.lightsearch.cmd.process;

import java.util.HashMap;
import java.util.Map;

import ru.viise.lightsearch.cmd.ClientCommands;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.data.pojo.SendForm;

public class ProcessesImpl implements Processes {

    private static final Map<String, Process<? extends SendForm, ? extends SendForm>> processes = new HashMap<>();

    public ProcessesImpl(NetworkService networkService) {
        if(NetworkService.isChange()) {
            fillProcess(networkService);
            NetworkService.setChange(false);
        } else if(processes.isEmpty()) {
            fillProcess(networkService);
        }
    }

    @Override
    public Process<? extends SendForm, ? extends SendForm> process(String name) {
        return processes.get(name);
    }

    private void fillProcess(NetworkService networkService) {
        processes.put(ClientCommands.LOGIN, new LoginProcess(networkService));
        processes.put(ClientCommands.SEARCH, new SearchProcess(networkService));
        processes.put(ClientCommands.OPEN_SOFT_CHECK, new OpenSoftCheckProcess(networkService));
        processes.put(ClientCommands.CANCEL_SOFT_CHECK, new CancelSoftCheckProcess(networkService));
        processes.put(ClientCommands.CLOSE_SOFT_CHECK, new CloseSoftCheckProcess(networkService));
        processes.put(ClientCommands.CONFIRM_SOFT_CHECK_PRODUCTS, new ConfirmSoftCheckProductsCommandProcess(networkService));
        processes.put(ClientCommands.BIND, new BindProcess(networkService));
        processes.put(ClientCommands.BIND_CHECK, new BindCheckProcess(networkService));
        processes.put(ClientCommands.UNBIND, new UnbindProcess(networkService));
        processes.put(ClientCommands.UNBIND_CHECK, new UnbindCheckProcess(networkService));
        processes.put(ClientCommands.SEARCH_SOFT_CHECK, new SearchSoftCheckProcess(networkService));
        processes.put(ClientCommands.SKLAD_LIST, new SkladListProcess(networkService));
        processes.put(ClientCommands.TK_LIST, new TKListProcess(networkService));
        processes.put(ClientCommands.CHECK_AUTH, new CheckAuthProcess(networkService));
        processes.put(ClientCommands.KEY, new KeyProcess(networkService));
        processes.put(ClientCommands.LOGIN_ENCRYPTED, new LoginEncryptedProcess(networkService));
    }
}
