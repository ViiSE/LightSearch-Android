package ru.viise.lightsearch.cmd.network.task;

import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.SendForm;

public interface NetworkCallback<L extends SendForm, R extends SendForm> {
    void handleResult(CommandResult<L, R> result);
}
