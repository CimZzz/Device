package com.mobile.device;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by CimZzz on 2018/5/29.<br>
 * Description:<br>
 */
@Entity
public class Device implements Serializable {
    @Id
    public String uuid;
    public String deviceName;
    public String desc;
    public boolean isOpen;
    public boolean getIsOpen() {
        return this.isOpen;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDeviceName() {
        return this.deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @Generated(hash = 1319315895)
    public Device(String uuid, String deviceName, String desc, boolean isOpen) {
        this.uuid = uuid;
        this.deviceName = deviceName;
        this.desc = desc;
        this.isOpen = isOpen;
    }
    @Generated(hash = 1469582394)
    public Device() {
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Device)
            return uuid.equals(((Device) obj).uuid);
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
