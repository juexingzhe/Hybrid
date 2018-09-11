package com.example.juexingzhe.hybrid.web;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.juexingzhe.hybrid.HybridConfig;
import com.example.juexingzhe.hybrid.IBinderManager;
import com.example.juexingzhe.hybrid.main.BinderManager;
import com.example.juexingzhe.hybrid.main.MainServivce;

import java.util.concurrent.CountDownLatch;

/**
 * 用于Web进程向主进程发起链接，获取Binder
 */
public class WebBinderHandler {

    private CountDownLatch countDownLatch;

    private ServiceConnectImpl serviceConnect;

    private static volatile WebBinderHandler handler;

    private IBinderManager binderManager;

    private WebBinderHandler() {
    }

    public static WebBinderHandler getInstance() {
        if (handler == null) {
            synchronized (WebBinderHandler.class) {
                if (handler == null) {
                    handler = new WebBinderHandler();
                }
            }
        }

        return handler;
    }

    /**
     * 绑定主进程服务
     *
     * @param context
     */
    public synchronized void bindMainService(Context context) {
        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(context, MainServivce.class);
        if (serviceConnect == null) {
            serviceConnect = new ServiceConnectImpl(context);
        }

        context.bindService(intent, serviceConnect, Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ServiceConnectImpl implements ServiceConnection {

        private Context context;

        public ServiceConnectImpl(Context context) {
            this.context = context;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderManager = IBinderManager.Stub.asInterface(service);
            final int pid = android.os.Process.myPid();
            Log.i("Hybrid", String.format("=======onServiceConnected====== 进程ID: %s", pid));
            try {
                // Web进程监听binder的死亡通知
                binderManager.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        Log.i(HybridConfig.TAG, "=======binderDied==== 进程ID: %s" + pid);
                        binderManager.asBinder().unlinkToDeath(this, 0);
                        binderManager = null;
                        // binder死了再次去启动服务连接主进程
                        bindMainService(context);
                    }
                }, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /**
     * 获取与主进程通信的Binder
     *
     * @return
     */
    public IBinder getWebBinder() {
        IBinder binder = null;
        try {
            if (binderManager != null) {
                binder = binderManager.queryBinder(BinderManager.BINDER_WEB_AIDL_CODE);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return binder;
    }


    public synchronized void unbindMainService(Context context) {
        if (serviceConnect != null) {
            context.unbindService(serviceConnect);
        }
    }
}
