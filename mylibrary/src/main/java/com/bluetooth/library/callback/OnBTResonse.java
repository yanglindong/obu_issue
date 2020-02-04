package com.bluetooth.library.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import com.bluetooth.library.exception.ConnectException;
import com.bluetooth.library.exception.ScanException;

public interface OnBTResonse {
    void onConnected(BluetoothGatt gatt, int status,
                     int newState);
    void onConnectFailed(ConnectException connectException);
    void onDisconnected(ConnectException connectException);

    void onServicesDiscovered(BluetoothGatt gatt, int status);
    void onServicesDiscoverFailed(ConnectException connectException);
}
