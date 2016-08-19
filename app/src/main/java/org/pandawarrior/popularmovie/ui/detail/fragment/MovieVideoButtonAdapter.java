package org.pandawarrior.popularmovie.ui.detail.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.MovieVideo;
import org.pandawarrior.popularmovie.databinding.LayoutMovieVideoButtonBinding;

import java.util.List;

/**
 * Popular Movie App
 * Created by jtlie on 8/20/2016.
 */

public class MovieVideoButtonAdapter extends RecyclerView.Adapter<MovieVideoButtonAdapter.ViewHolder> {

    List<MovieVideo> videoList;

    public MovieVideoButtonAdapter(List<MovieVideo> videoList) {
        this.videoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutMovieVideoButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_movie_video_button, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieVideo video = videoList.get(position);
        holder.binding.setVideo(video);
    }

    @Override
    public int getItemCount() {
        if (videoList == null)
            return 0;
        return videoList.size();
    }

    public void setVideoList(List<MovieVideo> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutMovieVideoButtonBinding binding;

        public ViewHolder(LayoutMovieVideoButtonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
