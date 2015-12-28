package com.dxz.helloworld;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dxz on 15/12/21.
 */
public class MainApplication extends Application {

    private static long mLastTimeStamp = 0;
    private static final int THRESHOLD_RETURN2MAIN = 1000;
    private static MainApplication sMainApplication = null;

    private static final int CACHE_DEFAULT_IMAGE_NUM = 100;
    private static final int ONE_K = 1024;
    private static final int ONE_M = 1024 * ONE_K;
    private static final int CACHE_DEFAULT_SIZE = 50 * ONE_M;

    private List<Activity> mActivityStack = new LinkedList<Activity>();


    @Override
    public void onCreate() {
        super.onCreate();
        if (sMainApplication == null) {
            sMainApplication = this;
        }
        x.Ext.init(sMainApplication);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static void returnMainActivity(String aFrom) {
        long curTime = System.currentTimeMillis();
        if ((curTime - mLastTimeStamp) > THRESHOLD_RETURN2MAIN) {
            // TODO:
            Intent intent = new Intent("com.dxz.helloworld.action.extend");
            intent.putExtra("from", aFrom);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sMainApplication.startActivity(intent);
        }
    }


    public static void setMainActivityQuitTime(long aTime) {
        mLastTimeStamp = aTime;
    }

    public static synchronized MainApplication getInstance() {
        return sMainApplication;
    }

    public void addActivity(Activity activity) {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            if (!mActivityStack.contains(activity)) {
                mActivityStack.add(activity);
            }
        } else {
            mActivityStack.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity);
            }
        }
    }

    public void exit() {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (Activity activity : mActivityStack) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    public void clearActivity() {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (Activity activity : mActivityStack) {
                activity.finish();
            }
        }

    }

}
