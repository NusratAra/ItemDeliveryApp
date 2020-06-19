package com.example.nishikanto.itemdeliverapp;

import android.app.Application;

import com.example.nishikanto.itemdeliverapp.model.User;

public class ItemDeliveryApplication extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
