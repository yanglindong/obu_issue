package com.bluetooth.library.exception;

public abstract class BaseException {
    private String desc;
    private int errorCode;

    public BaseException(String desc, int errorCode) {
        this.desc = desc;
        this.errorCode = errorCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ScanException{" +
                "desc='" + desc + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
