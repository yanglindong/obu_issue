package com.bsit.obu_issue;

import android.app.Application;

public class BaseApplication extends Application {
    private static BaseApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }




    //获取单例
    public static BaseApplication getInstance() {
          return mApplication;
    }








}
