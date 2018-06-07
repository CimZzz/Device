package com.mobile.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends ActionBarUI {
    RecyclerView deviceList;
    View search;

    MainAdapter adapter;

    @Override
    protected void onBaseUICreate(ActionBarUICreator creator) {
        creator.setActionBarID(R.layout.actionbar_main);
        creator.setLayoutID(R.layout.activity_main);
    }

    @Override
    protected void onViewInit(Bundle savedInstanceState) {
        search = findViewById(R.id.search);
        deviceList = (RecyclerView) findViewById(R.id.deviceList);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,SearchActivity.class),1000);
            }
        });

        deviceList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new MainAdapter(new MainAdapter.Callback() {
            @Override
            public void onSwitch(final Device device) {

                final SVProgressHUD loadBar = new SVProgressHUD(MainActivity.this);
                loadBar.showWithStatus("修改设备状态中请稍后");
                final boolean afterStatus = !device.isOpen;
                OkHttpClient client = ((NewApplication)getApplication()).client;
                client.newCall(new Request.Builder()
                        .url("http://104.224.163.27:8080/ble/servlet/UpdateDevicesServlet")
                        .post(new FormBody.Builder()
                                .add("id",device.getUuid())
                                .add("change_sta",afterStatus ? "0" : "1")
                                .build())
                        .build())
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadBar.dismissImmediately();
                                        Toast.makeText(MainActivity.this,"修改设备状态失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                if(TextUtils.isEmpty(result) || !result.equals("success")) {
                                    onFailure(null,null);
                                    return;
                                }

                                device.setIsOpen(afterStatus);
                                updateDeviceStatus(device);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateDeviceStatus(device);
                                        adapter.updateDeviceStatus(device);
                                        loadBar.dismissImmediately();
                                        Toast.makeText(MainActivity.this,"修改设备状态成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                updateDeviceStatus(device);
                adapter.updateDeviceStatus(device);
            }

            @Override
            public void onEdit(final Device device) {
                new EditDialog(MainActivity.this, new EditDialog.Callback() {
                    @Override
                    public void onConfirm(final String descStr) {
                        final SVProgressHUD loadBar = new SVProgressHUD(MainActivity.this);
                        loadBar.showWithStatus("更改描述中请稍后");
                        OkHttpClient client = ((NewApplication)getApplication()).client;
                        client.newCall(new Request.Builder()
                                .url("http://104.224.163.27:8080/ble/servlet/ModifyDevicesServlet")
                                .post(new FormBody.Builder()
                                        .add("id",device.getUuid())
                                        .add("device_name",device.getDeviceName())
                                        .add("device_des",descStr)
                                        .build())
                                .build())
                                .enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadBar.dismissImmediately();
                                                Toast.makeText(MainActivity.this,"修改描述失败",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String result = response.body().string();
                                        if(TextUtils.isEmpty(result) || !result.equals("success")) {
                                            onFailure(null,null);
                                            return;
                                        }

                                        device.setDesc(descStr);
                                        updateDeviceStatus(device);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                updateDeviceStatus(device);
                                                adapter.updateDeviceStatus(device);
                                                loadBar.dismissImmediately();
                                                Toast.makeText(MainActivity.this,"修改描述成功",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                    }
                }).show(device);
            }

            @Override
            public void onDel(Device device) {
                removeDevice(device);
                adapter.removeDevice(device);
            }
        });
        deviceList.setAdapter(adapter);
        adapter.setDeviceList(getSaveDeviceList());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1000 && resultCode == 101)
            adapter.setDeviceList(getSaveDeviceList());
        else super.onActivityResult(requestCode, resultCode, data);
    }

    private List<Device> getSaveDeviceList() {
        DeviceDao deviceDao = ((NewApplication)getApplication()).daoSession.getDeviceDao();
        return deviceDao.loadAll();
    }

    private void removeDevice(Device device) {
        DeviceDao deviceDao = ((NewApplication)getApplication()).daoSession.getDeviceDao();
        deviceDao.delete(device);
    }

    private void updateDeviceStatus(Device device) {
        DeviceDao deviceDao = ((NewApplication)getApplication()).daoSession.getDeviceDao();
        deviceDao.update(device);
    }
}
