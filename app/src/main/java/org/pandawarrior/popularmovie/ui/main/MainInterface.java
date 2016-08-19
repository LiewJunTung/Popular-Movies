package org.pandawarrior.popularmovie.ui.main;


import org.pandawarrior.popularmovie.data.Movie;

/**
 * Popular Movie App
 * Created by jtlie on 8/20/2016.
 */

public interface MainInterface {
    void onMovieClick(Movie movie);
    void onFavouriteClick();
    void onSetFilter(int resName);
}
