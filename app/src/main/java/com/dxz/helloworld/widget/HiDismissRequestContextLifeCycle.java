package com.dxz.helloworld.widget;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.dxz.helloworld.MainApplication;


/**
 * Created by jake on 3/6/15.
 */
public class HiDismissRequestContextLifeCycle implements Application.ActivityLifecycleCallbacks {

    /**
     * the activity
     */
    private Activity mContext = null;

    /**
     * the dialog
     */
    private OnDismissImmeRequest mDismissRequester = null;

    /**
     * is dismiss on pause
     */
    private boolean isDismissedOnPause = false;

    /**
     * the on dismiss listener
     */
    public interface OnDismissImmeRequest {

        /**
         * dismiss immeidately
         */
        public void onDismissRequest();
    }

    /**
     * start
     */
    public void start(Activity activity, OnDismissImmeRequest request) {

        remove();

        mContext = activity;
        mDismissRequester = request;

        if (null != mContext && null != mDismissRequester) {
            MainApplication.getInstance().registerActivityLifecycleCallbacks(this);
        }
    }

    /**
     * stop
     */
    public void stop() {

        if (null != mDismissRequester) {
            mDismissRequester.onDismissRequest();
        }

        remove();
    }

    /**
     * remove
     */
    public void remove() {
        MainApplication.getInstance().unregisterActivityLifecycleCallbacks(this);

        mDismissRequester = null;
        mContext = null;
    }

    /**
     * set dismiss on pause
     */
    public void setDismissOnPause(boolean dismissOnPause) {
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
        if (isDismissedOnPause && mContext == activity) {
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
        if (mContext == activity) {
            stop();
        }
    }

}
