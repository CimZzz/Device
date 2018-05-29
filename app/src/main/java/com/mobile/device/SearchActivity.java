package com.mobile.device;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CimZzz on 2018/5/29.<br>
 * Project Name : YIQIMMM<br>
 * Since : YIQIMMM_2.04<br>
 * Description:<br>
 */
public class SearchActivity extends ActionBarUI {
    RefreshHandlerView handlerView;
    SwipeRefreshLayout refreshLayout;
    RecyclerView searchList;
    SearchAdapter adapter;
    boolean isFirstLoaded;

    @Override
    protected void onBaseUICreate(ActionBarUICreator creator) {
        creator.setActionBarID(R.layout.actionbar_search);
        creator.setLayoutID(R.layout.activity_search);

        isFirstLoaded = false;
    }

    @Override
    protected void onViewInit(Bundle savedInstanceState) {
        handlerView = findViewById(R.id.refreshHandler);
        refreshLayout = findViewById(R.id.refresh);
        searchList = findViewById(R.id.searchList);
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        handlerView.showRefresh();
        handlerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerView.showRefresh();
                refresh();
            }
        });

        searchList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new SearchAdapter(new HashSet<>(getSaveDeviceList()), new SearchAdapter.Callback() {
            @Override
            public void onInsert(Device device) {
                insertDevice(device);
                adapter.insertDevice(device);
            }
        });
        searchList.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refresh();
    }

    @Override
    public void finish() {
        if(adapter.isInserted())
            setResult(101);
        super.finish();
    }

    private void updateSuccess(List<Device> devices) {
        adapter.setDeviceList(devices);
        if(!isFirstLoaded) {
            isFirstLoaded = true;
            handlerView.showContent();
        }
        else refreshLayout.setRefreshing(false);
    }

    private void updateFailure() {
        if(!isFirstLoaded)
            handlerView.showError();
        else {
            Toast.makeText(this,"网络连接失败",Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }

    private List<Device> getSaveDeviceList() {
        DeviceDao deviceDao = ((MyApplication)getApplication()).daoSession.getDeviceDao();
        return deviceDao.loadAll();
    }

    private void insertDevice(Device device) {
        if(device == null)
            return;
        DeviceDao deviceDao = ((MyApplication)getApplication()).daoSession.getDeviceDao();
        deviceDao.insertOrReplace(device);
    }

    private void refresh() {
        OkHttpClient client = ((MyApplication)getApplication()).client;
        client.newCall(new Request.Builder()
                .url("http://104.224.163.27:8080/ble/servlet/QueryDevicesServlet")
                .build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateFailure();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject object = JSONObject.parseObject(response.body().string());
                    final List<Device> deviceList = new ArrayList<Device>();
                    for(Map.Entry<String,Object> entry : object.entrySet()) {
                        JSONObject itemObject = object.getJSONObject(entry.getKey());
                        Device device = new Device();
                        device.setUuid(itemObject.getString("id"));
                        device.setDesc(itemObject.getString("device_des"));
                        device.setDeviceName(itemObject.getString("device_name"));
                        String openStr = itemObject.getString("device_status");
                        device.setIsOpen(openStr != null && openStr.equals("0"));
                        deviceList.add(device);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateSuccess(deviceList);
                        }
                    });
                } catch (Exception e) {
                    onFailure(call,null);
                }
            }
        });
    }
}
