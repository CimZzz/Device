package com.mobile.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

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
            public void onSwitch(Device device) {
                device.isOpen = !device.isOpen;
                updateDeviceStatus(device);
                adapter.updateDeviceStatus(device);
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
        DeviceDao deviceDao = ((MyApplication)getApplication()).daoSession.getDeviceDao();
        return deviceDao.loadAll();
    }

    private void removeDevice(Device device) {
        DeviceDao deviceDao = ((MyApplication)getApplication()).daoSession.getDeviceDao();
        deviceDao.delete(device);
    }

    private void updateDeviceStatus(Device device) {
        DeviceDao deviceDao = ((MyApplication)getApplication()).daoSession.getDeviceDao();
        deviceDao.update(device);
    }
}
