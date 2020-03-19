package ru.viise.lightsearch.security;

import ru.viise.lightsearch.exception.JWTException;

public interface JWTClient {
    void check() throws JWTException;
}
