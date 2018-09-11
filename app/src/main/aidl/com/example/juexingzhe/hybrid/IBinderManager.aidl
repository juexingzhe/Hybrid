// IBinderManager.aidl
package com.example.juexingzhe.hybrid;
import android.os.IBinder;

// Declare any non-default types here with import statements

interface IBinderManager {
    IBinder queryBinder(int binderCode);
}
