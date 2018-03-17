package com.android.nanodegree.moviesapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.nanodegree.moviesapp.presenter.BasePresenter;
import com.android.nanodegree.moviesapp.view.MVPView;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements MVPView {

    private P mPresenter;

    protected abstract P createPresenter();

    protected @NonNull P getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter == null) {
            throw new IllegalStateException("createPresenter() implementation returns null!");
        }
        return mPresenter;
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
}
