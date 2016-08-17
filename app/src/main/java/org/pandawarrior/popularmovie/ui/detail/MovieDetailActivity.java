package org.pandawarrior.popularmovie.ui.detail;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.pandawarrior.popularmovie.ApiService;
import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.data.MovieReview;
import org.pandawarrior.popularmovie.data.MovieReviewResponse;
import org.pandawarrior.popularmovie.data.MovieVideo;
import org.pandawarrior.popularmovie.data.MovieVideoResponse;
import org.pandawarrior.popularmovie.database.MovieProvider;
import org.pandawarrior.popularmovie.databinding.ActivityMovieDetailBinding;
import org.pandawarrior.popularmovie.utils.ApiUtils;
import org.pandawarrior.popularmovie.utils.DatabaseUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.pandawarrior.popularmovie.utils.DatabaseUtils.MOVIE_COLUMNS;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String SAVED_MOVIE_VIDEO = "saved_movie_video";

    public static final String MOVIE_INTENT = "movie_intent";
    private static final int MOVIE_FAV_DETAIL_LOADER = 11;
    ActivityMovieDetailBinding movieDetailBinding;
    private ArrayList<MovieVideo> videos;
    private ArrayList<MovieReview> reviews;
    private ApiService mApiService;

    MovieReviewRecyclerAdapter reviewRecyclerAdapter;
    private boolean mIsFavourite = false;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = ApiUtils.initApiService();
        Intent intent = getIntent();
        movie = intent.getParcelableExtra(MOVIE_INTENT);
        movieDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        movieDetailBinding.setMovie(movie);
        setSupportActionBar(movieDetailBinding.toolbar);
        initMovieReviewRecyclerView();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        initMovieReview(movie);
        initFavouriteButton();
        getSupportLoaderManager().initLoader(MOVIE_FAV_DETAIL_LOADER, null, this);
    }

    private void initFavouriteButton() {
        movieDetailBinding
                .contentMovieDetailInclude
                .btnDetailFavourite
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIsFavourite = !mIsFavourite;
                        setFavouriteButton();
                    }
                });
    }

    private void setFavouriteButton(){
        if (mIsFavourite){
            movieDetailBinding
                    .contentMovieDetailInclude
                    .btnDetailFavourite
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_white_24dp, 0, 0, 0);
        } else {
            movieDetailBinding
                    .contentMovieDetailInclude
                    .btnDetailFavourite
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_white_24dp, 0, 0, 0);
        }
        new SetFavouriteMovie().execute();
    }

    private class SetFavouriteMovie extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if (mIsFavourite){
                ContentValues movieValues = DatabaseUtils.getMovieContentValues(movie);
                Uri insertedUri = MovieDetailActivity.this.getContentResolver().insert(
                        MovieProvider.Movies.MOVIES_URI,
                        movieValues
                );
            }
            return null;
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
                Toast.makeText(MovieDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MovieDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initMovieReviewRecyclerView() {
        reviewRecyclerAdapter = new MovieReviewRecyclerAdapter(null);
        movieDetailBinding
                .contentMovieDetailInclude
                .movieUserReview
                .setLayoutManager(new LinearLayoutManager(this));
        movieDetailBinding
                .contentMovieDetailInclude
                .movieUserReview
                .setAdapter(reviewRecyclerAdapter);
        movieDetailBinding
                .contentMovieDetailInclude
                .movieUserReview
                .setNestedScrollingEnabled(false);
    }

    private void initMovieVideoYoutubeButton() {
        if (videos.size() > 0) {
            for (final MovieVideo video : videos) {
                Button button = new Button(this);
                button.setText(video.getName());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
                    }
                });
                movieDetailBinding
                        .contentMovieDetailInclude
                        .contentMovieDetail
                        .addView(button);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_MOVIE_VIDEO, videos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                MovieProvider.Movies.buildMovieId(id),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
