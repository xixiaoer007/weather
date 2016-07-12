package com.weather.app.activity;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

// 请在AndroidManifest.xml中application标签下android:name中指定该类
public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this, "a85ff3d75389be7099f7e50e0c5421a0");
    }
}
