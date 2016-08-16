package org.pandawarrior.popularmovie.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.databinding.ActivityMovieDetailBinding;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_INTENT = "movie_intent";
    ActivityMovieDetailBinding movieDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MOVIE_INTENT);
        movieDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        movieDetailBinding.setMovie(movie);
        setSupportActionBar(movieDetailBinding.toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
