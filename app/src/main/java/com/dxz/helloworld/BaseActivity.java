package com.dxz.helloworld;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dxz on 15/12/21.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public final ArrayList<LifeCycleListener> mListeners = new ArrayList<LifeCycleListener>();

    protected List<Activity> mActivityStatck = new ArrayList<Activity>();

    private boolean mIsWindowAttached = false;

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class BaseHandler extends Handler {
        private final WeakReference<BaseActivity> mActivity;

        public BaseHandler(BaseActivity activity) {
            mActivity = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mActivity.get();
            if (activity != null) {
                // ...
                activity.handleMessage(msg);
            }
        }
    }


    protected void handleMessage(Message msg) {

    }

    protected final BaseHandler mHandler = new BaseHandler(this);


    protected void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    protected void sendEmptyMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    protected void sendEmptyDelayMessage(int what, long delay) {
        mHandler.sendEmptyMessageDelayed(what, delay);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackClick();
                break;
        }
    }

    public interface LifeCycleListener {
        void onActivityCreated(BaseActivity activity);

        void onActivityDestroyed(BaseActivity activity);

        void onActivityStarted(BaseActivity activity);

        void onActivityStopped(BaseActivity activity);

        void onActivityResumed(BaseActivity activity);

        void onActivityPaused(BaseActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {
        public void onActivityCreated(BaseActivity activity) {
        }

        public void onActivityDestroyed(BaseActivity activity) {
        }

        public void onActivityStarted(BaseActivity activity) {
        }

        public void onActivityStopped(BaseActivity activity) {
        }

        @Override
        public void onActivityResumed(BaseActivity activity) {

        }

        @Override
        public void onActivityPaused(BaseActivity activity) {

        }
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        mListeners.remove(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityCreated(this);
        }
        mActivityStatck.add(this);

        MainApplication.getInstance().addActivity(this);
        setupStateBar(this);

    }


    protected void onBackClick() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsWindowAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsWindowAttached = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityStatck.remove(this);
        MainApplication.getInstance().removeActivity(this);
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityDestroyed(this);
        }

    }

    public boolean isAttachedToWindow() {
        return mIsWindowAttached;
    }

    public void clearAllActivity() {
        for (Activity activity : mActivityStatck) {
            activity.finish();
        }
        mActivityStatck.clear();
    }

    /**
     * 解决viewPager下无响应
     * 需要调用root Fragment的startForResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }


    }

    /**
     * 递归调用，对所有子Fragment生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    @SuppressLint("NewApi")
    protected void setupStateBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.statuBar_color));
        } else if (Build.VERSION.SDK_INT >= 19) {
            Window window = activity.getWindow();
            int flags = window.getAttributes().flags;
            if ((flags | WindowManager.LayoutParams.FLAG_FULLSCREEN) != flags) {
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                int height = getStatusBarHeight(activity);
                View contentView = window
                        .findViewById(Window.ID_ANDROID_CONTENT);
//                contentView.setBackgroundColor(getResources().getColor(R.color.statuBar_color));
                contentView.setPadding(0, height, 0, 0);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                View contentView = window
                        .findViewById(Window.ID_ANDROID_CONTENT);
//                contentView.setBackgroundColor(getResources().getColor(R.color.statuBar_color));
                contentView.setPadding(0, 0, 0, 0);
            }
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    protected int getStatusBarHeight(Context context) {

        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            int y = context.getResources().getDimensionPixelSize(x);
            return y;
        } catch (Exception e) {
            e.printStackTrace();
            return (int) (context.getResources().getDisplayMetrics().density * 20 + 0.5);
        }
    }
}
