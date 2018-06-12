package com.mobile.device;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CimZzz on 6/12/18.<br>
 * Description : <br>
 * 接受定时任务的服务
 */
public class AlarmService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("CimZzz","Ring");
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmBeanDao alarmBeanDao = ((NewApplication)getApplication()).daoSession.getAlarmBeanDao();
                DeviceDao deviceDao = ((NewApplication)getApplication()).daoSession.getDeviceDao();
                List<AlarmBean> alarmBeans = alarmBeanDao.loadAll();
                OkHttpClient client = ((NewApplication)getApplication()).client;
                for(AlarmBean bean : alarmBeans) {
                    if(bean.getTime() + 2 * 60 * 1000 > System.currentTimeMillis())
                        continue;
                    try {
                        Response response = client.newCall(new Request.Builder()
                                .url("http://104.224.163.27:8080/ble/servlet/UpdateDevicesServlet")
                                .post(new FormBody.Builder()
                                        .add("id",bean.getDeviceId())
                                        .add("change_sta",bean.getIsOpen() ? "0" : "1")
                                        .build())
                                .build())
                                .execute();

                        if(response.isSuccessful()) {
                            String result = response.body().string();
                            if(!TextUtils.isEmpty(result) && result.equals("success")) {
                                Device device = deviceDao.load(bean.getDeviceId());
                                if(device != null) {
                                    device.setIsOpen(bean.getIsOpen());
                                    deviceDao.update(device);
                                }
                            }
                        }
                    } catch (Exception e) {

                    } finally {
                        alarmBeanDao.delete(bean);
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
