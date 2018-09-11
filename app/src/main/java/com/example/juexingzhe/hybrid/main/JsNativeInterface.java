package com.example.juexingzhe.hybrid.main;

import android.os.RemoteException;

import com.example.juexingzhe.hybrid.HybridConfig;
import com.example.juexingzhe.hybrid.IWebBinderCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 给web端调用的方法
 */
public class JsNativeInterface {

    private static final String CALL_TO_USER_INFO = "javascript: onUserInfoResult(%s)";
    private static final String CALL_TO_ADDRESS = "javascript: onAddressResult(%s)";

    /**
     * 获取用户信息
     *
     * @param param
     * @param callback
     */
    public static void getUserInfo(final JSONObject param, final IWebBinderCallback callback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", "test@baidu.com");
            jsonObject.put("password", "1234567");

            // 回调给子进程调用js
            String backToJS = String.format(CALL_TO_USER_INFO, jsonObject.toString());
            if (callback != null) {
                callback.onResult(HybridConfig.MSG_TYPE_GET_USER_INFO, backToJS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取地址
     *
     * @param param
     * @param callback
     */
    public static void getAddress(final JSONObject param, final IWebBinderCallback callback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("address", "ShangHai");

            // 回调给子进程调用js
            String backToJS = String.format(CALL_TO_ADDRESS, jsonObject.toString());
            if (callback != null) {
                callback.onResult(HybridConfig.MSG_TYPE_GET_ADDRESS, backToJS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
