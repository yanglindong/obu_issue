package com.bsit.obu_issue;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bsit.obu_issue.adapter.CdbAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/2/8.
 * 充电宝列表
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CdbListActivity extends BaseActivity {
    // 开启进度条
    public LoadingDialog dialog;

    @BindView(R.id.lv_cdb_listviw)
    ListView lvCdbListviw;
    private List<ExpandDevice> list = new ArrayList<>();
    CdbAdapter adapter;
    private ZSCBleBusiness mBleBusiness;
    private String resultDeviceType;
    private String scanMac;
    private String deviceName;
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
           // Log.e("obu_yld", "device=="+device.getName()+"  :  "+device.getAddress()+"  :  "+device.getUuids());
            ExpandDevice expandDevice = new ExpandDevice(device, rssi, scanRecord);
            if (expandDevice.getDeviceName() != null && expandDevice.getDeviceName().contains("JL")) {
                addOrUpdateDevice(expandDevice);
                Collections.sort(list);
                adapter.notifyDataSetChanged();
            }

        }
    };
    private UartService.ConnectCallback connectCallback = new UartService.ConnectCallback() {
        @Override
        public void connectFailed(String msg) {
            bleConnectedFailed(msg);
        }

        @Override
        public void connectSuccess() {
            bleConnectedSuccess();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_cdb_list);
    }



    public void showDialog(String msg) {
        dialog = new LoadingDialog(this, msg);
        dialog.show();
    }

    /**
     * 对话框消失
     */
    public void hideDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void fillView() {
        super.fillView();
        deviceName = getIntent().getStringExtra("name");
        mBleBusiness = ZSCBleBusiness.getInstance();
        adapter = new CdbAdapter(this, list);
        lvCdbListviw.setAdapter(adapter);
        lvCdbListviw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBleBusiness.stopScanBle(leScanCallback);
                resultDeviceType = list.get(position).getDeviceName();
                scanMac = list.get(position).getMacAddress().replace(":", "");
                showDialog("正在连接");
                connectDevice(list.get(position).getMacAddress());
            }
        });
        mBleBusiness.startScanBle(leScanCallback);

    }

    private void bleConnectedSuccess() {
        hideDialog();
        Intent intentActivity = new Intent(this, ObuActivity.class);
        intentActivity.putExtra("type", "11");
        intentActivity.putExtra("mac", scanMac);
        intentActivity.putExtra("deviceName", resultDeviceType);
        startActivity(intentActivity);
        finish();
    }

    private void connectDevice(String macAddress) {
        mBleBusiness.connectDevice(macAddress, connectCallback);
    }

    private void bleConnectedFailed(final String msg) {
        hideDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(CdbListActivity.this, msg);
            }
        });
    }

    //防止重复设备出现
    private void addOrUpdateDevice(ExpandDevice expandDevice) {
        int index = list.indexOf(expandDevice);
        if (index >= 0) {
            list.get(index).setRssi(expandDevice.getRssi());
        } else {
            list.add(expandDevice);
        }
    }

    @Override
    protected void onDestroy() {
        mBleBusiness.stopScanBle(leScanCallback);
        super.onDestroy();
    }



}
