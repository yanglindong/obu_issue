package com.bsit.obu_issue;

public enum BleError {
    NOT_SUPPORT_BLUETOOTH(101, "不支持蓝牙"),

    NOT_SUPPORT_BLE(102, "不支持ble"),

    BLUETOOTH_IS_DISABLE(103, "蓝牙未打开,请打开蓝牙"),

    STATE_DISCONNECT(104, "设备已经断开了连接"),

    STATE_CONNECTING(105, "设备正在连接中"),

    STATE_SERVICE_DISCOVERY(106, "发现了设备服务"),

    STATE_CONNECTED(107, "设备已经连接上"),

    STATE_CONNECTED_AND_READY(108, "设备已经准备好，可以通讯了"),

    STATE_GATT_OVER(109, "设备正在断开中"),

    STATE_DISCONNECTING(133, "同一连接次数过多"),

    SERVICE_DISCOVERY_NOT_STARTED(261, "gatt失败，无法发现服务"),

    SERVICE_NOT_FOUND(262, "获取设备服务 , 结果为null"),

    CHARACTERISTICS_NOT_FOUND(263, "获取特征失败，结果为null"),

    CONNECT_TIME_OUT(264, "连接超时");

    private int errorCode;
    private String errorMsg;

    BleError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public static String getErrorMsgByCode(int errorCode) {
        for (BleError lm : values()) {
            if (lm.errorCode == errorCode) {
                return lm.errorMsg;
            }
        }
        return "unKnow Error";
    }
}

