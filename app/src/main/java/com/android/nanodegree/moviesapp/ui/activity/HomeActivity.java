package com.android.nanodegree.moviesapp.ui.activity;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;
import com.android.nanodegree.moviesapp.ui.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener {

    private static final String KEY_QUERY_TYPE = "key_query_type";
    private static final QueryType DEFAULT_QUERY_TYPE = QueryType.MOST_POPULAR;
    private static final int DEFAULT_QUERY_TITLE_RES_ID = R.string.most_popular;

    private HomeFragment mHomeFragment;
    private QueryType mCurrentQueryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(DEFAULT_QUERY_TITLE_RES_ID);
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_QUERY_TYPE) &&
                savedInstanceState.get(KEY_QUERY_TYPE) != null) {
            mCurrentQueryType = (QueryType) savedInstanceState.get(KEY_QUERY_TYPE);
        }
        if (getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, getHomeFragment())
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable(KEY_QUERY_TYPE, mCurrentQueryType);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentQueryType == DEFAULT_QUERY_TYPE) {
            super.onBackPressed();
        } else {
            getHomeFragment().loadMovies(DEFAULT_QUERY_TYPE);
        }
    }

    private HomeFragment getHomeFragment() {
        if (mHomeFragment == null) {
            mHomeFragment =
                    (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        }
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance(DEFAULT_QUERY_TYPE);
        }
        return mHomeFragment;
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (movie != null) {
            MovieDetailsActivity.startActivity(this, movie);
        }
    }

    @Override
    public void onQueryTypeChanged(QueryType newQueryType) {
        if (newQueryType != null) {
            switch (newQueryType) {
                case MOST_POPULAR:
                    setTitle(R.string.most_popular);
                    break;
                case TOP_RATED:
                    setTitle(R.string.top_rated);
                    break;
                case FAVORITES:
                    setTitle(R.string.favorites);
                    break;
            }
            mCurrentQueryType = newQueryType;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                getHomeFragment().loadMovies(QueryType.MOST_POPULAR);
                break;
            case R.id.action_top_rated:
                getHomeFragment().loadMovies(QueryType.TOP_RATED);
                break;
            case R.id.action_favorites:
                getHomeFragment().loadMovies(QueryType.FAVORITES);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
