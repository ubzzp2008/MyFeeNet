package com.biz.justin.myFeeNet.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;


public abstract class ProperTies {

    public static Properties getProperties(Context c) {
        Properties props = new Properties();
        try {
            //方法一：通过activity中的context攻取setting.properties的FileInputStream
            //注意这地方的参数appConfig在eclipse中应该是appConfig.properties才对,但在studio中不用写后缀
            //InputStream in = c.getAssets().open("appConfig.properties");
            InputStream in = c.getAssets().open("appConfig");
            //方法二：通过class获取setting.properties的FileInputStream
            //InputStream in = PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
            props.load(in);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return props;
    }

    public static String getServerUrl(Context context, String className, String method) {
        Properties proper = ProperTies.getProperties(context);
        String serviceUrl = proper.getProperty("serverUrl") + File.separator + className + File.separator + method;
        Log.i("TAG", "serviceUrl=" + serviceUrl);
        return serviceUrl;
    }

}
