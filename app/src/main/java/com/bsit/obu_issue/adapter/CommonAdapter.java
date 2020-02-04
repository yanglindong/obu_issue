package com.bsit.obu_issue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 万能适配器
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    public List<T> mData;
    public Context mContext;
    private int mLayoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        mContext = context;
        mData = data;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHodler(mContext, convertView, parent, mLayoutId, position);
        convert(holder, mData.get(position), position);
        return holder.getmConverView();
    }

    public abstract void convert(ViewHolder holder, T t, int position);

}
