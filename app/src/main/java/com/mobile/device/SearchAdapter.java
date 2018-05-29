package com.mobile.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by CimZzz on 2018/5/29.<br>
 * Project Name : YIQIMMM<br>
 * Since : YIQIMMM_2.04<br>
 * Description:<br>
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Device> deviceList;
    private boolean isInserted;
    private final Set<Device> saveDeviceSet;
    private final Callback callback;

    public SearchAdapter(Set<Device> saveDeviceSet,Callback callback) {
        deviceList = new ArrayList<>();
        this.saveDeviceSet = saveDeviceSet;
        this.isInserted = false;
        this.callback = callback;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList.clear();
        this.deviceList.addAll(deviceList);
        notifyDataSetChanged();
    }

    public void insertDevice(Device device) {
        int index = deviceList.indexOf(device);
        if(index == -1)
            return;

        isInserted = true;
        saveDeviceSet.add(device);
        notifyDataSetChanged();
    }

    public boolean isInserted() {
        return isInserted;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_search,parent,false),callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.deviceTitle.setText(device.getDeviceName());
        holder.deviceDesc.setText(device.getDesc());
        if(saveDeviceSet.contains(device)) {
            holder.deviceAdd.setVisibility(View.GONE);
            holder.deviceAdded.setVisibility(View.VISIBLE);
        }
        else {
            holder.deviceAdd.setVisibility(View.VISIBLE);
            holder.deviceAdded.setVisibility(View.GONE);
        }
        holder.bindDevice = device;
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Device bindDevice;

        TextView deviceTitle;
        TextView deviceDesc;
        TextView deviceAdded;
        View deviceAdd;


        public ViewHolder(View itemView, final Callback callback) {
            super(itemView);
            deviceTitle = itemView.findViewById(R.id.deviceTitle);
            deviceDesc = itemView.findViewById(R.id.deviceDesc);
            deviceAdded = itemView.findViewById(R.id.deviceAdded);
            deviceAdd = itemView.findViewById(R.id.deviceAdd);

            deviceAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onInsert(bindDevice);
                }
            });
        }
    }

    public interface Callback {
        void onInsert(Device device);
    }
}
