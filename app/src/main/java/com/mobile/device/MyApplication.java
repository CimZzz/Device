package com.mobile.device;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Created by CimZzz on 2018/5/29.<br>
 * Project Name : YIQIMMM<br>
 * Since : YIQIMMM_2.04<br>
 * Description:<br>
 */
public class MyApplication extends Application {
    public DaoSession daoSession;
    public OkHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        daoSession = DaoMaster.newDevSession(getApplicationContext(),"tb_device");
        client = new OkHttpClient.Builder()
                .build();
    }
}
