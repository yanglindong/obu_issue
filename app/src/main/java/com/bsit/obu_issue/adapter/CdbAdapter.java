package com.bsit.obu_issue.adapter;

import android.content.Context;

import com.bsit.obu_issue.ExpandDevice;
import com.bsit.obu_issue.R;

import java.util.List;

/**
 * Created by DELL on 2018/2/8.
 * 充电宝列表适配
 */

public class CdbAdapter extends CommonAdapter<ExpandDevice> {
    Context mContext;

    public CdbAdapter(Context context, List<ExpandDevice> data) {
        super(context, data, R.layout.list_item_device);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, ExpandDevice info, int position) {
        if (info != null) {
            holder.setText(R.id.textview_device_name, info.getDeviceName());
            holder.setText(R.id.textview_device_rssi, info.getRssi()+"" );
            holder.setText(R.id.textview_device_address, info.getMacAddress().replace(":",""));
        }
    }
}
