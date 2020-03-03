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

package ru.viise.lightsearch.cmd.holder.v2;

import java.util.HashMap;
import java.util.Map;

import ru.viise.lightsearch.cmd.ClientCommands;
import ru.viise.lightsearch.cmd.manager.NetworkService;
import ru.viise.lightsearch.cmd.process.v2.AuthorizationProcess;
import ru.viise.lightsearch.cmd.process.v2.BindCheckProcess;
import ru.viise.lightsearch.cmd.process.v2.BindProcess;
import ru.viise.lightsearch.cmd.process.v2.CancelSoftCheckProcess;
import ru.viise.lightsearch.cmd.process.v2.CloseSoftCheckProcess;
import ru.viise.lightsearch.cmd.process.v2.ConfirmSoftCheckProductsCommandProcess;
import ru.viise.lightsearch.cmd.process.v2.OpenSoftCheckProcess;
import ru.viise.lightsearch.cmd.process.v2.Process;
import ru.viise.lightsearch.cmd.process.v2.SearchProcess;
import ru.viise.lightsearch.cmd.process.v2.UnbindCheckProcess;
import ru.viise.lightsearch.cmd.process.v2.UnbindProcess;
import ru.viise.lightsearch.data.pojo.SendForm;

public class ProcessesDefaultImpl implements Processes {

    private static final Map<String, Process<? extends SendForm>> processes = new HashMap<>();
    public static boolean isChange = false;

    public ProcessesDefaultImpl(NetworkService networkService) {
        if(processes.isEmpty()) {
            processes.put(ClientCommands.LOGIN, new AuthorizationProcess(networkService));
            processes.put(ClientCommands.SEARCH, new SearchProcess(networkService));
            processes.put(ClientCommands.OPEN_SOFT_CHECK, new OpenSoftCheckProcess(networkService));
            processes.put(ClientCommands.CANCEL_SOFT_CHECK, new CancelSoftCheckProcess(networkService));
            processes.put(ClientCommands.CLOSE_SOFT_CHECK, new CloseSoftCheckProcess(networkService));
            processes.put(ClientCommands.CONFIRM_SOFT_CHECK_PRODUCTS, new ConfirmSoftCheckProductsCommandProcess(networkService));
            processes.put(ClientCommands.BIND, new BindProcess(networkService));
            processes.put(ClientCommands.BIND_CHECK, new BindCheckProcess(networkService));
            processes.put(ClientCommands.UNBIND, new UnbindProcess(networkService));
            processes.put(ClientCommands.UNBIND_CHECK, new UnbindCheckProcess(networkService));

        }

        if(isChange) {
            processes.put(ClientCommands.LOGIN, new AuthorizationProcess(networkService));
            processes.put(ClientCommands.SEARCH, new SearchProcess(networkService));
            processes.put(ClientCommands.OPEN_SOFT_CHECK, new OpenSoftCheckProcess(networkService));
            processes.put(ClientCommands.CANCEL_SOFT_CHECK, new CancelSoftCheckProcess(networkService));
            processes.put(ClientCommands.CLOSE_SOFT_CHECK, new CloseSoftCheckProcess(networkService));
            processes.put(ClientCommands.CONFIRM_SOFT_CHECK_PRODUCTS, new ConfirmSoftCheckProductsCommandProcess(networkService));
            processes.put(ClientCommands.BIND, new BindProcess(networkService));
            processes.put(ClientCommands.BIND_CHECK, new BindCheckProcess(networkService));
            processes.put(ClientCommands.UNBIND, new UnbindProcess(networkService));
            processes.put(ClientCommands.UNBIND_CHECK, new UnbindCheckProcess(networkService));

            ProcessesDefaultImpl.isChange = false;
        }
    }

    @Override
    public Process<? extends SendForm> process(String name) {
        return processes.get(name);
    }
}
