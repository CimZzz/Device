package com.mobile.device;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by CimZzz on 6/12/18.<br>
 * Description : <br>
 * 时间选择对话框
 */
public class TimeDialog extends Dialog {
    private final Callback callback;

    public TimeDialog(@NonNull Context context, Callback callback) {
        super(context);
        this.callback = callback;
    }

    public void show(long time) {
        setContentView(R.layout.dialog_time);
        DatePicker datePicker = (DatePicker) findViewById(R.id.dpPicker);
        TimePicker timePicker = (TimePicker) findViewById(R.id.tpPicker);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                callback.onTimeChange(calendar.getTimeInMillis());
            }
        });
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
        timePicker.setCurrentMinute((int) (time % (1000 * 60 * 60) / 1000 / 60));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.MINUTE,i1);
                callback.onTimeChange(calendar.getTimeInMillis());
            }
        });

        super.show();
    }

    public interface Callback {
        void onTimeChange(long time);
    }

}
