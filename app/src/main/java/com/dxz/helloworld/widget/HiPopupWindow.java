package com.dxz.helloworld.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

import com.dxz.helloworld.utils.AndTools;


/**
 * Created by jake on 3/6/15.
 */
public class HiPopupWindow extends PopupWindow implements HiDismissRequestContextLifeCycle.OnDismissImmeRequest{

    /** life cycle */
    private HiDismissRequestContextLifeCycle mLifeCycle = null;

    /**
     * <p>Create a new empty, non focusable popup window of dimension (0,0).</p>
     *
     * <p>The popup does provide a background.</p>
     */
    public HiPopupWindow(Context context) {
        super(context);
    }

    /**
     * <p>Create a new empty, non focusable popup window of dimension (0,0).</p>
     *
     * <p>The popup does provide a background.</p>
     */
    public HiPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * <p>Create a new empty, non focusable popup window of dimension (0,0).</p>
     *
     * <p>The popup does provide a background.</p>
     */
    public HiPopupWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * <p>Create a new, empty, non focusable popup window of dimension (0,0).</p>
     *
     * <p>The popup does not provide a background.</p>
     */
    public HiPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * <p>Create a new empty, non focusable popup window of dimension (0,0).</p>
     *
     * <p>The popup does not provide any background. This should be handled
     * by the content view.</p>
     */
    public HiPopupWindow() {
    }

    /**
     * <p>Create a new non focusable popup window which can display the
     * <tt>contentView</tt>. The dimension of the window are (0,0).</p>
     *
     * <p>The popup does not provide any background. This should be handled
     * by the content view.</p>
     *
     * @param contentView the popup's content
     */
    public HiPopupWindow(View contentView) {
        super(contentView);
    }

    /**
     * <p>Create a new empty, non focusable popup window. The dimension of the
     * window must be passed to this constructor.</p>
     *
     * <p>The popup does not provide any background. This should be handled
     * by the content view.</p>
     *
     * @param width the popup's width
     * @param height the popup's height
     */
    public HiPopupWindow(int width, int height) {
        super(width, height);
    }

    /**
     * <p>Create a new non focusable popup window which can display the
     * <tt>contentView</tt>. The dimension of the window must be passed to
     * this constructor.</p>
     *
     * <p>The popup does not provide any background. This should be handled
     * by the content view.</p>
     *
     * @param contentView the popup's content
     * @param width the popup's width
     * @param height the popup's height
     */
    public HiPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    /**
     * <p>Create a new popup window which can display the <tt>contentView</tt>.
     * The dimension of the window must be passed to this constructor.</p>
     *
     * <p>The popup does not provide any background. This should be handled
     * by the content view.</p>
     *
     * @param contentView the popup's content
     * @param width the popup's width
     * @param height the popup's height
     * @param focusable true if the popup can be focused, false otherwise
     */
    public HiPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    /**
     * get activity
     */
    private Activity getActivity(){
        Activity activity = null;

        View view = getContentView();
        if(null != view){
            Context context = view.getContext();
            if(null != context && context instanceof Activity){
                activity = (Activity)context;
            }
        }

        return activity;
    }

    /**
     * is context running
     */
    private boolean isContextRunning(){
        return AndTools.isActivitySafeForDialog(getActivity());
    }

    /**
     * <p>
     * Display the content view in a popup window at the specified location. If the popup window
     * cannot fit on screen, it will be clipped. See {@link android.view.WindowManager.LayoutParams}
     * for more information on how gravity and the x and y parameters are related. Specifying
     * a gravity of {@link android.view.Gravity#NO_GRAVITY} is similar to specifying
     * <code>Gravity.LEFT | Gravity.TOP</code>.
     * </p>
     *
     * @param parent a parent view to get the {@link android.view.View#getWindowToken()} token from
     * @param gravity the gravity which controls the placement of the popup window
     * @param x the popup's x location offset
     * @param y the popup's y location offset
     */
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if(isContextRunning()){
            super.showAtLocation(parent, gravity, x, y);

            makeSureLifeCycleObserver();
            mLifeCycle.start(getActivity(), this);
        }
    }

    /**
     * <p>Display the content view in a popup window anchored to the bottom-left
     * corner of the anchor view. If there is not enough room on screen to show
     * the popup in its entirety, this method tries to find a parent scroll
     * view to scroll. If no parent scroll view can be scrolled, the bottom-left
     * corner of the popup is pinned at the top left corner of the anchor view.</p>
     *
     * @param anchor the view on which to pin the popup window
     *
     * @see #dismiss()
     */
    public void showAsDropDown(View anchor) {
        if(isContextRunning()){
            super.showAsDropDown(anchor);

            makeSureLifeCycleObserver();
            mLifeCycle.start(getActivity(), this);
        }
    }

    /**
     * <p>Display the content view in a popup window anchored to the bottom-left
     * corner of the anchor view offset by the specified x and y coordinates.
     * If there is not enough room on screen to show
     * the popup in its entirety, this method tries to find a parent scroll
     * view to scroll. If no parent scroll view can be scrolled, the bottom-left
     * corner of the popup is pinned at the top left corner of the anchor view.</p>
     * <p>If the view later scrolls to move <code>anchor</code> to a different
     * location, the popup will be moved correspondingly.</p>
     *
     * @param anchor the view on which to pin the popup window
     * @param xoff A horizontal offset from the anchor in pixels
     * @param yoff A vertical offset from the anchor in pixels
     *
     * @see #dismiss()
     */
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if(isContextRunning()){
            super.showAsDropDown(anchor, xoff, yoff);

            makeSureLifeCycleObserver();
            mLifeCycle.start(getActivity(), this);
        }
    }

    /**
     * <p>Display the content view in a popup window anchored to the bottom-left
     * corner of the anchor view offset by the specified x and y coordinates.
     * If there is not enough room on screen to show
     * the popup in its entirety, this method tries to find a parent scroll
     * view to scroll. If no parent scroll view can be scrolled, the bottom-left
     * corner of the popup is pinned at the top left corner of the anchor view.</p>
     * <p>If the view later scrolls to move <code>anchor</code> to a different
     * location, the popup will be moved correspondingly.</p>
     *
     * @param anchor the view on which to pin the popup window
     * @param xoff A horizontal offset from the anchor in pixels
     * @param yoff A vertical offset from the anchor in pixels
     * @param gravity Alignment of the popup relative to the anchor
     *
     * @see #dismiss()
     */
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if(isContextRunning()){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                super.showAsDropDown(anchor, xoff, yoff, gravity);

                makeSureLifeCycleObserver();
                mLifeCycle.start(getActivity(), this);
            }
        }
    }

    @Override
    public void dismiss() {

        super.dismiss();

        if(null != mLifeCycle){
            mLifeCycle.remove();
        }
    }

    private void makeSureLifeCycleObserver(){
        if(null == mLifeCycle){
            mLifeCycle = new HiDismissRequestContextLifeCycle();
        }
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
