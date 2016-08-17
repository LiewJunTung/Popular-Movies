package org.pandawarrior.popularmovie.utils;

import android.content.ContentValues;

import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.database.MovieColumns;

/**
 * Popular Movie App
 * Created by jtlie on 8/18/2016.
 */

public class DatabaseUtils {
    public static final String PATH_MOVIES = "movies";

    public static final String[] MOVIE_COLUMNS = {
            MovieColumns.TABLE_NAME + "." + MovieColumns._ID,
            MovieColumns._ID,
            MovieColumns.POSTER_PATH,
            MovieColumns.ADULT,
            MovieColumns.OVERVIEW,
            MovieColumns.ORIGINAL_TITLE,
            MovieColumns.ORIGINAL_LANGUAGE,
            MovieColumns.TITLE,
            MovieColumns.BACKDROP_PATH,
            MovieColumns.POPULARITY,
            MovieColumns.VOTE_COUNT,
            MovieColumns.VIDEO,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.RELEASE_DATE
    };

    public static final int _ID = 0;
    public static final int POSTER_PATH = 2;
    public static final int ADULT = 3;
    public static final int OVERVIEW = 4;
    public static final int ORIGINAL_TITLE = 5;
    public static final int ORIGINAL_LANGUAGE = 6;
    public static final int TITLE = 7;
    public static final int BACKDROP_PATH = 8;
    public static final int POPULARITY = 9;
    public static final int VOTE_COUNT = 10;
    public static final int VIDEO = 11;
    public static final int VOTE_AVERAGE = 12;
    public static final int RELEASE_DATE = 13;

    public static ContentValues getMovieContentValues(Movie movie) {
        ContentValues movieContentValue = new ContentValues();
        movieContentValue.put(MovieColumns._ID, movie.getId());
        movieContentValue.put(MovieColumns.ADULT, movie.isAdult());
        movieContentValue.put(MovieColumns.OVERVIEW, movie.getOverview());
        movieContentValue.put(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
        movieContentValue.put(MovieColumns.ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        movieContentValue.put(MovieColumns.TITLE, movie.getTitle());
        movieContentValue.put(MovieColumns.BACKDROP_PATH, movie.getBackdropPath());
        movieContentValue.put(MovieColumns.POSTER_PATH, movie.getPosterPath());
        movieContentValue.put(MovieColumns.POPULARITY, movie.getPopularity());
        movieContentValue.put(MovieColumns.VOTE_COUNT, movie.getVoteCount());
        movieContentValue.put(MovieColumns.VIDEO, movie.isVideo());
        movieContentValue.put(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
        movieContentValue.put(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
        return movieContentValue;
    }
}
