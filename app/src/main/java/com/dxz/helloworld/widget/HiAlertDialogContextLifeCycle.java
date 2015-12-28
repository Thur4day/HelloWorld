package com.dxz.helloworld.widget;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;

import com.dxz.helloworld.MainApplication;


/**
 * Created by jake on 3/6/15.
 */
public class HiAlertDialogContextLifeCycle implements Application.ActivityLifecycleCallbacks{

    /** the activity */
    private Activity mContext = null;

    /** the dialog */
    private Dialog mDialog = null;

    /** is dismiss on pause */
    private boolean isDismissedOnPause = false;

    /**
     * start
     */
    public void start(Activity activity, Dialog dialog){

        remove();

        mContext = activity;
        mDialog = dialog;

        if(null != mContext && null != mDialog){
            MainApplication.getInstance().registerActivityLifecycleCallbacks(this);
        }
    }

    /**
     * stop
     */
    public void stop(){

        if(null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        remove();
    }

    /**
     * remove
     */
    public void remove(){
        MainApplication.getInstance().unregisterActivityLifecycleCallbacks(this);

        mDialog = null;
        mContext = null;
    }

    /**
     * set dismiss on pause
     */
    public void setDismissOnPause(boolean dismissOnPause){
        isDismissedOnPause = dismissOnPause;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        if(isDismissedOnPause && mContext == activity){
            stop();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if(mContext == activity){
            stop();
        }
    }
}
