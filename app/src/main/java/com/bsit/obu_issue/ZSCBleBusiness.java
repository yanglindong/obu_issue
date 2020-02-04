package com.bsit.obu_issue;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ZSCBleBusiness {

    private UartService mService;
    private Context mContext;
    private static final String TAG = "obu_yld";
    private static ZSCBleBusiness mBleBusiness;

    private void service_init() {
        Log.e(TAG, "service_init: start");
        Intent bindIntent = new Intent(mContext, UartService.class);
        mContext.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "onServiceConnected mService= " + mService);
            }
            if (!mService.initialize()) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                }
                return;
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            // if (BuildConfig.DEBUG){
            Log.e(TAG, "no get info");
            //}
            mService = null;
        }
    };

    private ZSCBleBusiness() {
        mContext = BaseApplication.getInstance();
        Log.d(TAG, "BleBusiness: mContext is : " + mContext);
        if (mContext != null) {
            service_init();
        }
    }

    public synchronized static ZSCBleBusiness getInstance() {
        if (mBleBusiness == null || mBleBusiness.mService == null) {
            mBleBusiness = new ZSCBleBusiness();
        }
        return mBleBusiness;
    }


    /**
     * 搜索设备
     */
    public boolean startScanBle(BluetoothAdapter.LeScanCallback bleScanCallback) {
        if (mService == null) {
            service_init();
        }
        try {
            if (mService.isConnect()) {
                Log.e(TAG, "开始扫描前蓝牙状态为 : 已连接");
                mService.disconnect();
            }
            return mService.scan(bleScanCallback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 停止搜索设备
     */
    public void stopScanBle(BluetoothAdapter.LeScanCallback bleScanCallback) {
        if (mService == null) {
            return;
        }
        try {
            mService.stopScan(bleScanCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接设备
     */
    public void connectDevice(String deviceMac, UartService.ConnectCallback connectCallback) {

        Log.e(TAG, "连接deviceMac = " + deviceMac);
        if (mService == null) {
            connectCallback.connectFailed("蓝牙服务出错");
        }
        try {
            mService.cardTage="";
            mService.connectDeveice(deviceMac, connectCallback);
        } catch (Exception e) {
            e.printStackTrace();
            connectCallback.connectFailed(e.getMessage());
        }
    }

    /**
     * 蓝牙断开连接
     */
    public boolean disConnectDevice() {
        if (mService == null) {
            return false;
        }
        try {
            return mService.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 设备是否连接
     */
    public boolean isConnect() {
        if (mService == null) {
            Log.e(TAG, "mService is null");
            return false;
        }
        try {
            Log.e(TAG, "mService not is null");
            return mService.isConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送数据
     *
     * @param data 写入数据
     */
    public void writeDate(String deviceType, String data) {
        if (mService == null) {
            return;
        }
        try {
            mService.writeData(ByteUtil.hexStr("42" + deviceType + data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     *
     * @param data 二进制数组
     */
    public boolean writeByteDate(byte[] data) {
        if (mService == null) {
            return false;
        }
        try {
            return mService.writeData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送数据
     *
     * @param data 二进制数组
     */
    public boolean writeByteDateForResult(byte[] data) {
        if (mService == null) {
            return false;
        }
        try {
            return mService.writeData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
