package org.pandawarrior.popularmovie;

import org.pandawarrior.popularmovie.data.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Popular Movie App
 * Created by jtlie on 8/15/2016.
 */

public interface ApiService {
    @GET("/3/movie/popular")
    Call<MovieResponse> getPopularMovie();

    @GET("/3/movie/top_rated")
    Call<MovieResponse> getTopRatedMovie();
}
