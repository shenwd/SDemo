package com.isme.shen.sdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.isme.shen.slibrary.SBaseApplication;

/**
 * Created by shen on 2016/9/8.
 */
public class BaseApplication extends SBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    /**
     * 应用需要的初始化工作
     * */
    private void init() {
        //Fresco初始化
        Fresco.initialize(this);
    }


}
