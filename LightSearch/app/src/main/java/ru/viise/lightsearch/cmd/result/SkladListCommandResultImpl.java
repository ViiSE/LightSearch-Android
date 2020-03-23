package ru.viise.lightsearch.cmd.result;

import ru.viise.lightsearch.data.ReconnectDTO;

public class SkladListCommandResultImpl implements SkladListCommandResult {

    private final boolean isDone;
    private final String message;
    private final String[] records;

    public SkladListCommandResultImpl(boolean isDone, String message, String[] records) {
        this.isDone = isDone;
        this.message = message;
        this.records = records;
    }

    @Override
    public String[] records() {
        return records;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean isReconnect() {
        return false;
    }

    @Override
    public ReconnectDTO reconnectDTO() {
        return null;
    }

    @Override
    public String message() {
        return message;
    }
}
