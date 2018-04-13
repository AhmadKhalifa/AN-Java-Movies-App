package com.android.nanodegree.moviesapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.mvp.BasePresenter;
import com.android.nanodegree.moviesapp.mvp.MVPView;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements MVPView {

    private P mPresenter;
    protected View mRootView;
    protected Snackbar mSnackBar;

    protected abstract P createPresenter();

    protected @NonNull View getRootView() {
        if (mRootView == null) {
            throw new IllegalStateException(
                    "Root view cannot be null. " +
                            "It MUST hold the root view created in the onCreateView method"
            );
        }
        return mRootView;
    }

    protected @NonNull P getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter == null) {
            throw new IllegalStateException("createPresenter() implementation returns null!");
        }
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BasePresenter basePresenter = getPresenter();
        basePresenter.onAttach(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDetach();
    }

    protected void showMessage(int messageResId) {
        showMessage(MoviesApp.getStringRes(messageResId));
    }

    protected void showMessage(String message) {
        if (message != null) {
            if (mSnackBar != null) {
                mSnackBar.dismiss();
            }
            mSnackBar = Snackbar.make(getRootView(), message, Snackbar.LENGTH_SHORT);
            mSnackBar.show();
        }
    }

    protected void showMessage(Snackbar snackBar) {
        if (snackBar != null) {
            if (mSnackBar != null) {
                mSnackBar.dismiss();
            }
            mSnackBar = snackBar;
            mSnackBar.show();
        }
    }

    protected void dismissMessagesIfAny() {
        if (mSnackBar != null) {
            mSnackBar.dismiss();
        }
    }
}
