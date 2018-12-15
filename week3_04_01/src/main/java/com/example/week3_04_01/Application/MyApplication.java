package com.example.week3_04_01.Application;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
