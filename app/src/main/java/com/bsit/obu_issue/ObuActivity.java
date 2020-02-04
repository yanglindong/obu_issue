package com.bsit.obu_issue;

import androidx.annotation.RequiresApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ObuActivity extends BaseActivity {
    private String TAG = "RechargeActivity";

    private AlertDialog dialog;

    private ZSCBleBusiness mBleBusiness;
    private IntentFilter intentFilter;
    public static byte cmdHeader = 0;

    private MyHandler sHandler = new MyHandler(this);
    public class MyHandler extends Handler {

        WeakReference<Activity> activityWeakReference;

        MyHandler(Activity activity) {
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }





    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void fillView() {
        super.fillView();
       // mBleBusiness = ZSCBleBusiness.getInstance();
        registerMyBrocast();
/*
        deviceType = getIntent().getStringExtra("type");
        deviceName = getIntent().getStringExtra("deviceName");
        mac = getIntent().getStringExtra("mac");
*/


    }




    private void registerMyBrocast() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ActionConstant.CARD_INFO_GET_SUCCESS_TAG);
        intentFilter.addAction(ActionConstant.FIND_CARD_FAILD_TAG);
        intentFilter.addAction(ActionConstant.TOP_UP);
        intentFilter.addAction(ActionConstant.TOP_UP_MAC2);
        intentFilter.addAction(ActionConstant.BLE_STATUS);
        intentFilter.addAction(ActionConstant.GET_VERSION_INFO_SUCCESS);
        intentFilter.addAction(ActionConstant.START_SEND_PACKAGE);
        intentFilter.addAction(ActionConstant.BLE_GET_DATA_ERROR);
        intentFilter.addAction(ActionConstant.CARD_INFO_GET_TAG);
        intentFilter.addAction(ActionConstant.ETC_REQUEST_QUERY_SUCESECC);//ETC查询余额
        intentFilter.addAction(ActionConstant.ETC_REQUEST_TOPUP_SUCESECC);//ETC充值
        registerReceiver(topupReceive, intentFilter);
    }


    @Override
    public void onBackPressed() {
        dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage("是否确认结束本次操作？");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mBleBusiness.isConnect()) {
                    mBleBusiness.disConnectDevice();
                }
            }
        });

        dialog.show();
    }


    @Override
    protected void onDestroy() {

//        if (topupReceive != null/* && isRegister*/)
        unregisterReceiver(topupReceive);
        mBleBusiness.disConnectDevice();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        hideDialog();
//        dismissRechargeDialog();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onPause();
    }
    /**
     * 获取设备信息广播接收器
     */
    protected BroadcastReceiver topupReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {}
    };




    /**
     * @param @param i
     * @return void
     * @throws
     * @Title: sendData
     * @Description: (封装发送数据包)
     */
/*    private boolean sendData(int i) {
        Log.e(TAG, "index is : " + i);
        byte[] tempData = ByteUtil.getTempByte(cmdHeader);
        tempData[2] = (byte) (i & 0xFF);// 序列号
        tempData[3] = (byte) (i >> 8 & 0xFF);// 序列号
        int start = i - 1;
        System.arraycopy(mData, start * 16, tempData, 4, 16);
        return mBleBusiness.writeByteDateForResult(tempData);
    }*/





}