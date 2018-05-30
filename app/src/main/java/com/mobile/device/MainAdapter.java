package com.mobile.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CimZzz on 2018/5/29.<br>
 * Project Name : YIQIMMM<br>
 * Since : YIQIMMM_2.04<br>
 * Description:<br>
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    private List<Device> deviceList;
    private final Callback callback;

    public MainAdapter(Callback callback) {
        this.callback = callback;
        deviceList = new ArrayList<>();
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList.clear();
        this.deviceList.addAll(deviceList);
        notifyDataSetChanged();
    }

//    public void insertDevice(Device device) {
//        this.deviceList.add(device);
//        notifyItemInserted(this.deviceList.size() - 1);
//    }

    public void removeDevice(Device device) {
        int index = this.deviceList.indexOf(device);
        if(index != -1) {
            this.deviceList.remove(device);
            notifyDataSetChanged();
        }
    }

    public void updateDeviceStatus(Device device) {
        int index = this.deviceList.indexOf(device);
        if(index != -1) {
            Device findDevice = this.deviceList.get(index);
            findDevice.isOpen = device.isOpen;
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_list,parent,false),callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.deviceTitle.setText(device.getDeviceName());
        holder.deviceDesc.setText(device.getDesc());
        holder.deviceStatus.setText(device.isOpen ? "开" : "关");
        holder.bindDevice = device;
    }

    @Override
    public int getItemCount() {
        return this.deviceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Device bindDevice;

        TextView deviceTitle;
        TextView deviceDesc;
        TextView deviceStatus;
        View deviceSwitch;
        View deviceDel;


        public ViewHolder(View itemView, final Callback callback) {
            super(itemView);
            deviceTitle = (TextView) itemView.findViewById(R.id.deviceTitle);
            deviceDesc = (TextView) itemView.findViewById(R.id.deviceDesc);
            deviceStatus = (TextView) itemView.findViewById(R.id.deviceStatus);
            deviceSwitch = itemView.findViewById(R.id.deviceSwitch);
            deviceDel = itemView.findViewById(R.id.deviceDel);

            deviceDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onDel(bindDevice);
                }
            });

            deviceSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onSwitch(bindDevice);
                }
            });
        }
    }

    public interface Callback {
        void onSwitch(Device device);
        void onDel(Device device);
    }
}
