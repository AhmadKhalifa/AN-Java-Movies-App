package com.android.nanodegree.moviesapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.data.repository.movies.local.LocalMoviesRepository;
import com.android.nanodegree.moviesapp.data.repository.movies.remote.RetrofitRemoteMoviesRepository;
import com.android.nanodegree.moviesapp.data.storage.handler.MoviesContentProviderHandler;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;
import com.android.nanodegree.moviesapp.mvp.home.HomePresenter;
import com.android.nanodegree.moviesapp.ui.adapter.HomeMoviesAdapter;
import com.android.nanodegree.moviesapp.util.BaseConnectionChecker;
import com.android.nanodegree.moviesapp.mvp.home.HomeView;
import com.android.nanodegree.moviesapp.util.ConnectionChecker;
import com.android.nanodegree.moviesapp.util.DimensionsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<HomePresenter>
        implements HomeView, HomeMoviesAdapter.OnItemInteractionListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private static final int LIST_ITEM_MINIMUM_WIDTH_IN_DP = 140;

    private static final String KEY_QUERY_TYPE = "key_query_type";
    private static final String KEY_FAVORITE_MOVIES_IDS_SET = "ket_favorite_movies_ids_set";
    private static final String KEY_ALL_MOVIES_MAP = "key_all_movies_map";

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.ll_error_layout)
    LinearLayout mErrorLayout;

    @BindView(R.id.iv_error_icon)
    ImageView mErrorImageView;

    @BindView(R.id.tv_no_movies)
    TextView mErrorTextView;

    @BindView(R.id.tv_tab_to_try_again)
    TextView mTabToTryAgainTextView;

    @BindView(R.id.pb_loading_movies)
    ProgressBar mLoadingMoviesProgressBar;

    private QueryType mCurrentQueryType;
    private HashMap<QueryType, List<Movie>> mQueryTypeListMap;
    private HomeMoviesAdapter mHomeMoviesAdapter;
    private OnFragmentInteractionListener mFragmentInteractionListener;
    private HashSet<Long> mFavoriteMoviesIdsSet;
    private BaseConnectionChecker mConnectionChecker;

    private View.OnClickListener mTryAgainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mCurrentQueryType != null) {
                getPresenter().loadMovies(mCurrentQueryType, true);
            }
        }
    };

    public static HomeFragment newInstance(QueryType queryType) {
        if (queryType == null) {
            queryType = QueryType.MOST_POPULAR;
        }
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_QUERY_TYPE, queryType);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public HomeFragment() {
        mQueryTypeListMap = new HashMap<>();
        mQueryTypeListMap.put(QueryType.MOST_POPULAR, new ArrayList<Movie>());
        mQueryTypeListMap.put(QueryType.TOP_RATED, new ArrayList<Movie>());
        mQueryTypeListMap.put(QueryType.FAVORITES, new ArrayList<Movie>());
        mConnectionChecker = new ConnectionChecker();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHomeMoviesAdapter = new HomeMoviesAdapter(this);
        mMoviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMoviesRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), getGridSpanCount())
        );
        mMoviesRecyclerView.setAdapter(mHomeMoviesAdapter);
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_QUERY_TYPE) &&
                savedInstanceState.get(KEY_QUERY_TYPE) != null &&
                savedInstanceState.containsKey(KEY_FAVORITE_MOVIES_IDS_SET) &&
                savedInstanceState.getSerializable(KEY_FAVORITE_MOVIES_IDS_SET) != null &&
                savedInstanceState.containsKey(KEY_ALL_MOVIES_MAP) &&
                savedInstanceState.getSerializable(KEY_ALL_MOVIES_MAP) != null) {
            mCurrentQueryType = (QueryType) savedInstanceState.get(KEY_QUERY_TYPE);
            mFavoriteMoviesIdsSet = (HashSet<Long>) savedInstanceState
                    .getSerializable(KEY_FAVORITE_MOVIES_IDS_SET);
            mQueryTypeListMap = (HashMap<QueryType, List<Movie>>) savedInstanceState
                    .getSerializable(KEY_ALL_MOVIES_MAP);
            if (mQueryTypeListMap != null &&
                    mCurrentQueryType != null &&
                    mQueryTypeListMap.get(mCurrentQueryType) != null) {
                updateList(mQueryTypeListMap.get(mCurrentQueryType));
            } else {
                loadMovies(QueryType.MOST_POPULAR);
            }
        } else if (getArguments() != null &&
                getArguments().containsKey(KEY_QUERY_TYPE) &&
                getArguments().get(KEY_QUERY_TYPE) != null) {
            loadMovies((QueryType) getArguments().get(KEY_QUERY_TYPE));
        } else {
            loadMovies(QueryType.MOST_POPULAR);
        }
    }

    @Override
    public void onResume() {
        getPresenter().loadFavoriteMoviesIds(true);
        if (mCurrentQueryType == QueryType.FAVORITES) {
            getPresenter().loadMovies(QueryType.FAVORITES);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_QUERY_TYPE, mCurrentQueryType);
        outState.putSerializable(KEY_FAVORITE_MOVIES_IDS_SET, mFavoriteMoviesIdsSet);
        outState.putSerializable(KEY_ALL_MOVIES_MAP, mQueryTypeListMap);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new IllegalStateException(
                    context.getClass().getSimpleName() + " must implement " +
                            "MovieDetailsFragment.OnFragmentInteractionListener interface"
            );
        }
    }

    @Override
    public void onDetach() {
        mFragmentInteractionListener = null;
        super.onDetach();
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(
                new LocalMoviesRepository(new MoviesContentProviderHandler()),
                new RetrofitRemoteMoviesRepository(
                        mConnectionChecker,
                        MoviesApp.getStringRes(R.string.tmdb_api_key),
                        MoviesApp.getStringRes(R.string.tmdb_api_base_url)
                )
        );
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (movie != null && mFragmentInteractionListener != null) {
            mFragmentInteractionListener.onMovieClick(movie);
        }
    }

    @Override
    public void onAddMovieToFavorites(Movie movie) {
        if (movie != null) {
            getPresenter().addToFavorites(movie);
        }
    }

    @Override
    public void onRemoveMovieFromFavorites(Movie movie) {
        if (movie != null) {
            getPresenter().removeFromFavorites(movie);
        }
    }

    @Override
    public void onError(Error error) {
        switch (error) {
            case ERROR_LOADING_FAVORITE_MOVIES:
            case ERROR_ADDING_MOVIE_TO_FAVORITES:
            case ERROR_REMOVING_MOVIE_FROM_FAVORITES:
                showMessage(error.getStringResId());
                break;
            case NO_INTERNET_CONNECTION:
            case NON_STABLE_CONNECTION:
                showErrorLayout(
                        R.drawable.ic_cloud_off_white_48px,
                        error.getStringResId(),
                        true
                );
                break;
            case GENERAL_ERROR:
            default:
                showErrorLayout(
                        R.drawable.ic_sentiment_very_dissatisfied_white_48px,
                        error.getStringResId(),
                        true
                );
                break;
        }

    }

    @Override
    public void onMoviesRetrieved(QueryType queryType,
                                  List<Movie> movies,
                                  boolean isRemotelyLoaded) {
        if (queryType != null && movies != null) {
            mQueryTypeListMap.put(queryType, movies);
            if (mCurrentQueryType == queryType) {
                updateList(movies);
                if (queryType != QueryType.FAVORITES &&
                        !isRemotelyLoaded &&
                        mConnectionChecker.isNetworkAvailable()) {
                    showMessage(Snackbar.make(
                            getRootView(),
                            R.string.refreshing_list,
                            Snackbar.LENGTH_INDEFINITE
                    ));
                    getPresenter().loadMovies(queryType, true);
                }
            }
        } else {
            mMoviesRecyclerView.setVisibility(View.GONE);
            mLoadingMoviesProgressBar.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
            showErrorLayout(
                    R.drawable.ic_sentiment_very_dissatisfied_white_48px,
                    Error.GENERAL_ERROR.getStringResId(),
                    true
            );
        }
    }

    @Override
    public void onFavoriteMoviesIdsRetrieved(HashSet<Long> favoriteMoviesIdsSet) {
        if (favoriteMoviesIdsSet != null) {
            mFavoriteMoviesIdsSet = favoriteMoviesIdsSet;
            mHomeMoviesAdapter.updateFavoriteMoviesIdsSet(favoriteMoviesIdsSet);
        }
    }

    @Override
    public void onAddToFavoritesSuccess(Movie movie) {
        mFavoriteMoviesIdsSet.add(movie.getId());
        mQueryTypeListMap.get(QueryType.FAVORITES).add(movie);
        updateFavoriteMoviesSet(mFavoriteMoviesIdsSet);
        showMessage(MoviesApp.getStringRes(R.string.added_to_favorites));
        getPresenter().loadFavoriteMoviesIds();
    }

    @Override
    public void onRemoveFromFavoritesSuccess(Movie movie) {
        mFavoriteMoviesIdsSet.remove(movie.getId());
        mQueryTypeListMap.get(QueryType.FAVORITES).remove(movie);
        updateFavoriteMoviesSet(mFavoriteMoviesIdsSet);
        showMessage(MoviesApp.getStringRes(R.string.removed_from_favorites));
        getPresenter().loadFavoriteMoviesIds();
        if (mCurrentQueryType == QueryType.FAVORITES && mFavoriteMoviesIdsSet.isEmpty()) {
            showErrorLayout(
                    R.drawable.ic_theaters_white_48px,
                    R.string.nothing_to_show,
                    false
            );
        }
    }

    private void updateList(List<Movie> movies) {
        if (movies != null && mHomeMoviesAdapter != null) {
            dismissMessagesIfAny();
            if (movies.isEmpty()) {
                showErrorLayout(
                        R.drawable.ic_theaters_white_48px,
                        R.string.nothing_to_show,
                        false
                );
            } else {
                mMoviesRecyclerView.setVisibility(View.VISIBLE);
                mLoadingMoviesProgressBar.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.GONE);
                mHomeMoviesAdapter.updateList(movies);
            }
        }
    }

    private void updateFavoriteMoviesSet(HashSet<Long> favoriteMoviesIdsSet) {
        mFavoriteMoviesIdsSet = favoriteMoviesIdsSet;
        mHomeMoviesAdapter.updateFavoriteMoviesIdsSet(mFavoriteMoviesIdsSet);
    }

    public void loadMovies(QueryType queryType) {
        if (queryType != null) {
            List<Movie> movies = mQueryTypeListMap.get(queryType);
            updateQueryType(queryType);
            if (mCurrentQueryType != QueryType.FAVORITES && movies != null && !movies.isEmpty()) {
                updateList(movies);
            } else {
                mMoviesRecyclerView.setVisibility(View.GONE);
                mLoadingMoviesProgressBar.setVisibility(View.VISIBLE);
                mErrorLayout.setVisibility(View.GONE);
                getPresenter().loadMovies(queryType);
            }
        }
    }

    private void updateQueryType(QueryType queryType) {
        mCurrentQueryType = queryType;
        if (mFragmentInteractionListener != null) {
            mFragmentInteractionListener.onQueryTypeChanged(queryType);
        }
    }

    private void showErrorLayout(int imageResId, int messageResId, boolean showTryAgainText) {
        mLoadingMoviesProgressBar.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.GONE);
        mErrorImageView.setImageResource(imageResId);
        mErrorTextView.setText(messageResId);
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setOnClickListener(showTryAgainText ? mTryAgainOnClickListener : null);
        mTabToTryAgainTextView.setVisibility(showTryAgainText ? View.VISIBLE : View.GONE);
    }

    @OnClick({
            R.id.ll_error_layout
    })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_error_layout:

                break;
        }
    }

    public int getGridSpanCount() {
        return DimensionsUtil.getGridSpanCount(LIST_ITEM_MINIMUM_WIDTH_IN_DP);
    }

    public interface OnFragmentInteractionListener {

        void onMovieClick(Movie movie);

        void onQueryTypeChanged(QueryType newQueryType);
    }
}
