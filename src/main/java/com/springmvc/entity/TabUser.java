package com.springmvc.entity;

public class TabUser {
    private String sId;

    private String sName;

    private Long dtEstablish;

    private String sPassword;

    private Integer iLevel;

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Long getDtEstablish() {
        return dtEstablish;
    }

    public void setDtEstablish(Long dtEstablish) {
        this.dtEstablish = dtEstablish;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public Integer getiLevel() {
        return iLevel;
    }

    public void setiLevel(Integer iLevel) {
        this.iLevel = iLevel;
    }
}