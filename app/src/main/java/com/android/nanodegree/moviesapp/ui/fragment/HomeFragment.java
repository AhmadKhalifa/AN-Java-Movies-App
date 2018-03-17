package com.android.nanodegree.moviesapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.data.repository.home.local.SQLLocalHomeRepository;
import com.android.nanodegree.moviesapp.data.repository.home.remote.RetrofitRemoteHomeRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;
import com.android.nanodegree.moviesapp.presenter.HomePresenter;
import com.android.nanodegree.moviesapp.view.Error;
import com.android.nanodegree.moviesapp.view.HomeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView {

    @BindView(R.id.text)
    TextView mTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(
                new SQLLocalHomeRepository(),
                new RetrofitRemoteHomeRepository(MoviesApp.getStringRes(R.string.api_key))
        );
    }

    @OnClick(R.id.load_button)
    void loadMovies() {
        getPresenter().getMovies(QueryType.MOST_POPULAR);
    }

    @Override
    public void onLocalMoviesRetrieved(QueryType queryType, List<Movie> cachedMovies) {
        mTextView.setText("local " + queryType.toString() + cachedMovies.size());
    }

    @Override
    public void onRemoteMoviesRetrieved(QueryType queryType, List<Movie> remoteMovies) {
        mTextView.setText("remote " + queryType.toString() + remoteMovies.size());
    }

    @Override
    public void onLoadMoviesFailure(Error error) {
        mTextView.setText(error.toString());
    }
}
