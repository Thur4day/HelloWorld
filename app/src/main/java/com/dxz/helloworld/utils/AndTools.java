package com.dxz.helloworld.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dxz.helloworld.BaseActivity;
import com.dxz.helloworld.MainApplication;
import com.dxz.helloworld.R;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hanhanliu on 15/5/13.
 */
public class AndTools {

    public static final long SECOND = 1;
    public static final long MINUTE = 60;
    public static final long HOUR = MINUTE * MINUTE;
    public static final long DAY = HOUR * 24;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = MONTH * 12;

    // 上次toast的内容，避免重复弹同一个toast
    private static String lastToastContent;

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into dp
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float px2dpFloat(Context context, float px) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (null != context && context.getResources() != null && context.getResources().getDisplayMetrics() != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            float dp = px / scale + 0.5f;
            return dp;
        }
        return px;
    }

    /**
     * 返回浮点型的处理值
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static float dp2pxFloat(Context context, float dpValue) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (null != context && context.getResources() != null && context.getResources().getDisplayMetrics() != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (dpValue * scale + 0.5f);
        }

        return dpValue;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) dp2pxFloat(context, dpValue);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) px2dpFloat(context, pxValue);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context != null) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;

            return width;
        }
        return 0;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context != null) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            int height = dm.heightPixels;

            return height;
        }
        return 0;
    }

    /**
     * 获取屏幕dpi
     *
     * @param context
     * @return
     */
    public static int getDisplayMetricDensityDpi(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context != null) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            return dm.densityDpi;
        }
        return DisplayMetrics.DENSITY_HIGH;
    }

    /**
     * 返回NetworkInfo isAvailable
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        boolean netStatus = false;

        if (context != null) {
            ConnectivityManager connectManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();

            if (networkInfo != null) {
                netStatus = networkInfo.isAvailable();
            }
        }

        return netStatus;
    }

    /**
     * 返回NetworkInfo isConnected，如果是移动网络，根据通话情况和运营商信息做二次判断
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        boolean isConnected = false;

        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();

            if (null != ni) {
                isConnected = ni.isConnected();

                if (isConnected) {

                    int nType = ni.getType();
                    if (ConnectivityManager.TYPE_WIFI != nType) {

                        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                        if (TelephonyManager.CALL_STATE_IDLE != telManager.getCallState()) {
                            String imsi = telManager.getSubscriberId();

                            if (imsi != null) {

                                //移动，电信在通话的时候，没有网络
                                if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46003")) {
                                    isConnected = false;
                                }
                            }
                        }
                    }
                }

            }
        }

        return isConnected;
    }

    /**
     * 返回电话是否正在使用
     *
     * @param context
     * @return
     */
    public static boolean isPhoneUsing(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context == null) {
            return false;
        }
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_IDLE == telManager.getCallState()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 返回是否正在响铃
     *
     * @param context
     * @return
     */
    public static boolean isPhoneRinging(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context == null) {
            return false;
        }
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_RINGING == telManager.getCallState()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回versionCode
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        int verCode = -1;
        if (context != null) {
            try {
                verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return verCode;
    }

    /**
     * 返回versionName
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        String verName = "";
        if (context != null) {
            try {
                verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return verName;
    }

    /**
     * 显示键盘
     *
     * @param view
     */
    public static void showKeyboard(Context context, View view) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏键盘
     */
    public static void hideKeyboard(Context context, View view) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 获取PackageInfo
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context != null) {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            try {
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                return packageManager.getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 振动
     *
     * @param context
     * @param duration 振动的时间长度，单位为毫秒
     */
    public static void playVibrator(Context context, long duration) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        if (context != null) {
            Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
            if (vib != null) {
                vib.vibrate(duration);
            }
        }
    }

    /**
     * 当前网络是否是wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null
                    && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }

    /**
     * 当前网络是否是3g
     *
     * @param context
     * @return
     */
    public static boolean is3G(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }
        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return true; // ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;

            }
        }
        return false;
    }

    private final static String WIFI = "wifi";
    private final static String NET_4G = "4g";
    private final static String NET_3G = "3g";
    private final static String NET_2G = "2g";
    private final static String OTHER = "other";

    public static String getNetInfo(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null
                    && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return WIFI;
            }
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NET_2G;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NET_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return NET_4G;
                default:
                    return OTHER;
            }
        }
        return OTHER;
    }

    /***
     * 获得sdk版本
     **/
    public static int getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }


    public static String getAPNType(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }
        String netType = null;
        if (context != null) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
                return null;
            }

            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                    netType = "cmnet";
                } else {
                    netType = "cmwap";
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = "WIFI";
            }
        }

        return netType;
    }

    /**
     * 传resId
     *
     * @param resId
     */
    public static void showToast(int resId) {
        showToast(MainApplication.getInstance().getResources().getString(resId), Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT);
    }

    /**
     * 传toast string
     *
     * @param content
     */
    public static void showToast(String content) {
        showToast(content, Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT);
    }


    /**
     * 可设置toast位置
     *
     * @param content
     * @param gravity
     * @param offsetX
     * @param offsetY
     */
    public static void showToast(String content, int gravity, int offsetX, int offsetY) {
        showToast(content, gravity, offsetX, offsetY, Toast.LENGTH_SHORT);
    }

    /**
     * 可设置toast时长
     *
     * @param content
     * @param duration
     */
    public static void showToast(String content, int duration) {
        showToast(content, Gravity.CENTER, 0, 0, duration);
    }

    /**
     * 最终调用的toast方法，避免同一个string短时间内重复展示
     *
     * @param content
     * @param gravity
     * @param offsetX
     * @param offsetY
     * @param duration
     */

    private static Toast toast = null;

    public static void showToast(String content, int gravity, int offsetX, int offsetY, int duration) {

        if (toast == null) {
            toast = Toast.makeText(MainApplication.getInstance(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();


//        if(!TextUtils.isEmpty(content)){//如果字符串为空，没必要弹出toast。只在对应位子打debug log即可
//            Toast toast = Toast.makeText(PWApplication.getApplication(), content, duration);
//            toast.setGravity(gravity, offsetX, offsetY);
//            lastToastContent = content;
//            toast.show();
//        }
    }

    /**
     * 用户的os的sdk的版本是否大于等于指定的版本
     *
     * @param apiLevel
     * @return
     */
    public static boolean isCompatibleApiLevel(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    public static String getImsi(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        String imsi = null;
        if (context != null) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (null != mTelephonyMgr) {
                try {
                    imsi = mTelephonyMgr.getSubscriberId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (TextUtils.isEmpty(imsi)) {
                imsi = getImei(context);
            }
        }

        return imsi;
    }


    public static String getImei(Context context) {
        if (context == null) {
            context = MainApplication.getInstance();
        }

        String imei = null;

        if (null != context) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTelephonyMgr) {
                try {
                    imei = mTelephonyMgr.getDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (TextUtils.isEmpty(imei)) {
            imei = "imei";
        }
        return imei;
    }

    public static boolean getIsArtInUse() {
        final String vmVersion = System.getProperty("java.vm.version");
        return vmVersion != null && vmVersion.startsWith("2");
    }

    //国际化判断，以后如果有新的判断条件，在这里重构。
    public static boolean isChina() {
        return Locale.getDefault().getLanguage().equals(Locale.CHINA.getLanguage());
    }


    public static void removeWebKitCookies() {
        //清除webview缓存
        try {

            CookieManager.getInstance().removeAllCookie();
            CookieSyncManager.getInstance().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isActivityActive(Activity context) {

        boolean finished = true;
        if (null != context) {
            if (context instanceof BaseActivity) {

                BaseActivity ba = (BaseActivity) context;
                if (Build.VERSION.SDK_INT < 17) {
                    finished = ba.isFinishing();
                } else {
                    finished = ba.isDestroyed();
                }

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                finished = (context.isDestroyed() || context.isDestroyed());
            } else {
                if (Build.VERSION.SDK_INT < 17) {
                    finished = context.isFinishing();
                } else {
                    finished = context.isDestroyed();
                }
            }

        }

        return !finished;
    }

    public static boolean isActivitySafeForDialog(Activity context) {

        boolean finished = true;
        if (null != context) {
            if (context instanceof BaseActivity) {

                BaseActivity ba = (BaseActivity) context;
                if (Build.VERSION.SDK_INT < 17) {
                    finished = (ba.isFinishing() || !ba.isAttachedToWindow());
                } else {
                    finished = (ba.isDestroyed() || !ba.isAttachedToWindow());
                }

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                finished = (context.isDestroyed() || context.isDestroyed());
            } else {
                if (Build.VERSION.SDK_INT < 17) {
                    finished = context.isFinishing();
                } else {
                    finished = context.isDestroyed();
                }
            }
        }

        return !finished;
    }


    public static String getFileSize(long fileSize) {
        String fileSizeString = "";
        if (fileSize >= 1024 * 1024) {
            fileSizeString = String.valueOf(fileSize / (1024 * 1024)) + "MB";
        } else if (fileSize >= 1024) {
            fileSizeString = String.valueOf(fileSize / 1024) + "KB";
        } else {
            fileSizeString = String.valueOf(fileSize) + "B";
        }
        return fileSizeString;
    }

    public static void makeTimePickerNoInput(TimePickerDialog dialog) {
        if (null != dialog) {
            try {
                Class<TimePickerDialog> clazz = (Class<TimePickerDialog>) dialog.getClass();
                Field field = clazz.getDeclaredField("mTimePicker");

                if (null != field) {
                    field.setAccessible(true);

                    Object pickerObject = field.get(dialog);
                    if (null != pickerObject && pickerObject instanceof TimePicker) {
                        TimePicker picker = (TimePicker) pickerObject;
                        picker.setFocusable(false);
                        picker.setFocusableInTouchMode(false);
                        picker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void makeDatePickerNoInput(DatePickerDialog dialog) {
        if (null != dialog) {
            dialog.getDatePicker().setFocusableInTouchMode(false);
            dialog.getDatePicker().setFocusable(false);
            dialog.getDatePicker().setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }

    /**
     * 显示dialog
     *
     * @param context
     * @param title
     * @param message
     * @param indeterminate
     * @param cancelable
     * @return ProgressDialog
     * @Title: showProgress
     * @date 2013-10-14 下午3:46:21
     */
    public static ProgressDialog showProgress(Context context, CharSequence title,
                                              CharSequence message, boolean indeterminate, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);

        dialog.show();
        return dialog;
    }

    /**
     * 取消dialog
     *
     * @param loading
     * @Title: dismissDialog
     * @date 2013-10-14 下午3:46:21
     */
    public static void dismissDialog(ProgressDialog loading) {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
            loading.cancel();
        }
    }

    /**
     * 从本地缓存信息
     *
     * @return
     */
    public static String getCurrentData(Context context, String key) {
        String strT = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getString(key, "");
        return strT;
    }

    /**
     * 保存当前数据到缓存
     *
     */
    public static void saveCurrentData2Cache(Context context, String key, String data) {
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putString(key, data).commit();
    }

    public static String getStrTime(double lcc_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String re_StrTime = sdf.format(new Date((long) (lcc_time * 1000L)));
        return re_StrTime;
    }

    public static String convertTime(int time, Context context) {
        String timeFormat[] = context.getResources().getStringArray(R.array.time_array);

        long pastTime, nowTime, range;

        pastTime = Long.valueOf(time);
        nowTime = System.currentTimeMillis() / 1000;
        range = nowTime - pastTime;

        if (range < 0) {
            return timeFormat[timeFormat.length - 1];
        } else {
            if (range / YEAR >= 1) {
                return ((int) (range / YEAR) + timeFormat[0]);
            } else if (range / MONTH >= 1) {
                return ((int) range / MONTH + timeFormat[1]);
            } else if (range / DAY >= 1) {
                return ((int) range / DAY + timeFormat[2]);
            } else if (range / HOUR >= 1) {
                return ((int) range / HOUR + timeFormat[3]);
            } else if (range / MINUTE >= 1) {
                return ((int) range / MINUTE + timeFormat[4]);
            } else if (range / SECOND >= 1) {
                return ((int) range / SECOND + timeFormat[5]);
            }
        }
        return timeFormat[timeFormat.length - 1];
    }


    //输入限制字数
    public static void setTextLenght(final EditText editText, final int maxTextLenght, final TextView tv, final String hintText) {
        editText.addTextChangedListener(new TextWatcher() {

            CharSequence temp;
            int selectionStart;
            int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    editText.setHint(hintText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                int number = maxTextLenght - s.length();
                if (tv != null) {
                    tv.setText("(" + number + ")");
                }

                selectionStart = editText.getSelectionStart();
                selectionEnd = editText.getSelectionEnd();

                if (temp.length() > maxTextLenght) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    editText.setText(s);
                    editText.setSelection(tempSelection);
                }
            }
        });

    }

    public static String getStandardDate(long secondTime) {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat mSdf = new SimpleDateFormat("MM月dd日 HH:mm");
        String longTime = mSdf.format(new Date(secondTime * 1000));

        long time = System.currentTimeMillis() - (secondTime * 1000);
        long second = (long) Math.ceil(time / 1000);//秒前
        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(longTime);
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天前");
            } else {
                sb.append(hour + "小时前");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时前");
            } else {
                sb.append(minute + "分钟前");
            }
        } else if (second - 1 > 0) {
            if (second == 60) {
                sb.append("1分钟前");
            } else {
                sb.append(second + "秒前");
            }
        } else {
            sb.append("刚刚");
        }
        return sb.toString();
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * google maps的脚本里代码
     */
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

}
