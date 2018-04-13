package com.android.nanodegree.moviesapp.ui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Khalifa on 07/04/2018.
 *
 */
public class MovieReviewsAdapter
        extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsViewHolder> {

    private List<Movie.Review> mReviews;

    public void updateList(List<Movie.Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieReviewsAdapter.MovieReviewsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsViewHolder holder, int position) {
        holder.setContent(position);
    }

    @Override
    public int getItemCount() {
        return mReviews != null ? mReviews.size() : 0;
    }

    class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_review_author)
        TextView mAuthorNameTextView;

        @BindView(R.id.tv_review_content)
        TextView mContentTextView;

        MovieReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setContent(int position) {
            if (isValidValue(position)) {
                Movie.Review review = mReviews.get(position);
                mAuthorNameTextView.setText(review.getAuthor());
                mContentTextView.setText(review.getContent());
            }
        }

        private boolean isValidValue(int position) {
            return mReviews != null && mReviews.size() > position && mReviews.get(position) != null;
        }
    }
}
