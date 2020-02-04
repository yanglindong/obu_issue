package com.bsit.obu_issue;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class UartService extends Service {
    private final static String TAG = "obu_yld";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    private BluetoothGattCharacteristic writeCharacteristic;
    private BluetoothGattService mBluetoothService;
    private BluetoothGattCharacteristic notityCharacteristic;


    private BluetoothDevice device = null;
    private final Object mLock = new Object();
    private int mErrorState = -1;
    private int connectState = BleError.STATE_DISCONNECT.getErrorCode();
    private boolean mNotificationsEnabled;
    String mMacAddress = "";
    public String cardTage = ""; //寻卡返回标志 同一张卡 返回值相同
    private boolean isReplace = false;
    private static final UUID CONFIG_DESCRIPTOR_UUID = UUID
            .fromString("0000fec9-0000-1000-8000-00805f9b34fb");

    private UUID SERVICE_UUID = UUID
            .fromString("0000fee7-0000-1000-8000-00805f9b34fb");

    private UUID WRITE_CHARACTER = UUID
            .fromString("0000fec7-0000-1000-8000-00805f9b34fb");

    private UUID NOTIFY_CHARACTER = UUID
            .fromString("0000fec8-0000-1000-8000-00805f9b34fb");
    // 0000fec7-0000-1000-8000-00805f9b34fb
    //0000fec9-0000-1000-8000-00805f9b34fb
    //0000fec8-0000-1000-8000-00805f9b34fb

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /**
         * 连接状态发生变化
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            Log.e(TAG, "status is " + status + " and ------ newState is " + newState);
            mErrorState = status;
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    if (connectState != BleError.STATE_CONNECTED_AND_READY.getErrorCode()) {
                        connectState = BleError.STATE_CONNECTED
                                .getErrorCode();
                        Log.e(TAG, "conConnectionStateChange onnectState ====" + connectState);
                    }
                    if (!gatt.discoverServices()) {
                        mErrorState = BleError.SERVICE_DISCOVERY_NOT_STARTED
                                .getErrorCode();
                    }

                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    connectState = BleError.STATE_DISCONNECT.getErrorCode();
                    refreshDeviceCache(gatt);
                    Log.e(TAG, "state is : " + connectState + (gatt == null));
                    if (gatt != null) {
                        mNotificationsEnabled = false;
                        gatt.close();
                    }
                }

            } else {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Connection state change error: "
                            + status + " newState: " + newState);
                }
                connectState = BleError.STATE_CONNECTED.getErrorCode();
            }

            sendBroadcast(new Intent(ActionConstant.BLE_STATUS));
            synchronized (mLock) {
                mLock.notifyAll();
            }
        }

        /**
         * 发现新设备
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "Services discovered");
                }
                if (BleError.SERVICE_DISCOVERY_NOT_STARTED
                        .getErrorCode() != connectState) {
                    connectState = BleError.STATE_SERVICE_DISCOVERY
                            .getErrorCode();
                }
            } else {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Service discovery error: "
                            + status);
                }
            }
            UartService.this.mErrorState = status;
            synchronized (mLock) {
                UartService.this.mLock.notifyAll();
            }

        }

        /**
         * 写描述返回
         */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

                if (UartService.CONFIG_DESCRIPTOR_UUID.equals(descriptor
                        .getUuid())) {
                    UartService.this.mNotificationsEnabled = (descriptor
                            .getValue()[0] == 1);
                    if (UartService.this.mNotificationsEnabled) {
                        UartService.this.connectState = BleError.STATE_CONNECTED_AND_READY
                                .getErrorCode();
                        Log.e(UartService.this.TAG, "通知通道打开 connectState " + connectState);
                    }
                }
            }
            UartService.this.mErrorState = status;
            synchronized (mLock) {
                mLock.notifyAll();
            }
        }

        /**
         * 收到蓝牙回复数据
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            try {
                parseData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void bleDisconnected() {
        cardTage = "";

        Intent deviceWrongIntent = new Intent(ActionConstant.FIND_CARD_FAILD_TAG);
        sendBroadcast(deviceWrongIntent);
    }

    private void refreshDeviceCache(BluetoothGatt mgatt) {
        if (mgatt == null) {
            return;
        }
        try {
            Method refresh = mgatt.getClass().getMethod("refresh");
            if (refresh != null) {
                boolean success = ((Boolean) refresh.invoke(mgatt))
                        .booleanValue();
            }
            Method m = mgatt.getDevice().getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(mgatt.getDevice(), (Object[]) null);
        } catch (Exception e) {
            Log.e(this.TAG, "An exception occured while refreshing device", e);
        }
    }

    public interface ConnectCallback {
        public void connectFailed(String msg);

        public void connectSuccess();
    }


    /**
     * 连接设备
     *
     * @param macAddress
     */
    public synchronized void connectDeveice(final String macAddress, final ConnectCallback connectCallback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (checkBlueToothEnable() != 0) {
                    connectCallback.connectFailed("蓝牙未开启");
                    return;
                }
                if (TextUtils.isEmpty(macAddress)) {
                    connectCallback.connectFailed("MAC地址为空");
                    return;
                }
                try {
                    device = mBluetoothAdapter.getRemoteDevice(macAddress);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                if (device == null) {
                    connectCallback.connectFailed("设备匹配失败");
                    return;
                }
                Log.e(TAG, "进入链接  ");
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.close();
                }
                mBluetoothGatt = device.connectGatt(UartService.this, false, mGattCallback);
                try {
                    synchronized (mLock) {
                        do {
                            mLock.wait();
                            if ((connectState != BleError.STATE_CONNECTING
                                    .getErrorCode())
                                    && (connectState != BleError.STATE_CONNECTED
                                    .getErrorCode()))
                                break;
                        } while (mErrorState == 0);
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, "Sleeping interrupted", e);
                }

                Log.e(TAG, "连接步骤已经完成");
                if (mBluetoothGatt == null) {
                    connectCallback.connectFailed("蓝牙协议异常");
                    return;
                }
                if (mErrorState > 0) {
                    connectCallback.connectFailed("蓝牙连接异常 错误码：" + mErrorState);
                    terminateConnection(mErrorState);
                    return;
                }

                mMacAddress = macAddress;
                Log.e(TAG, "mMacAddress     = " + mMacAddress);
                CommonConstant.ZSC_DEVICE_ID = ByteUtil.appendLengthForMessage(CommUtils.stringPas(CommUtils.parseMac(mMacAddress)), 16);
                Log.e(TAG, "开始获取设备服务");
                mBluetoothService = mBluetoothGatt.getService(SERVICE_UUID);
                if (mBluetoothService == null) {
                    terminateConnection(BleError.SERVICE_NOT_FOUND
                            .getErrorCode());
                    connectCallback.connectFailed("蓝牙服务为空");
                    return;
                }
                Log.e(TAG, "获取了设备服务");

                List<BluetoothGattCharacteristic> bluetoothGattCharacteristicList =  mBluetoothService.getCharacteristics();
                for (BluetoothGattCharacteristic bluetoothGattCharacteristic :bluetoothGattCharacteristicList){
                    Log.e(TAG, "获取了设备服务uuid"+ bluetoothGattCharacteristic.getUuid());
                }
                writeCharacteristic = mBluetoothService
                        .getCharacteristic(WRITE_CHARACTER);
                notityCharacteristic = mBluetoothService
                        .getCharacteristic(NOTIFY_CHARACTER);
                if ((writeCharacteristic == null)
                       ) {
                    terminateConnection(BleError.CHARACTERISTICS_NOT_FOUND
                            .getErrorCode());
                    connectCallback.connectFailed("蓝牙通知打开失败");
                    return;
                }
                setCharacteristicNotification(mBluetoothGatt, notityCharacteristic, true);
                connectState = BleError.STATE_CONNECTED_AND_READY
                        .getErrorCode();
                connectCallback.connectSuccess();
            }
        }.start();
    }


    private void terminateConnection(int error) {
        Log.e(this.TAG, "将要中断连接 error = " + error);
//		sendBroadcast(new Intent(ActionConstant.GOTO_SCAN_CODE_ACTIVITY));
        if (this.connectState != BleError.STATE_DISCONNECT.getErrorCode()) {
            if (BuildConfig.DEBUG) {
                Log.w(this.TAG, "正在停止连接" + error + "---state = "
                        + this.connectState);
            }
            try {
                BluetoothGattService dfuService = this.mBluetoothGatt
                        .getService(this.SERVICE_UUID);
                if (dfuService != null) {
                    BluetoothGattCharacteristic controlPointCharacteristic = dfuService
                            .getCharacteristic(this.NOTIFY_CHARACTER);
                    setCharacteristicNotification(this.mBluetoothGatt,
                            controlPointCharacteristic, false);
                }
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        if (error != 0) {
            if ((error > 260) || (error == 141)) {
                if (BuildConfig.DEBUG) {
                    Log.i(this.TAG, "断开 error" + error);
                }
            } else {
                // 重连
//                 connectDeveice(mMacAddress);
            }
        }
        this.mErrorState = 0;
    }

    private void setCharacteristicNotification(BluetoothGatt gatt,
                                               BluetoothGattCharacteristic characteristic, boolean enable) {
        this.mErrorState = 0;

        if (gatt != null && characteristic != null) {
            gatt.setCharacteristicNotification(characteristic, enable);
            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(CONFIG_DESCRIPTOR_UUID);
            if (descriptor != null) {
                descriptor
                        .setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }
        }

        try {
            synchronized (mLock) {
                do {
                    if (BuildConfig.DEBUG) {
                        Log.e(this.TAG, "mNotificationsEnabled = "
                                + this.mNotificationsEnabled + "---enable = "
                                + enable + "---connectState = " + this.connectState);
                    }
                    mLock.wait();
                    if ((this.mNotificationsEnabled == enable)
                            || (((this.connectState != BleError.STATE_SERVICE_DISCOVERY
                            .getErrorCode()) || (!enable)) && ((enable) || (this.connectState != BleError.STATE_CONNECTED_AND_READY
                            .getErrorCode()))))
                        break;
                } while (this.mErrorState == 0);
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(this.TAG, "Sleeping interrupted", e);
            }
        }
    }

    /**
     * 检测蓝牙是否可用
     */
    private int checkBlueToothEnable() {
        int state = 0;
        if (this.mBluetoothAdapter == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                state = 101;
            } else {
                this.mBluetoothAdapter = bluetoothManager.getAdapter();
                state = 0;
            }
        }
        if (this.mBluetoothAdapter == null)
            state = 101;
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE))
            state = 102;
        if (!this.mBluetoothAdapter.isEnabled())
            state = 103;
        mBluetoothAdapter.enable();
        return state;
    }


    private int mGetCardInfoIndex = 1;
    private int mGetQuanCunInitInfoIndex = 1;

    /**
     * 解析数据
     *
     * @param data
     * @throws Exception
     */
    private void parseData(byte[] data) throws Exception {
        Log.e(TAG, "返回数据: " + ByteUtil.byte2HexStr(data));
        /**
         *长度不够或者以6F00结尾，退出蓝牙连接
         */
        if (data.length < 2 || (data[data.length - 2] == 0x6F && data[data.length - 1] == 0)) {
            bleDisconnected();
            return;
        }
        /**
         * 关于升级包回复的指令处理
         */
        if (data[0] == -62 || data[0] == -61 || data[0] == -60) {
            switch (data[1]) {
                case 0://初始化时，1为成功
                    if (data[2] == 1)
                        sendBroadcast(new Intent(ActionConstant.START_SEND_PACKAGE));
                    else
                        sendBroadcast(new Intent(ActionConstant.BLE_GET_DATA_ERROR));
                    break;
                case 1://接收数据时，2为成功
                    if (data[2] == 2)
                        sendBroadcast(new Intent(ActionConstant.START_SEND_PACKAGE));
                    else
                        sendBroadcast(new Intent(ActionConstant.BLE_GET_DATA_ERROR));
                    break;
            }


        } else {
            /**
             * 发送获取信息请求指令
             */
            switch (data[2]) {
                case (byte) -63://读取版本信息
                    break;
                case (byte) -74://寻卡指令

                    break;
                case -77://读卡指令B3
                    parseCardInfo(data);
                    break;
                case -76://圈存初始化指令B4
                    parseQuancunInitInfo(data, true);
                    break;
                case -73://获取卡信息B7
                    parseQuancunInitInfo(data, false);
                    break;
                case (byte) -75://圈存指令b5
                    Intent intent = new Intent(ActionConstant.TOP_UP_MAC2);
                    intent.putExtra("MacTeger", ByteUtil.byte2HexStr(data));
                    sendBroadcast(intent);
                    break;

            }
        }

    }



    /**
     * 解析圈存初始化信息
     *
     * @param data
     */
    private void parseQuancunInitInfo(byte[] data, boolean isInit) {

        if (data[3] == 1) {
            mGetQuanCunInitInfoIndex = 1;
            mQuancunInitDataLenth = data[4] * 2;
            if (quancunInitResult.length() == 0) {
                String result = ByteUtil.byte2HexStr(data);
                quancunInitResult.append(result.substring(10));
            }
            mGetQuanCunInitInfoIndex++;
        } else {
            int index = data[3];
            if (index == mGetQuanCunInitInfoIndex) {
                String result = ByteUtil.byte2HexStr(data);
                quancunInitResult.append(result.substring(8));
                mGetQuanCunInitInfoIndex++;
            }
        }
        if (mQuancunInitDataLenth == quancunInitResult.length()) {
            if (isInit) {
                Intent intent = new Intent(ActionConstant.TOP_UP);
                intent.putExtra("topUpInfo", quancunInitResult.toString());
                sendBroadcast(intent);
            } else {
                Intent intent = new Intent(ActionConstant.CARD_INFO_GET_TAG);
                intent.putExtra("topUpInfo", quancunInitResult.toString());
                sendBroadcast(intent);
            }
            quancunInitResult.delete(0, quancunInitResult.length());
        }
    }

    /**
     * 解析卡信息
     *
     * @param data
     */
    private void parseCardInfo(byte[] data) {
        if (data[3] == 1) {
            mGetCardInfoIndex = data[3];
            mCardInfoDataLenth = data[4] * 2;
            if (cardInfoDataResult.length() == 0) {
                String result = ByteUtil.byte2HexStr(data);
                cardInfoDataResult.append(result.substring(10));
            }
            mGetCardInfoIndex++;
        } else {
            int index = data[3];
            if (index == mGetCardInfoIndex) {
                String result = ByteUtil.byte2HexStr(data);
                cardInfoDataResult.append(result.substring(8));
                mGetCardInfoIndex++;
            }
        }
        if (mCardInfoDataLenth == cardInfoDataResult.toString().length()) {
            Intent cardInfoIntent = new Intent(ActionConstant.CARD_INFO_GET_SUCCESS_TAG);
            cardInfoIntent.putExtra("cardInfo", cardInfoDataResult.toString());
            sendBroadcast(cardInfoIntent);
            cardInfoDataResult.delete(0, cardInfoDataResult.length());
        }
    }



    private StringBuffer quancunInitResult = new StringBuffer();
    private int mQuancunInitDataLenth;

    private StringBuffer cardInfoDataResult = new StringBuffer();
    private int mCardInfoDataLenth;


    public class LocalBinder extends Binder {
        public UartService getService() {
            return UartService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Unable to initialize BluetoothManager.");
                }
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            }
            return false;
        }

        return true;
    }

    /**
     * 蓝牙是否连接可用
     *
     * @return
     */
    public boolean isConnect() {
        Log.e(TAG, "isConnect =====" + connectState);
        return connectState == BleError.STATE_CONNECTED_AND_READY.getErrorCode();
    }

    /**
     * 搜索设备
     *
     * @param
     */
    public boolean scan(BluetoothAdapter.LeScanCallback bleScanCallback) {
        Log.e(TAG, "开始扫描前蓝牙状态为 : " + connectState);
        if (checkBlueToothEnable() != 0) {
            return false;
        }
        stopScan(bleScanCallback);
        boolean scan = mBluetoothAdapter.startLeScan(bleScanCallback);
        return scan;
    }


    /**
     * 停止搜索设备
     */
    public void stopScan(BluetoothAdapter.LeScanCallback bleScanCallback) {
        this.mBluetoothAdapter.stopLeScan(bleScanCallback);
    }

    public boolean writeData(byte[] data) {
//		Log.e(this.TAG, "写入数据时 " + ByteUtil.byte2HexStr(data) );
        Boolean flag = false;
        if (this.connectState != BleError.STATE_CONNECTED_AND_READY
                .getErrorCode()) {
            //if (BuildConfig.DEBUG){
            Log.e(this.TAG, "写入数据时，蓝牙状态有误 state = " + this.connectState);
            //}
            bleDisconnected();
            return false;
        }
        if ((this.writeCharacteristic != null) && (this.mBluetoothGatt != null)) {
            writeCharacteristic.setValue(data);
            flag = this.mBluetoothGatt.writeCharacteristic(this.writeCharacteristic);
            /*if (!flag){
                bleDisconnected();
			}*/
            //if (BuildConfig.DEBUG){
            Log.e(this.TAG, "写入数据时 " + ByteUtil.byte2HexStr(data) + "写入状态" + flag);
            //}
        }
        return flag;
    }

    /**
     * 断开连接
     */
    public boolean disconnect() {
        Log.e(this.TAG, "断开连接------");
        mErrorState = 0;
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
        if (this.connectState == BleError.STATE_DISCONNECT.getErrorCode())
            return true;
        this.connectState = BleError.STATE_DISCONNECTING.getErrorCode();
        if (BuildConfig.DEBUG) {
            Log.i(this.TAG, "Disconnecting from the device...");
        }
        try {
            synchronized (mLock) {
                do {
                    mLock.wait();
                    if (this.connectState == BleError.STATE_DISCONNECT
                            .getErrorCode())
                        break;
                } while (this.mErrorState == 0);
            }
        } catch (InterruptedException e) {
            if (BuildConfig.DEBUG) {
                Log.e(this.TAG, "Sleeping interrupted", e);
            }
        }
        return connectState == BleError.STATE_DISCONNECT.getErrorCode();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.w(TAG, "mBluetoothGatt closed");
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

}

