package com.dxz.helloworld.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by dxz on 2015/12/28.
 */
public class OkHttpHelper {
    public static final String TAG = "OkHttpHelper";
    public static final long DEFAULT_MILLISECONDS = 10000;
    private static OkHttpHelper mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;


    private OkHttpHelper() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());


        if (true) {
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }


    }


    private boolean debug;
    private String tag;


    public OkHttpHelper debug(String tag) {
        debug = true;
        this.tag = tag;
        return this;
    }


    public static OkHttpHelper getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpHelper.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpHelper();
                }
            }
        }
        return mInstance;
    }


    public Handler getDelivery() {
        return mDelivery;
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


}