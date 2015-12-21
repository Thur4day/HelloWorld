package com.dxz.helloworld;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by dxz on 15/12/21.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public Bundle mFragmentArgs;

    public View mFragmentView;
    public ActionBar mActionBar;
    private View mBack;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentArgs = getArguments();
        if (mFragmentArgs == null) {
            mFragmentArgs = new Bundle();
        }
        mActionBar = getActivity().getActionBar();
    }

    public Context getContext() {
        return getActivity() == null ? MainApplication.getInstance() : getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(getLayoutId(), container, false);
        initViews();
        return mFragmentView;
    }

    protected void initViews() {
        mBack = mFragmentView.findViewById(R.id.back);
//        mTitle = (TextView) mFragmentView.findViewById(R.id.tv_title);
        if (mBack != null) {
            mBack.setOnClickListener(this);
        }
    }

    protected void setTitleBarTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    protected void hideBack() {
        if (mBack != null) {
            mBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onDetach() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDetach();
    }

    protected abstract int getLayoutId();

}
