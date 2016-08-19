package org.pandawarrior.popularmovie.ui.detail.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.pandawarrior.popularmovie.ApiService;
import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.data.MovieReview;
import org.pandawarrior.popularmovie.data.MovieReviewResponse;
import org.pandawarrior.popularmovie.data.MovieVideo;
import org.pandawarrior.popularmovie.data.MovieVideoResponse;
import org.pandawarrior.popularmovie.database.MovieColumns;
import org.pandawarrior.popularmovie.database.MovieProvider;
import org.pandawarrior.popularmovie.databinding.FragmentDetailBinding;
import org.pandawarrior.popularmovie.ui.detail.MovieDetailActivity;
import org.pandawarrior.popularmovie.ui.detail.MovieReviewRecyclerAdapter;
import org.pandawarrior.popularmovie.ui.main.MainActivity;
import org.pandawarrior.popularmovie.utils.ApiUtils;
import org.pandawarrior.popularmovie.utils.DatabaseUtils;
import org.pandawarrior.popularmovie.utils.DividerItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.pandawarrior.popularmovie.ui.detail.MovieDetailActivity.SAVED_MOVIE_VIDEO;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.MOVIE_COLUMNS;

/**
 * Popular Movie App
 * Created by jtlie on 8/20/2016.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String MOVIE_PARCELABLE = "MOVIE_PARCELABLE";
    private static final String SAVED_MOVIE = "SAVED_MOVIE";

    FragmentDetailBinding fragmentDetailBinding;
    private ArrayList<MovieVideo> videos;
    private ArrayList<MovieReview> reviews;
    private ApiService mApiService;

    MovieReviewRecyclerAdapter reviewRecyclerAdapter;
    MovieVideoButtonAdapter videoButtonAdapter;
    private boolean mIsFavourite = false;
    Movie movie;

    @Override
    public void setArguments(Bundle args) {
        movie = args.getParcelable(MOVIE_PARCELABLE);
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = ApiUtils.initApiService();
        if (movie == null && savedInstanceState != null){
            movie = savedInstanceState.getParcelable(SAVED_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        fragmentDetailBinding.setMovie(movie);
        initMovieReviewRecyclerView();
        initMovieReview(movie);
        if (savedInstanceState == null) {
            initMovieVideo(movie);
        } else {
            videos = savedInstanceState.getParcelableArrayList(SAVED_MOVIE_VIDEO);
            if (videos == null) {
                initMovieVideo(movie);
            } else {
                initMovieVideoYoutubeButton();
            }
        }
        initFavouriteButton();
        getLoaderManager().initLoader(MovieDetailActivity.MOVIE_FAV_DETAIL_LOADER, null, this);
        return fragmentDetailBinding.getRoot();
    }

    private void initFavouriteButton() {
        fragmentDetailBinding
                .btnDetailFavourite
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setFavouriteButton();
                        new SetFavouriteMovie().execute();
                    }
                });
    }

    private void setFavouriteButton(){
        if (mIsFavourite){
            fragmentDetailBinding
                    .btnDetailFavourite
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_white_24dp, 0, 0, 0);
        } else {
            fragmentDetailBinding
                    .btnDetailFavourite
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_white_24dp, 0, 0, 0);
        }
    }

    private class SetFavouriteMovie extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (mIsFavourite){
                getActivity().getContentResolver().delete(
                        MovieProvider.Movies.MOVIES_URI, MovieColumns._ID + " = ?", new String[] {Integer.toString(movie.getId())}
                );
            } else {
                ContentValues movieValues = DatabaseUtils.getMovieContentValues(movie);
                getActivity().getContentResolver().insert(
                        MovieProvider.Movies.MOVIES_URI,
                        movieValues);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).onFavouriteClick();
            }
        }
    }

    private void initMovieReview(Movie movie) {
        mApiService.getMovieReview(movie.getId()).enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {
                if (response.isSuccessful()) {
                    reviews = (ArrayList<MovieReview>) response.body().getData();
                    reviewRecyclerAdapter.setMovieReviews(reviews);
                }
            }

            @Override
            public void onFailure(Call<MovieReviewResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMovieVideo(Movie movie) {

        mApiService.getMovieVideo(movie.getId()).enqueue(new Callback<MovieVideoResponse>() {
            @Override
            public void onResponse(Call<MovieVideoResponse> call, Response<MovieVideoResponse> response) {
                if (response.isSuccessful()) {
                    videos = (ArrayList<MovieVideo>) response.body().getData();
                    initMovieVideoYoutubeButton();
                }
            }

            @Override
            public void onFailure(Call<MovieVideoResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initMovieReviewRecyclerView() {
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        reviewRecyclerAdapter = new MovieReviewRecyclerAdapter(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setAutoMeasureEnabled(true);
        fragmentDetailBinding
                .movieUserReview
                .setLayoutManager(linearLayoutManager);
        fragmentDetailBinding
                .movieUserReview
                .setAdapter(reviewRecyclerAdapter);
        fragmentDetailBinding
                .movieUserReview
                .setNestedScrollingEnabled(false);
        fragmentDetailBinding
                .movieUserReview
                .addItemDecoration(itemDecoration);
    }

    private void initMovieVideoYoutubeButton() {
        videoButtonAdapter = new MovieVideoButtonAdapter(videos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setAutoMeasureEnabled(true);
        fragmentDetailBinding
                .movieVideoButton
                .setLayoutManager(linearLayoutManager);
        fragmentDetailBinding
                .movieVideoButton
                .setAdapter(videoButtonAdapter);
        fragmentDetailBinding
                .movieVideoButton
                .setNestedScrollingEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_MOVIE_VIDEO, videos);
        outState.putParcelable(SAVED_MOVIE, movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.content.CursorLoader(
                getActivity(),
                MovieProvider.Movies.buildMovieId(movie.getId()),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        mIsFavourite = data.getCount() > 0;
        setFavouriteButton();
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }

}
