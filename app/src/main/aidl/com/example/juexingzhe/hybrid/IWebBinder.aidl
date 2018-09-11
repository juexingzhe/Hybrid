// IWebBinder.aidl
package com.example.juexingzhe.hybrid;
import  com.example.juexingzhe.hybrid.IWebBinderCallback;

// Declare any non-default types here with import statements

interface IWebBinder {
void handleJsFunction(in String methodName, in String params, in IWebBinderCallback callback);
}
