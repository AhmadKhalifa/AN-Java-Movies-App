package com.android.nanodegree.moviesapp.mvp;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
interface MVPPresenter<V extends MVPView> {

    /**
     * Called when an {@code MvpView} is attached to this presenter.
     * @param view The attached {@code MvpView}
     */
    void onAttach(V view);

    /**
     * Called when the view is resumed according to Android components
     * NOTE: this method will only be called for presenters that override it.
     */
    void onResume();

    /**
     * Called when an {@code MvpView} is detached from this presenter.
     */
    void onDetach();

}