package com.bsit.obu_issue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.gyf.barlibrary.ImmersionBar;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 公共Activity
 */
public abstract class BaseActivity extends Activity {


    protected StaticHandler handler; //自定handler对象，需要在子类实现MyHandlerMessageInterface接口以及在子类中的setLister（）方法中对handler进行实例化
    private boolean isSupportIBar = true;
    private ImmersionBar mImmersionBar;
    // 开启进度条
    public LoadingDialog dialog;
    private PermissionsResultListener mListener;
    private int mRequestCode;
    /**
     * 自定handler类 避免handler内存回收问题导致的内存泄漏的问题
     */
    public static class StaticHandler extends Handler {
        WeakReference<Activity> mActivityReference;
        public MyHandlerMessageInterface handlerInterface;
        public StaticHandler(Activity activity, MyHandlerMessageInterface hanlerInterface) {
            mActivityReference = new WeakReference<>(activity);
            this.handlerInterface = hanlerInterface;
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                this.handlerInterface.onHandlerMessage(msg);
            }
        }
    }

    public interface MyHandlerMessageInterface{
        void onHandlerMessage(Message msg);
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
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && UiUtils.isTranslucentOrFloating(this)) {
//            UiUtils.fixOrientation(this);
//        }

        super.onCreate(savedInstanceState);
        initView();
        ButterKnife.bind(this);
        setLister();
        fillView();
    }

    /**
     * 设置界面布局
     */
    public abstract void initView();



    protected MyHandler requestHandler = new MyHandler(this);//handler对象，在子类中不需要进行实例之类操作


    /**
     * 自定Handler类 目的：避免子类handler由于内存回收问题导致内存泄漏问题
     * handleMessage处理一致的，均可用此handler进行处理。
     */
    protected class MyHandler extends Handler {
        WeakReference<Activity> activityWeakReference;

        MyHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if (activityWeakReference.get() != null && msg != null && msg.obj != null) {
                super.handleMessage(msg);
                ToastUtils.showToast(activityWeakReference.get(), (String) msg.obj);
            }
        }
    }

    /**
     * 其他 activity 继承 BaseSimpleActivity 调用 performRequestPermissions 方法
     *
     * @param desc        首次申请权限被拒绝后再次申请给用户的描述提示
     * @param permissions 要申请的权限数组
     * @param requestCode 申请标记值
     * @param listener    实现的接口
     */
    protected void performRequestPermissions(String desc, String[] permissions, int requestCode, PermissionsResultListener listener) {
        if (permissions == null || permissions.length == 0) return;
        mRequestCode = requestCode;
        mListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkEachSelfPermission(permissions)) {// 检查是否声明了权限
                requestEachPermissions(desc, permissions, requestCode);
            } else {// 已经申请权限
                if (mListener != null) {
                    mListener.onPermissionGranted();
                }
            }
        } else {
            if (mListener != null) {
                mListener.onPermissionGranted();
            }
        }
    }

    /**
     * 申请权限前判断是否需要声明
     *
     * @param desc
     * @param permissions
     * @param requestCode
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestEachPermissions(String desc, String[] permissions, int requestCode) {
        if (shouldShowRequestPermissionRationale(permissions)) {// 需要再次声明
            showRationaleDialog(desc, permissions, requestCode);
        } else {
            requestPermissions(permissions, requestCode);
        }
    }

    /**
     * 弹出声明的 Dialog
     *
     * @param desc
     * @param permissions
     * @param requestCode
     */
    private void showRationaleDialog(String desc, final String[] permissions, final int requestCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage(desc)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(permissions, requestCode);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setCancelable(false)
                .show();
    }


    /**
     * 再次申请权限时，是否需要声明
     *
     * @param permissions
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检察每个权限是否申请
     *
     * @param permissions
     * @return true 需要申请权限,false 已申请权限
     */
    private boolean checkEachSelfPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 申请权限结果的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequestCode) {
            if (checkEachPermissionsGranted(grantResults)) {
                if (mListener != null) {
                    mListener.onPermissionGranted();
                }
            } else {// 用户拒绝申请权限
                if (mListener != null) {
                    mListener.onPermissionDenied();
                }
            }
        }
    }

    /**
     * 检查回调结果
     *
     * @param grantResults
     * @return
     */
    private boolean checkEachPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限接口
     */
    public interface PermissionsResultListener {
        /**
         * 权限声请通过
         */
        void onPermissionGranted();

        /**
         * 权限申请拒绝
         */
        void onPermissionDenied();
    }
    /**
     * 设置控件监听
     */
    public void setLister(){}

    /**
     * 初始化界面数据
     */
    public void fillView(){}

    public class TimeCount extends CountDownTimer {
        private Button mButton;

        public TimeCount(long millisInFuture, long countDownInterval, Button mButton) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.mButton = mButton;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mButton.setText("获取验证码");
            mButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

            mButton.setClickable(false);
            mButton.setText(millisUntilFinished / 1000 + "s");
        }
    }

    /**
     * 体重数据 整数
     * @return 返回体重整数参数列表
     */
    public List<String> getWeightData1(){
        List<String> weight = new ArrayList();
        for(int i=20;i<200;i++){
            weight.add(i+"");
        }
        return weight;
    }
    /**
     * 体重数据 小数
     * @return 返回体重小数参数列表
     */
    public List<String> getWeightData2(){
        List<String> weight = new ArrayList();
        for(int i=0;i<10;i++){
            weight.add(i+"");
        }
        return weight;
    }

    /**
     * 身高数据初始化
     * @return 返回身高参数列表
     */
    public static List<String> getHeightData(){
        List<String> height = new ArrayList();
        for(int i=100;i<250;i++){
            height.add(i+"");
        }
        return height;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            //必须调用该方法，防止内存泄漏，不调用该方法，如果界面 bar 发生改变，在不关闭 app 的情况下，退出此界面再进入将记忆最后一次 bar 改变的状态
            mImmersionBar.destroy();
        }
        hideDialog();
    }
}

