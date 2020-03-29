package com.example.appmainframe.Utils;

import android.app.Application;
import android.os.StrictMode;

import org.xutils.x;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //注册xUtils
        x.Ext.init(this);
        Density.setDensity(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
