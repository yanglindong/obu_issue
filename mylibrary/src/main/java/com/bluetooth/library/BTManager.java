package com.bluetooth.library;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.bluetooth.library.callback.OnConnectDeviceResonse;
import com.bluetooth.library.callback.OnBTResonse;
import com.bluetooth.library.callback.OnScanDeviceResonse;
import com.bluetooth.library.constants.Constants;
import com.bluetooth.library.exception.ConnectException;
import com.bluetooth.library.exception.ScanException;
import com.bluetooth.library.service.BleService;
import com.bluetooth.library.utils.LogUtils;

import java.util.List;
import java.util.UUID;

public class BTManager {
    private static BTManager btManager;
    private Context mContext;
    private BleService bleService;
    private String bleAddress;
    private  BluetoothAdapter bluetoothAdapter;
    private  BluetoothGatt gatt;

    public synchronized static BTManager getInstance(Context context) {
        if (btManager == null) {
            btManager = new BTManager(context);
        }
        return btManager;
    }

    public BTManager(Context context) {
        this.mContext = context;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void scanBluetoothDevice(final OnScanDeviceResonse onScanDeviceResonse){
        BluetoothManager bluetoothManager = (BluetoothManager) mContext
                .getSystemService(Context.BLUETOOTH_SERVICE);
         bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            onScanDeviceResonse.onScanFailed(new ScanException("设备不支持蓝牙",Constants.NOT_SUPPORT_BLUETOOTH));
        }
        if (!bluetoothAdapter.isEnabled()) {
            onScanDeviceResonse.onScanFailed(new ScanException("蓝牙没有开启",Constants.NOT_OPEN_BLUETOOTH));
        }
        bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
                onScanDeviceResonse.onScanSuccess(bluetoothDevice,i,bytes);
            }
        });
    }

    private final ServiceConnection conn = new ServiceConnection() {
        @SuppressLint("NewApi")
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            bleService = ((BleService.LocalBinder) service).getService();
            bleService.connect(bluetoothAdapter, bleAddress, new OnBTResonse() {
                @Override
                public void onConnected(BluetoothGatt gatt, int status, int newState) {
                    if(mOnConnectDeviceResonse!=null){
                        mOnConnectDeviceResonse.onConnected( gatt,  status,newState);
                    }
                }

                @Override
                public void onConnectFailed(ConnectException connectException) {
                    if(mOnConnectDeviceResonse!=null){
                        mOnConnectDeviceResonse.onConnectFailed(connectException);
                    }
                }

                @Override
                public void onDisconnected(ConnectException connectException) {
                    if(mOnConnectDeviceResonse!=null){
                        mOnConnectDeviceResonse.onConnectFailed(connectException);
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if(mOnConnectDeviceResonse!=null){
                        if(true){
                            mOnConnectDeviceResonse.setService(gatt.getService(""));
                        }else{
                            mOnConnectDeviceResonse.setServices(gatt.getServices());
                        }
                    }

                }

                @Override
                public void onServicesDiscoverFailed(ConnectException connectException) {
                    if(mOnConnectDeviceResonse!=null){
                        mOnConnectDeviceResonse.onServicesDiscoverFailed(connectException);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            bleService = null;
        }
    };
    private OnConnectDeviceResonse mOnConnectDeviceResonse;
    public void conntectBluetoothDevice(BluetoothDevice bluetoothDevice, OnConnectDeviceResonse onConnectDeviceResonse){
        this.mOnConnectDeviceResonse = onConnectDeviceResonse;
        bindBleSevice(true);
        bleAddress =  bluetoothDevice.getAddress();
    }


    public void bindBleSevice(boolean isStart) {
        if(isStart){
            Intent serviceIntent = new Intent(mContext, BleService.class);
            mContext.bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BluetoothGattService getService(UUID uuid){
        if(bleService==null||gatt==null){
            LogUtils.d("service is launch or device no connected");
        }
       return gatt.getService(uuid);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public List<BluetoothGattService> getServices(){
        if(bleService==null||gatt==null){
            LogUtils.d("service is launch or device no connected");
        }
        return gatt.getServices();
    }


}
