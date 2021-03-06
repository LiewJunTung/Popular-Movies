package org.pandawarrior.popularmovie.ui.main;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.databinding.LayoutItemMovieBinding;

import java.util.List;

/**
 * Popular Movie App
 * Created by jtlie on 8/16/2016.
 */

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.ViewHolder> {

    private @Nullable List<Movie> movieList;
    private MovieClickHandler<Movie> clickHandler;
    private View emptyView;

    public MainActivityRecyclerAdapter(@Nullable List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setClickHandler(MovieClickHandler<Movie> clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutItemMovieBinding layoutItemMovieBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item_movie, parent, false);
        return new ViewHolder(layoutItemMovieBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (movieList != null){
            final Movie movie = movieList.get(position);
            if (movie != null){
                holder.binding.setMovie(movie);
                holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickHandler != null){
                            clickHandler.onItemClick(movie);
                        }
                    }
                });
                holder.binding.executePendingBindings();
            }
        }
    }

    public void setMovieList(@Nullable List<Movie> movieList){
        if (movieList != null){
            this.movieList = movieList;
        }
        notifyDataSetChanged();
    }

    public void addMovieList(@Nullable List<Movie> movieList){
        if (movieList != null && this.movieList != null){
            int start = this.movieList.size();
            this.movieList.addAll(movieList);
            notifyItemInserted(start);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public int getItemCount() {
        if (emptyView != null){
            if(movieList == null || movieList.size() <= 0){
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
        }
        if (movieList == null){
            return 0;
        }

        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LayoutItemMovieBinding binding;

        ViewHolder(LayoutItemMovieBinding movieBinding) {
            super(movieBinding.getRoot());
            binding = movieBinding;
        }
    }
}
