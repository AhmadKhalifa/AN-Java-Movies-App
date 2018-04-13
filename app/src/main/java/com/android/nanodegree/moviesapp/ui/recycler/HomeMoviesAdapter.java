package com.android.nanodegree.moviesapp.ui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.util.DateFormatter;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Khalifa on 03/04/2018.
 *
 */
public class HomeMoviesAdapter
        extends RecyclerView.Adapter<HomeMoviesAdapter.HomeMoviesViewHolder> {

    private OnItemInteractionListener mOnItemInteractionListener;
    private List<Movie> mMovies;
    private HashSet<Long> mFavoriteMoviesIdsSet;

    public HomeMoviesAdapter(OnItemInteractionListener onItemInteractionListener) {
        mOnItemInteractionListener = onItemInteractionListener;
    }

    public void updateList(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void updateFavoriteMoviesIdsSet(HashSet<Long> favoriteMoviesIdsSet) {
        mFavoriteMoviesIdsSet = favoriteMoviesIdsSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeMoviesViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMoviesViewHolder holder, int position) {
        holder.setContent(position);
    }

    @Override
    public int getItemCount() {
        return mMovies != null ? mMovies.size() : 0;
    }

    class HomeMoviesViewHolder extends RecyclerView.ViewHolder {

        private int mCurrentPosition;

        @BindView(R.id.ib_favorite_or_unFavorite)
        ImageButton mFavoriteOrUnFavoriteImageButton;

        @BindView(R.id.iv_movie_poster)
        ImageView mPosterImageView;

        @BindView(R.id.tv_movie_name)
        TextView mNameTextView;

        @BindView(R.id.tv_movie_release_date)
        TextView mReleaseDateTextView;

        @BindView(R.id.tv_movie_rate)
        TextView mRateTextView;

        HomeMoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setContent(int position) {
            if (isValidValue(position)) {
                mCurrentPosition = position;
                Movie movie = mMovies.get(mCurrentPosition);
                if (mFavoriteMoviesIdsSet != null) {
                    mFavoriteOrUnFavoriteImageButton.setVisibility(View.VISIBLE);
                    mFavoriteOrUnFavoriteImageButton.setImageDrawable(MoviesApp.getDrawableRes(
                            mFavoriteMoviesIdsSet.contains(movie.getId()) ?
                                    R.drawable.ic_favorite_white :
                                    R.drawable.ic_favorite_border_white
                    ));
                } else {
                    mFavoriteOrUnFavoriteImageButton.setVisibility(View.INVISIBLE);
                }
                Picasso
                        .get()
                        .load(movie.getPosterPathUrl())
                        .into(mPosterImageView);
                mNameTextView.setText(movie.getTitle());
                mReleaseDateTextView.setText(DateFormatter.format(movie.getReleaseDate()));
                mRateTextView.setText(String.valueOf(movie.getVoteAverage()));
            }
        }

        private boolean isValidValue(int position) {
            return mMovies != null && mMovies.size() > position && mMovies.get(position) != null;
        }

        @OnClick({
                R.id.grid_item_movie,
                R.id.ib_favorite_or_unFavorite
        })
        void onClick(View view) {
            if (mOnItemInteractionListener == null) {
                return;
            }
            switch (view.getId()) {
                case R.id.grid_item_movie:
                    mOnItemInteractionListener.onMovieClick(mMovies.get(mCurrentPosition));
                    break;
                case R.id.ib_favorite_or_unFavorite:
                    if (mFavoriteMoviesIdsSet == null) {
                        return;
                    }
                    Movie movie = mMovies.get(mCurrentPosition);
                    if (mFavoriteMoviesIdsSet.contains(movie.getId())) {
                        mOnItemInteractionListener.onRemoveMovieFromFavorites(movie);
                    } else {
                        mOnItemInteractionListener.onAddMovieToFavorites(movie);
                    }
                    break;
            }
        }
    }

    public interface OnItemInteractionListener {

        void onMovieClick(Movie movie);

        void onAddMovieToFavorites(Movie movie);

        void onRemoveMovieFromFavorites(Movie movie);
    }
}
