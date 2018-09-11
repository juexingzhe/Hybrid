package com.example.juexingzhe.hybrid.web;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.juexingzhe.hybrid.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WebActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webView);

        final WebHelper webHelper = new WebHelper(this);
        webHelper.setWebView(webView);
        webHelper.setConnectCallback(new ServiceConnectCallback() {
            @Override
            public void onServiceConnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("file:///android_asset/testjs.html");
                    }
                });
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, final String message, final String defaultValue, JsPromptResult result) {
                if (!message.isEmpty()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("address", "ShangHai");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    result.confirm(jsonObject.toString());
                }

                return true;
            }
        });
    }
}
