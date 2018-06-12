package com.mobile.device;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by CimZzz on 6/12/18.<br>
 * Description : <br>
 * 定时界面
 */
public class AlarmActivity extends ActionBarUI {
    TextView timeView;

    Device device;

    long time = System.currentTimeMillis() + 3600 * 1000;
    boolean isOpen = true;

    @Override
    protected void onBaseUICreate(ActionBarUICreator creator) {
        creator.setActionBarID(R.layout.actionbar_alarm)
                .setLayoutID(R.layout.activity_alarm);
    }

    @Override
    protected void onViewInit(Bundle savedInstanceState) {
        device = (Device) getIntent().getSerializableExtra("device");

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        timeView = (TextView) findViewById(R.id.time);
        timeView.setText(getTime());
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimeDialog(AlarmActivity.this, new TimeDialog.Callback() {
                    @Override
                    public void onTimeChange(long time) {
                        AlarmActivity.this.time = time;
                        timeView.setText(getTime());
                    }
                }).show(time);
            }
        });

        ((TextView)findViewById(R.id.deviceName)).setText(device.getDeviceName());
        ((TextView)findViewById(R.id.deviceDesc)).setText(device.getDesc());
        findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交
                long currentTime = System.currentTimeMillis();
                if(time == -1 || time <= currentTime) {
                    Toast.makeText(AlarmActivity.this,"时间不合法",Toast.LENGTH_SHORT).show();
                    return;
                }

                AlarmBean alarmBean = new AlarmBean();
                alarmBean.deviceId = device.getUuid();
                alarmBean.time = time;
                alarmBean.setIsOpen(isOpen);

                AlarmBeanDao alarmBeanDao = ((NewApplication)getApplication()).daoSession.getAlarmBeanDao();
                alarmBeanDao.insert(alarmBean);

                AlarmManager alarmManager = (AlarmManager) getSystemService(
                        Context.ALARM_SERVICE);

                Intent intent = new Intent(AlarmActivity.this, AlarmService.class).setAction("com.mobile.device.update");
                PendingIntent uploadIntent = PendingIntent.getService(AlarmActivity.this
                        , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if(alarmManager == null) {
                    return;
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, uploadIntent);

                Toast.makeText(AlarmActivity.this,"定时成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        isOpen = device.getIsOpen();

        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                isOpen = i == R.id.open;
            }
        });
    }

    private String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
    }
}
