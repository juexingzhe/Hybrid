package com.example.juexingzhe.hybrid;

import android.app.Application;
import android.content.Context;

import com.example.juexingzhe.hybrid.main.JsBridge;
import com.example.juexingzhe.hybrid.main.JsNativeInterface;
import com.example.juexingzhe.hybrid.web.WorkManager;

public class MyApplication extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        WorkManager.getInstance().postTask(new Runnable() {
            @Override
            public void run() {
                JsBridge.getInstance().register(JsNativeInterface.class);
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;
    }
}
