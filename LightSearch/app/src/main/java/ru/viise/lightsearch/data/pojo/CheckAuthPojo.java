package ru.viise.lightsearch.data.pojo;

public class CheckAuthPojo implements SendForm {

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
