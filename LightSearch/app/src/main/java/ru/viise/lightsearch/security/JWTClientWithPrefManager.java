package ru.viise.lightsearch.security;

import com.auth0.android.jwt.JWT;

import ru.viise.lightsearch.exception.JWTException;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class JWTClientWithPrefManager implements JWTClient {

    private final PreferencesManager prefManager;

    public JWTClientWithPrefManager(PreferencesManager prefManager) {
        this.prefManager = prefManager;
    }

    @Override
    public void check() throws JWTException {
        JWT jwt = new JWT(prefManager.load(PreferencesManagerType.TOKEN_MANAGER));
        boolean isExpired = jwt.isExpired(1);
        if(isExpired)
            throw new JWTException("JWT is expired");
    }
}