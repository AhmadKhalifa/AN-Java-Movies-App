package com.android.nanodegree.moviesapp.data.dummy;

import com.android.nanodegree.moviesapp.util.BaseConnectionChecker;

/**
 * Created by Khalifa on 25/03/2018.
 *
 */
public class DummyConnectionChecker implements BaseConnectionChecker {

    private final boolean mIsNetworkAvailable;

    public DummyConnectionChecker(boolean mIsNetworkAvailable) {
        this.mIsNetworkAvailable = mIsNetworkAvailable;
    }

    @Override
    public boolean isNetworkAvailable() {
        return mIsNetworkAvailable;
    }
}
