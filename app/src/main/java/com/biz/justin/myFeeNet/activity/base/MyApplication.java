package com.biz.justin.myFeeNet.activity.base;

import android.app.Application;

import org.xutils.x;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化
        x.Ext.init(this);
        // 设置是否输出debug
        x.Ext.setDebug(false);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
        //Global.setDefaultExecutor(Executors.newSingleThreadExecutor());
    }

}
