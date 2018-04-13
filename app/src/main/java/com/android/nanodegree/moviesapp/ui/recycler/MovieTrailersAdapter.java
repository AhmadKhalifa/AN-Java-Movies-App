package com.android.nanodegree.moviesapp.ui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Khalifa on 07/04/2018.
 *
 */
public class MovieTrailersAdapter
        extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersViewHolder> {

    private List<Movie.Trailer> mTrailers;
    private OnItemInteractionListener mItemInteractionListener;

    public MovieTrailersAdapter(OnItemInteractionListener onItemInteractionListener) {
        mItemInteractionListener = onItemInteractionListener;
    }

    public void updateList(List<Movie.Trailer> Trailers) {
        mTrailers = Trailers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieTrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieTrailersViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_trailer, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailersViewHolder holder, int position) {
        holder.setContent(position);
    }

    @Override
    public int getItemCount() {
        return mTrailers != null ? mTrailers.size() : 0;
    }

    class MovieTrailersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_trailer_thumbnail)
        ImageView mThumbnailImageView;

        @BindView(R.id.tv_trailer_name)
        TextView mNameTextView;

        private int mCurrentPosition;

        MovieTrailersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setContent(int position) {
            if (isValidValue(position)) {
                mCurrentPosition = position;
                Movie.Trailer trailer = mTrailers.get(mCurrentPosition);
                Picasso
                        .get()
                        .load(trailer.getThumbnailImageUrl())
                        .into(mThumbnailImageView);
                mNameTextView.setText(trailer.getName());
            }
        }

        @OnClick(R.id.cv_trailer_layout)
        void onTrailerClick() {
            if (isValidValue(mCurrentPosition) && mItemInteractionListener != null) {
                mItemInteractionListener.onTrailerClick(mTrailers.get(mCurrentPosition));
            }
        }

        private boolean isValidValue(int position) {
            return mTrailers != null &&
                    mTrailers.size() > position &&
                    mTrailers.get(position) != null;
        }
    }

    public interface OnItemInteractionListener {

        void onTrailerClick(Movie.Trailer trailer);
    }
}