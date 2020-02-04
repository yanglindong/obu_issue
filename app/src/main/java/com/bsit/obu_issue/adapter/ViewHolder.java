package com.bsit.obu_issue.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * ViewHolder
 */
public class ViewHolder {

    private SparseArray<View> array;// 保存布局中的控件-android提供的比HashMap效率更高的Map
    private View mConverView;// 布局
    private int mPosition;// 位置标识


    public ViewHolder(Context context, ViewGroup parent, int layoutId,
                      int position) {
        array = new SparseArray();
        mPosition = position;
        mConverView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mConverView.setTag(this);

    }

    /**
     * 获取ViewHolder
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder getViewHodler(Context context, View convertView,
                                           ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public View getmConverView() {
        return mConverView;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = array.get(viewId);
        if (view == null) {
            view = mConverView.findViewById(viewId);
            array.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * @param viewId
     * @return
     */
    public ImageView getIv(int viewId) {
        return getView(viewId);
    }

    /**
     * @param viewId
     * @return
     */
    public TextView getTv(int viewId) {
        return getView(viewId);
    }

    /**
     * 设置文字
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置背景色
     *
     * @param viewId
     * @return
     */
    public void setBackgroundColor(int viewId, String color) {
        View tv = getView(viewId);
        GradientDrawable myGrad = (GradientDrawable) tv.getBackground();
        myGrad.setColor(Color.parseColor("#" + (TextUtils.isEmpty(color) ? "198fd5" : color)));
    }

    /**
     * 设置click监听
     *
     * @param viewId
     * @param clickListener
     * @return
     */
    public ViewHolder setClick(int viewId, final int position, final OnItemClickListen clickListener) {
        View tv = getView(viewId);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClickListen(position, v);
            }
        });
        return this;
    }

    /**
     * 设置文字
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text, int color) {
        TextView tv = getView(viewId);
        tv.setText(text);
        tv.setTextColor(color);
        return this;
    }

    /**
     * 设置文字图标
     *
     * @param direction 0左  1上  2右  3下
     */
    public ViewHolder setTextDrawable(Context context, int textViewId, int drawableId, int direction) {
        TextView textView = getView(textViewId);
        Drawable img = context.getResources().getDrawable(drawableId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        switch (direction) {
            case 0:
                textView.setCompoundDrawables(img, null, null, null); //设置左图标
                break;
            case 1:
                textView.setCompoundDrawables(null, img, null, null); //设置上图标
                break;
            case 2:
                textView.setCompoundDrawables(null, null, img, null); //设置右图标
                break;
            case 3:
                textView.setCompoundDrawables(null, null, null, img); //设置下图标
                break;
        }
        return this;
    }

    /**
     * @param viewId
     * @return
     */
    public ViewHolder setFlags(int viewId) {
        TextView tv = getView(viewId);
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        return this;
    }

    /**
     * @param viewId
     * @return
     */
    public ViewHolder setLayoutParams(int viewId, LinearLayout.LayoutParams lp) {
        View view = getView(viewId);
        view.setLayoutParams(lp);
        return this;
    }

    /**
     * @param viewId
     * @param bitmap
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * @param viewId
     * @param drawable
     * @return
     */
    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
        return this;
    }


    /**
     * @param viewId
     * @param isChecked
     * @return
     */
    public ViewHolder setChecked(int viewId, boolean isChecked) {
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(isChecked);
        return this;
    }


    public interface OnItemClickListen {
        void onItemClickListen(int position, View view);
    }
}
