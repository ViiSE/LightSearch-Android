package ru.viise.lightsearch.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckAuthResultPojo {

    @SerializedName("ok")
    @Expose
    private boolean ok;

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }
}
