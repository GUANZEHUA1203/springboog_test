package com.gzh.springboot.domain;

import java.util.Date;

/**
 * @author zehua
 *门禁 卡人脸绑定信息
 */
public class OwnOpenKey {
    private Long openid;

    private String openKey;

    private Integer keyType;

    private Long ownId;

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

    public Integer getKeyType() {
        return keyType;
    }

    public void setKeyType(Integer keyType) {
        this.keyType = keyType;
    }

    public Long getOwnId() {
        return ownId;
    }

    public void setOwnId(Long ownId) {
        this.ownId = ownId;
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