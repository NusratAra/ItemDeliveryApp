package com.example.nishikanto.itemdeliverapp.model;

import com.google.gson.annotations.SerializedName;

public class UserCredential {

    @SerializedName("refresh")
    private String refresh;

    @SerializedName("access")
    private String access;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
