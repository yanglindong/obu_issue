package com.bluetooth.library.callback;

import android.bluetooth.BluetoothDevice;

import com.bluetooth.library.exception.ScanException;

public interface OnScanDeviceResonse {
   void onScanSuccess(BluetoothDevice bluetoothDevice, int i, byte[] bytes);
   void onScanFailed(ScanException scanException);
}
