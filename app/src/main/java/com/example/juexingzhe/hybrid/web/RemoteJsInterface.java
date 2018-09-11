package com.example.juexingzhe.hybrid.web;

import android.os.Handler;
import android.webkit.JavascriptInterface;

/**
 * Webview.addJavascriptInterface
 */
public final class RemoteJsInterface {

    private final Handler handler = new Handler();
    private JsFunctionCallback callback;


    @JavascriptInterface
    public void callJavaFunction(final String methodName, final String params) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (callback != null) {
                        callback.execute(methodName, params);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCallback(JsFunctionCallback callback) {
        this.callback = callback;
    }

    public interface JsFunctionCallback {
        void execute(String methodName, String params);
    }

}
