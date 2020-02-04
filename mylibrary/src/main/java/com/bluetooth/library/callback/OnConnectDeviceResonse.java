package com.bluetooth.library.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;

import com.bluetooth.library.exception.ConnectException;
import com.bluetooth.library.exception.ScanException;

import java.util.List;

public interface OnConnectDeviceResonse extends OnBTResonse{
  void  setServices( List<BluetoothGattService> bluetoothGattServiceList);
  void setService(BluetoothGattService bluetoothGattService);

}
