package com.example.juexingzhe.hybrid.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MainServivce extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderManager(this);
    }
}
