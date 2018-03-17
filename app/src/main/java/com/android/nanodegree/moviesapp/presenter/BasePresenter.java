package com.android.nanodegree.moviesapp.presenter;


import com.android.nanodegree.moviesapp.util.RxWrapper;
import com.android.nanodegree.moviesapp.view.MVPView;

import java.lang.ref.WeakReference;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class BasePresenter<V extends MVPView> extends RxWrapper implements MVPPresenter<V> {

    private WeakReference<V> mViewReference;

    @Override
    public void onAttach(V view) {
        mViewReference = new WeakReference<>(view);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDetach() {
        if (mViewReference != null) {
            clearSubscriptions();
            mViewReference.clear();
            mViewReference = null;
        }
    }

    protected boolean isViewAttached() {
        return mViewReference != null && mViewReference.get() != null;
    }

    protected V getView() {
        return mViewReference != null ? mViewReference.get() : null;
    }
}
