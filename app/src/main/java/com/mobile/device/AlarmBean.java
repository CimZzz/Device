package com.mobile.device;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by CimZzz on 6/12/18.<br>
 * Description : <br>
 * 定时数据类，存放定时信息
 */
@Entity
public class AlarmBean {
    @Id(autoincrement = true)
    public Long id;
    public String deviceId;
    public long time;
    public boolean isOpen;
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getDeviceId() {
        return this.deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getIsOpen() {
        return this.isOpen;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    @Generated(hash = 1575002281)
    public AlarmBean(Long id, String deviceId, long time, boolean isOpen) {
        this.id = id;
        this.deviceId = deviceId;
        this.time = time;
        this.isOpen = isOpen;
    }
    @Generated(hash = 1834917355)
    public AlarmBean() {
    }
}
