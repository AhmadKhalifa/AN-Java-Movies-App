package com.android.nanodegree.moviesapp.presenter;

import com.android.nanodegree.moviesapp.model.QueryType;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
interface IHomePresenter {

    void getMovies(QueryType queryType);
}
