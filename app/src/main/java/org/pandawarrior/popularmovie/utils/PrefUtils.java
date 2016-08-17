package org.pandawarrior.popularmovie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Popular Movie App
 * Created by jtlie on 8/17/2016.
 */

public class PrefUtils {
    public static final String FAV_MOVIES = "fav_movies";

    private static SharedPreferences initSharedPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isFavourite(Context context, int movieId){
        SharedPreferences sharedPreferences = initSharedPref(context);
        Set<String> favMovies = getFavouriteMovieId(context);
        return favMovies.contains(Integer.toString(movieId));
    }

    public static void setFavourite(Context context, int movieId){
        SharedPreferences sharedPreferences = initSharedPref(context);
        Set<String> favMovies = getFavouriteMovieId(context);
        favMovies.add(Integer.toString(movieId));
        sharedPreferences.edit().putStringSet(FAV_MOVIES, favMovies).apply();
    }

    public static void setUnfavourite(Context context, int movieId){
        SharedPreferences sharedPreferences = initSharedPref(context);
        Set<String> favMovies = getFavouriteMovieId(context);
        favMovies.remove(Integer.toString(movieId));
        sharedPreferences.edit().putStringSet(FAV_MOVIES, favMovies).apply();
    }

    private static Set<String> getFavouriteMovieId(Context context){
        SharedPreferences sharedPreferences = initSharedPref(context);
        return sharedPreferences.getStringSet(FAV_MOVIES, new HashSet<String>());
    }
}
