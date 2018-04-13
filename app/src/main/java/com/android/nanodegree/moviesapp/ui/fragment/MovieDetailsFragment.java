package com.android.nanodegree.moviesapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.data.repository.movies.local.SQLiteLocalMoviesRepository;
import com.android.nanodegree.moviesapp.data.repository.movies.remote.RetrofitRemoteMoviesRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.mvp.movieDetails.MovieDetailsPresenter;
import com.android.nanodegree.moviesapp.mvp.movieDetails.MovieDetailsView;
import com.android.nanodegree.moviesapp.ui.recycler.MovieReviewsAdapter;
import com.android.nanodegree.moviesapp.ui.recycler.MovieTrailersAdapter;
import com.android.nanodegree.moviesapp.util.ConnectionChecker;
import com.android.nanodegree.moviesapp.util.DateFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Khalifa on 03/04/2018.
 *
 */
public class MovieDetailsFragment extends BaseFragment<MovieDetailsPresenter>
        implements MovieDetailsView, MovieTrailersAdapter.OnItemInteractionListener {

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();

    private static final String KEY_MOVIE = "key_movie";
    private static final String KEY_TRAILERS = "key_trailers";
    private static final String KEY_REVIEWS = "key_reviews";

    @BindView(R.id.iv_backdrop_image)
    ImageView mBackdropImageView;

    @BindView(R.id.ib_home)
    ImageButton mHomeImageButton;

    @BindView(R.id.tv_movie_title)
    TextView mTitleTextView;

    @BindView(R.id.iv_movie_poster)
    ImageView mPosterImageView;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.tv_rate)
    TextView mRateTextView;

    @BindView(R.id.b_add_or_remove_from_favorites)
    Button mFavoritesButton;

    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;

    @BindView(R.id.tv_no_trailers)
    TextView mNoTrailersTextView;

    @BindView(R.id.rv_details_trailers)
    RecyclerView mTrailersRecyclerView;

    @BindView(R.id.pb_loading_trailers)
    ProgressBar mLoadingTrailersProgressBar;

    @BindView(R.id.tv_no_reviews)
    TextView mNoReviewsTextView;

    @BindView(R.id.rv_details_reviews)
    RecyclerView mReviewsRecyclerView;

    @BindView(R.id.pb_loading_reviews)
    ProgressBar mLoadingReviewsProgressBar;

    private Movie mMovie;

    private List<Movie.Trailer> mTrailers;
    private List<Movie.Review> mReviews;

    private MovieTrailersAdapter mTrailersAdapter;
    private MovieReviewsAdapter mReviewsAdapter;

    private OnFragmentInteractionListener mFragmentInteractionListener;

    public static MovieDetailsFragment newInstance(Movie movie) {
        if (movie == null) {
            throw new IllegalStateException("Movie cannot be null!");
        }
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(KEY_MOVIE, movie);
        fragment.setArguments(arguments);
        return fragment;
    }

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, mRootView);

        mTrailersAdapter = new MovieTrailersAdapter(this);
        mTrailersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        ));
        mTrailersRecyclerView.setNestedScrollingEnabled(false);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        mReviewsAdapter = new MovieReviewsAdapter();
        mReviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        initializeMovie(savedInstanceState);
        return mRootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_MOVIE, mMovie);
        if (mTrailers instanceof ArrayList) {
            outState.putParcelableArrayList(KEY_TRAILERS, ((ArrayList<Movie.Trailer>) mTrailers));
        }
        if (mReviews instanceof ArrayList) {
            outState.putParcelableArrayList(KEY_REVIEWS, ((ArrayList<Movie.Review>) mReviews));
        }
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(savedInstanceState);
    }

    private void initializeMovie(Bundle savedInstanceState) {
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_MOVIE) &&
                savedInstanceState.getParcelable(KEY_MOVIE) != null) {
            showMovieDetails((Movie) savedInstanceState.getParcelable(KEY_MOVIE));
        } else if (getArguments() != null &&
                getArguments().containsKey(KEY_MOVIE) &&
                getArguments().getParcelable(KEY_MOVIE) != null) {
            showMovieDetails((Movie) getArguments().getParcelable(KEY_MOVIE));
        } else {
            throw new IllegalStateException("Movie cannot be null");
        }
    }

    private void initialize(Bundle savedInstanceState) {
        loadMovieFavoriteState();
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_TRAILERS) &&
                savedInstanceState.getParcelableArrayList(KEY_TRAILERS) != null) {
            ArrayList<Movie.Trailer> savedTrailers =
                    savedInstanceState.getParcelableArrayList(KEY_TRAILERS);
            showMovieTrailers(savedTrailers);
        } else {
            loadTrailers();
        }
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_REVIEWS) &&
                savedInstanceState.getParcelableArrayList(KEY_REVIEWS) != null) {
            ArrayList<Movie.Review> savedReviews =
                    savedInstanceState.getParcelableArrayList(KEY_REVIEWS);
            showMovieReviews(savedReviews);
        } else {
            loadReviews();
        }
    }

    private void loadMovieFavoriteState() {
        if (mMovie != null) {
            getPresenter().isFavoriteMovie(String.valueOf(mMovie.getId()));
        }
    }

    private void showMovieDetails(Movie movie) {
        if (movie != null) {
            mMovie = movie;
            Picasso
                    .get()
                    .load(mMovie.getBackdropPathUrl())
                    .into(mBackdropImageView);
            Picasso
                    .get()
                    .load(mMovie.getPosterPathUrl())
                    .into(mPosterImageView);
            mTitleTextView.setText(mMovie.getTitle());
            mReleaseDateTextView.setText(DateFormatter.format(mMovie.getReleaseDate()));
            mRateTextView.setText(String.valueOf(mMovie.getVoteAverage()));
            mOverviewTextView.setText(mMovie.getOverview());
        }
    }

    private void showMovieTrailers(List<Movie.Trailer> trailers) {
        if (trailers != null) {
            mTrailers = trailers;
            if (trailers.isEmpty()) {
                mNoTrailersTextView.setVisibility(View.VISIBLE);

                mNoTrailersTextView.setText(R.string.details_no_trailers_available);
                mTrailersRecyclerView.setVisibility(View.GONE);
                mLoadingTrailersProgressBar.setVisibility(View.GONE);
            } else {
                mTrailersRecyclerView.setVisibility(View.VISIBLE);
                mNoTrailersTextView.setVisibility(View.GONE);
                mLoadingTrailersProgressBar.setVisibility(View.GONE);
                mTrailersAdapter.updateList(trailers);
            }
        } else {
            mNoTrailersTextView.setVisibility(View.VISIBLE);
            mNoTrailersTextView.setText(R.string.error_loading_movie_trailers);
            mTrailersRecyclerView.setVisibility(View.GONE);
            mLoadingTrailersProgressBar.setVisibility(View.GONE);
        }
    }

    private void showMovieReviews(List<Movie.Review> reviews) {
        if (reviews != null) {
            mReviews = reviews;
            if (mReviews.isEmpty()) {
                mNoReviewsTextView.setVisibility(View.VISIBLE);
                mNoReviewsTextView.setText(R.string.details_no_reviews_available);
                mReviewsRecyclerView.setVisibility(View.GONE);
                mLoadingReviewsProgressBar.setVisibility(View.GONE);
            } else {
                mReviewsRecyclerView.setVisibility(View.VISIBLE);
                mNoReviewsTextView.setVisibility(View.GONE);
                mLoadingReviewsProgressBar.setVisibility(View.GONE);
                mReviewsAdapter.updateList(reviews);
            }
        } else {
            mNoReviewsTextView.setVisibility(View.VISIBLE);
            mNoReviewsTextView.setText(R.string.error_loading_movie_reviews);
            mReviewsRecyclerView.setVisibility(View.GONE);
            mLoadingReviewsProgressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.b_add_or_remove_from_favorites)
    void addOrRemoveFromFavorites() {
        if (mFavoritesButton.getText() != null &&
                MoviesApp.getStringRes(R.string.add_to_favorites)
                        .equals(mFavoritesButton.getText().toString())) {
            addToFavorites();
        } else {
            removeFromFavorites();
        }
    }

    private void addToFavorites() {
        getPresenter().addToFavorites(mMovie);
    }

    private void removeFromFavorites() {
        getPresenter().removeFromFavorites(mMovie);
    }

    public void setFavoritesButtonSelected(boolean favoritesButtonSelected) {
        if (favoritesButtonSelected) {
            mFavoritesButton.setBackground(MoviesApp.getDrawableRes(R.drawable.button_yellow));
            mFavoritesButton.setText(R.string.remove_from_favorites);
        } else {
            mFavoritesButton.setBackground(MoviesApp.getDrawableRes(R.drawable.button_green));
            mFavoritesButton.setText(R.string.add_to_favorites);
        }
    }

    private void loadReviews() {
        if (mMovie != null) {
            mReviewsRecyclerView.setVisibility(View.GONE);
            mNoReviewsTextView.setVisibility(View.GONE);
            mLoadingReviewsProgressBar.setVisibility(View.VISIBLE);
            getPresenter().loadMovieReviews(String.valueOf(mMovie.getId()));
        }
    }

    private void loadTrailers() {
        if (mMovie != null) {
            mTrailersRecyclerView.setVisibility(View.GONE);
            mNoTrailersTextView.setVisibility(View.GONE);
            mLoadingTrailersProgressBar.setVisibility(View.VISIBLE);
            getPresenter().loadMovieTrailers(String.valueOf(mMovie.getId()));
        }
    }

    @Override
    protected MovieDetailsPresenter createPresenter() {
        return new MovieDetailsPresenter(
                new SQLiteLocalMoviesRepository(),
                new RetrofitRemoteMoviesRepository(
                        new ConnectionChecker(),
                        MoviesApp.getStringRes(R.string.tmdb_api_key),
                        MoviesApp.getStringRes(R.string.tmdb_api_base_url)
                )
        );
    }

    @Override
    public void onTrailersRetrieved(List<Movie.Trailer> trailers) {
        showMovieTrailers(trailers);
    }

    @Override
    public void onReviewsRetrieved(List<Movie.Review> reviews) {
        showMovieReviews(reviews);
    }

    @Override
    public void onAddToFavoritesSuccess(Movie movie) {
        showMessage(MoviesApp.getStringRes(R.string.added_to_favorites));
        setFavoritesButtonSelected(true);
    }

    @Override
    public void onRemoveFromFavoritesSuccess(Movie movie) {
        showMessage(MoviesApp.getStringRes(R.string.removed_from_favorites));
        setFavoritesButtonSelected(false);
    }

    @Override
    public void onMovieFavoriteStateRetrieved(boolean isFavoriteMovie) {
        setFavoritesButtonSelected(isFavoriteMovie);
    }

    @Override
    public void onError(Error error) {
        if (error != null) {
            switch (error) {
                case ERROR_LOADING_MOVIE_TRAILERS:
                    onTrailersRetrieved(null);
                    break;
                case ERROR_LOADING_MOVIE_REVIEWS:
                    onReviewsRetrieved(null);
                    break;
                default:
                    showMessage(error.getStringResId());
                    break;
            }
        }
    }

    @Override
    public void onTrailerClick(Movie.Trailer trailer) {
        if (mFragmentInteractionListener != null &&
                trailer != null &&
                !TextUtils.isEmpty(trailer.getVideoUrl())) {
                mFragmentInteractionListener.onTrailerClick(trailer);
        }
    }

    public interface OnFragmentInteractionListener {

        void onTrailerClick(Movie.Trailer trailer);
    }
}
