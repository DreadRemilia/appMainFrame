package com.example.appmainframe.Utils;

import android.app.Application;

import org.xutils.x;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        Density.setDensity(this);
    }
}
