package com.dxz.helloworld.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.hwand.pinhaowanr.utils.AndTools;

/**
 * Created by jake on 3/6/15.
 */
public class HiProgressDialog extends ProgressDialog implements HiDismissRequestContextLifeCycle.OnDismissImmeRequest{

    /** life cycle */
    private HiDismissRequestContextLifeCycle mLifeCycle = null;

    /** the context */
    private Activity mContext = null;

    /**
     * Constructor
     * @param context
     */
    public HiProgressDialog(Context context) {
        super(context);
        mContext = (Activity)context;
    }

    /**
     * Constructor
     * @param context
     * @param theme
     */
    public HiProgressDialog(Context context, int theme) {
        super(context, theme);
        mContext = (Activity)context;
    }

    public static HiProgressDialog show(Context context, CharSequence title,
                                      CharSequence message) {
        return show(context, title, message, false);
    }

    public static HiProgressDialog show(Context context, CharSequence title,
                                      CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static HiProgressDialog show(Context context, CharSequence title,
                                      CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static HiProgressDialog show(Context context, CharSequence title,
                                      CharSequence message, boolean indeterminate,
                                      boolean cancelable, OnCancelListener cancelListener) {
        HiProgressDialog dialog = new HiProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    /**
     * show
     */
    public void show(){
        if(AndTools.isActivitySafeForDialog(mContext)){
            super.show();

            if(null == mLifeCycle){
                mLifeCycle = new HiDismissRequestContextLifeCycle();
            }

            mLifeCycle.start(mContext, this);
        }
    }

    /**
     * dismiss
     */
    public void dismiss(){

        if(null != mLifeCycle){
            mLifeCycle.remove();
        }

        super.dismiss();
    }

    /**
     * this method is called for case that activity is destroying but the dialog is still showing, so dismiss it directly
     * some times user do animation in the dismiss function, in order to make sure it does not crash, so donot call the dismiss instead of super.dismiss
     */
    @Override
    public final void onDismissRequest() {
        super.dismiss();
    }
}
