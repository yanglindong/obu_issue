package com.bluetooth.library.exception;

public class ConnectException extends BaseException{

    public ConnectException(String desc, int errorCode) {
        super(desc, errorCode);
    }
}
