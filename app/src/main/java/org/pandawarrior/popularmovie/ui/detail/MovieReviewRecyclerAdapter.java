package org.pandawarrior.popularmovie.ui.detail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.MovieReview;
import org.pandawarrior.popularmovie.databinding.LayoutItemMovieReviewBinding;

import java.util.List;

/**
 * Popular Movie App
 * Created by jtlie on 8/17/2016.
 */

public class MovieReviewRecyclerAdapter extends RecyclerView.Adapter<MovieReviewRecyclerAdapter.ViewHolder> {

    List<MovieReview> movieReviews;

    public MovieReviewRecyclerAdapter(List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutItemMovieReviewBinding reviewBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.layout_item_movie_review, parent, false
                );
        return new ViewHolder(reviewBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieReview review = movieReviews.get(position);
        holder.binding.setReview(review);
    }

    @Override
    public int getItemCount() {
        if (movieReviews == null){
            return 0;
        }
        return movieReviews.size();
    }

    public void setMovieReviews(List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutItemMovieReviewBinding binding;

        public ViewHolder(LayoutItemMovieReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
