package com.mobile.device;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by CimZzz on 2018/4/26.<br>
 * Description:<br>
 * 用户数据类
 */
@Entity
public class User implements Serializable {
    @Id
    private String userName;
    private String userPwd;


    @Generated(hash = 861302266)
    public User(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
