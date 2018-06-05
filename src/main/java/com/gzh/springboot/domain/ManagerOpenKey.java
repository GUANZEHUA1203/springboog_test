package com.gzh.springboot.domain;

import java.util.Date;

public class ManagerOpenKey {
    private Long openid;

    private String openKey;

    private String managerName;

    private String deviceId;

    private Short keyState;

    private Date createTime;

    private Date updateTime;

    private Date effectiveTime;

    public Long getOpenid() {
        return openid;
    }

    public void setOpenid(Long openid) {
        this.openid = openid;
    }

    public String getOpenKey() {
        return openKey;
    }

    public void setOpenKey(String openKey) {
        this.openKey = openKey == null ? null : openKey.trim();
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Short getKeyState() {
        return keyState;
    }

    public void setKeyState(Short keyState) {
        this.keyState = keyState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
}