package com.example.juexingzhe.hybrid.main;


import android.content.Context;
import android.os.IBinder;

import com.example.juexingzhe.hybrid.IBinderManager;

/**
 * Web进程和主进程之间Binder的管理
 */
public class BinderManager extends IBinderManager.Stub {

    public static final int BINDER_WEB_AIDL_CODE = 0x101;
    private Context context;

    public BinderManager(Context context) {
        this.context = context;
    }

    @Override
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        switch (binderCode) {
            case BINDER_WEB_AIDL_CODE:
                binder = new WebBinder(context);
                break;
            default:
                break;
        }

        return binder;
    }
}
