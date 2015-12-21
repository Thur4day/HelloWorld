/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dxz.helloworld.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;

import com.dxz.helloworld.utils.AndTools;


/**
 * A subclass of Dialog that can display one, two or three buttons. If you only want to
 * display a String in this dialog box, use the setMessage() method.  If you
 * want to display a more complex view, look up the FrameLayout called "custom"
 * and add your view to it:
 * <p/>
 * <pre>
 * FrameLayout fl = (FrameLayout) findViewById(android.R.id.custom);
 * fl.addView(myView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
 * </pre>
 * <p/>
 * <p>The AlertDialog class takes care of automatically setting
 * {@link WindowManager.LayoutParams#FLAG_ALT_FOCUSABLE_IM
 * WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM} for you based on whether
 * any views in the dialog return true from {@link View#onCheckIsTextEditor()
 * View.onCheckIsTextEditor()}.  Generally you want this set for a Dialog
 * without text editors, so that it will be placed on top of the current
 * input method UI.  You can modify this behavior by forcing the flag to your
 * desired mode after calling onCreate.
 * <p/>
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating dialogs, read the
 * <a href="{@docRoot}guide/topics/ui/dialogs.html">Dialogs</a> developer guide.</p>
 * </div>
 */
public class HiAlertDialog {

    public static class Builder extends AlertDialog.Builder {

        /**
         * the dialog
         */
        private AlertDialog mDialog = null;

        /**
         * life cycle
         */
        private HiAlertDialogContextLifeCycle mLifeCycle = null;

        /**
         * the on dismiss listener
         */
        private AlertDialog.OnDismissListener mOnDismissListener = null;

        /**
         * context
         */
        private Activity mContext = null;

        /**
         * Constructor using a context for this builder and the {@link AlertDialog} it creates.
         */
        public Builder(Context context) {
            super(context);

            mContext = (Activity) context;
            mLifeCycle = new HiAlertDialogContextLifeCycle();
        }

        /**
         * Constructor using a context and theme for this builder and
         * the AlertDialog it creates.  The actual theme
         * that an AlertDialog uses is a private implementation, however you can
         * here supply either the name of an attribute in the theme from which
         * to get the dialog's style (such as {@link android.R.attr#alertDialogTheme}
         * or one of the constants
         * AlertDialog#THEME_TRADITIONAL AlertDialog.THEME_TRADITIONAL,
         * AlertDialog#THEME_HOLO_DARK AlertDialog.THEME_HOLO_DARK or
         * AlertDialog#THEME_HOLO_LIGHT AlertDialog.THEME_HOLO_LIGHT
         */
        public Builder(Context context, int theme) {
            super(context, theme);

            mContext = (Activity) context;
            mLifeCycle = new HiAlertDialogContextLifeCycle();
        }

        /**
         * create a dialog
         *
         * @return
         */
        public AlertDialog create() {

            if (null == mDialog) {
                mDialog = super.create();

                mDialog.setOnDismissListener(new AlertDialog.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        if (null != mLifeCycle) {
                            mLifeCycle.remove();
                        }

                        if (null != mOnDismissListener) {
                            mOnDismissListener.onDismiss(dialog);
                        }
                    }
                });
            }

            return mDialog;
        }

        /**
         * Creates a AlertDialog with the arguments supplied to this builder and
         * {@link Dialog#show()}'s the dialog.
         */
        public AlertDialog show() {

            AlertDialog dialog = create();

            if (AndTools.isActivitySafeForDialog(mContext)) {
                dialog.show();
                mLifeCycle.start(mContext, dialog);
            }

            return dialog;
        }

        /**
         * dismiss dialog
         */
        public AlertDialog dismiss() {

            if (null != mLifeCycle) {
                mLifeCycle.stop();
            }

            return mDialog;
        }

        /**
         * is showing
         */
        public boolean isShowing() {
            return (null != mDialog && mDialog.isShowing());
        }

        /**
         * set the on dismiss listener
         */
        public Builder setOnDismissListener(AlertDialog.OnDismissListener listener) {
            mOnDismissListener = listener;
            return this;
        }

        /**
         * set dismiss on paused
         */
        public Builder setDismissOnPaused(boolean flag) {
            mLifeCycle.setDismissOnPause(flag);
            return this;
        }

    }
}
