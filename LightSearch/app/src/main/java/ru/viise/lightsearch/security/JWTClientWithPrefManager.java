package ru.viise.lightsearch.security;

import com.auth0.android.jwt.JWT;

import java.util.Date;

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
        String token = prefManager.load(PreferencesManagerType.TOKEN_MANAGER);

        if(token.equals(""))
            throw new JWTException("JWT is expired");

        JWT jwt = new JWT(token);
        Date exp = jwt.getExpiresAt();
        Date today = new Date();
        if(today.after(exp))
            throw new JWTException("JWT is expired");
    }
}
