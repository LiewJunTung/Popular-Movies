package org.pandawarrior.popularmovie;

import org.pandawarrior.popularmovie.data.MovieResponse;
import org.pandawarrior.popularmovie.data.MovieReviewResponse;
import org.pandawarrior.popularmovie.data.MovieVideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Popular Movie App
 * Created by jtlie on 8/15/2016.
 */

public interface ApiService {
    @GET("/3/movie/popular")
    Call<MovieResponse> getPopularMovie();

    @GET("/3/movie/top_rated")
    Call<MovieResponse> getTopRatedMovie();

    @GET("/3/movie/{id}/videos")
    Call<MovieVideoResponse> getMovieVideo(@Path("id") int id);

    @GET("/3/movie/{id}/reviews")
    Call<MovieReviewResponse> getMovieReview(@Path("id") int id);
}
