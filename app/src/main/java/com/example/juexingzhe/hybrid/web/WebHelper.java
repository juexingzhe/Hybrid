package com.example.juexingzhe.hybrid.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.example.juexingzhe.hybrid.HybridConfig;
import com.example.juexingzhe.hybrid.IWebBinder;
import com.example.juexingzhe.hybrid.IWebBinderCallback;


public class WebHelper implements RemoteJsInterface.JsFunctionCallback {
    public static final String JS_INTERFACE_NAME = "jsInterface";

    private Activity activity;
    private WebView webView;
    private RemoteJsInterface jsInterface;

    private IWebBinder webBinder;
    private ServiceConnectCallback connectCallback;

    public WebHelper(Activity activity) {
        this.activity = activity;
        jsInterface = new RemoteJsInterface();
        jsInterface.setCallback(this);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public void setWebView(WebView webView) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsInterface, JS_INTERFACE_NAME);
        bindService(activity);
    }

    protected void bindService(final Activity activity) {
        WorkManager.getInstance().postTask(new Runnable() {
            @Override
            public void run() {
                WebBinderHandler webBinderHandler = WebBinderHandler.getInstance();
                webBinderHandler.bindMainService(activity);
                IBinder binder = webBinderHandler.getWebBinder();
                webBinder = IWebBinder.Stub.asInterface(binder);
                if (connectCallback != null) {
                    connectCallback.onServiceConnected();
                }
            }
        });
    }

    @Override
    public void execute(String methodName, String params) {
        if (webBinder == null) {
            Log.i(HybridConfig.TAG, "WebBinder == null");
            return;
        }

        handleJsFunction(methodName, params);
    }

    protected void handleJsFunction(String methodName, String params) {
        try {
            webBinder.handleJsFunction(methodName, params, new IWebBinderCallback.Stub() {
                @Override
                public void onResult(int msgType, String message) {
                    Log.i(HybridConfig.TAG, "=======handleJsFunction.onResult() message:" + message);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript(message, null);
                    } else {
                        webView.loadUrl(message);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setConnectCallback(ServiceConnectCallback connectCallback) {
        this.connectCallback = connectCallback;
    }
}
