package ru.viise.lightsearch.data.entity;

import ru.viise.lightsearch.data.pojo.SearchPojo;

public class SearchCommandWithType implements Command<SearchPojo> {

    private final Command<SearchPojo> command;
    private final SearchCommandType type;

    public SearchCommandWithType(Command<SearchPojo> command, SearchCommandType type) {
        this.command = command;
        this.type = type;
    }

    @Override
    public SearchPojo formForSend() {
        SearchPojo searchPojo = command.formForSend();
        searchPojo.setType(type);

        return searchPojo;
    }

    @Override
    public String name() {
        return command.name();
    }
}
