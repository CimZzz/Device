package com.mobile.device;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

/**
 * Created by CimZzz on 2018/6/7.<br>
 * Project Name : YIQIMMM<br>
 * Since : YIQIMMM_2.04<br>
 * Description:<br>
 */
public class EditDialog extends Dialog{
    Device device;
    EditText et;

    private final Callback callback;

    public EditDialog(@NonNull Context context,Callback callback) {
        super(context);
        this.callback = callback;
    }

    public void show(Device device) {
        this.device = device;
        setContentView(R.layout.dialog_edit);
        et = (EditText) findViewById(R.id.dialog_text);
        et.setText(device.desc);

        findViewById(R.id.dialog_closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.dialog_confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onConfirm(et.getText().toString());
            }
        });
        super.show();
    }

    public interface Callback {
        void onConfirm(String descStr);
    }
}
