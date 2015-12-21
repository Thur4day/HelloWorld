package com.dxz.helloworld.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.hwand.pinhaowanr.utils.AndTools;

/**
 * Created by jake on 3/6/15.
 */
public class HiDialog extends Dialog implements HiDismissRequestContextLifeCycle.OnDismissImmeRequest{

    /** life cycle */
    private HiDismissRequestContextLifeCycle mLifeCycle = null;

    /** the context */
    private Activity mContext = null;

    /**
     * Constructor
     * @param context
     */
    public HiDialog(Context context) {
        super(context);
        mContext = (Activity)context;
    }

    /**
     * Constructor
     * @param context
     * @param theme
     */
    public HiDialog(Context context, int theme) {
        super(context, theme);
        mContext = (Activity)context;
    }

    /**
     * Constructor
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    protected HiDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = (Activity)context;
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
