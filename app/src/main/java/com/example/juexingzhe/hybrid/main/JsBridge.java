package com.example.juexingzhe.hybrid.main;

import android.util.Log;

import com.example.juexingzhe.hybrid.HybridConfig;
import com.example.juexingzhe.hybrid.IWebBinderCallback;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class JsBridge {

    private static Map<String, Method> methodMap = new HashMap<>();

    private static volatile JsBridge bridge;

    public JsBridge() {
    }

    public static JsBridge getInstance() {
        if (bridge == null) {
            synchronized (JsBridge.class) {
                if (bridge == null) {
                    bridge = new JsBridge();
                }
            }
        }

        return bridge;
    }

    public void register(Class clazz) {
        try {
            parseMethods(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseMethods(Class clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method[] declaredMethods1 = clazz.getSuperclass().getDeclaredMethods();
        appendMethodsToMap(declaredMethods, methodMap);
        appendMethodsToMap(declaredMethods1, methodMap);
    }

    private void appendMethodsToMap(Method[] methods, Map<String, Method> methodMap) {
        for (Method method : methods) {
            String name = method.getName();
            if (name == null) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (null != parameterTypes) {
                if (parameterTypes[0] == JSONObject.class) {
                    methodMap.put(name, method);
                }
            }
        }
    }

    /**
     * 根据网页调用的方法名、参数，通过反射调用注册的客户端方法
     */
    public void callJava(final String methodName, final String param, final IWebBinderCallback callback) {
        if (methodMap.containsKey(methodName)) {
            Method method = methodMap.get(methodName);
            if (method != null) {
                try {
                    method.invoke(null, new JSONObject(param), callback);
                } catch (Exception e) {
                    Log.i(HybridConfig.TAG, "执行异常，请检查传入参数是否有误！");
                }
            } else {
                Log.i(HybridConfig.TAG, "Android侧没有定义该方法");
            }
        } else {
            Log.i(HybridConfig.TAG, "Android侧没有定义接口[" + methodName + "]");
        }
    }

}
