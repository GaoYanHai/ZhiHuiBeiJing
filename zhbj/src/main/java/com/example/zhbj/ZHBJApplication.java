package com.example.zhbj;

import android.app.Application;

import com.mob.MobSDK;



public class ZHBJApplication extends Application {
    @Override
    public void onCreate() {
        MobSDK.init(this);
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
        super.onCreate();

    }
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(base);
//    }
}
