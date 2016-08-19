package org.pandawarrior.popularmovie.ui.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.databinding.ActivityMovieDetailBinding;
import org.pandawarrior.popularmovie.ui.detail.fragment.DetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String SAVED_MOVIE_VIDEO = "saved_movie_video";

    public static final String MOVIE_INTENT = "movie_intent";
    public static final int MOVIE_FAV_DETAIL_LOADER = 11;
    ActivityMovieDetailBinding movieDetailBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        setSupportActionBar(movieDetailBinding.toolbar);
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MOVIE_INTENT);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailFragment.MOVIE_PARCELABLE, movie);
            detailFragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_fragment, detailFragment)
                    .commit();
        }

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

}
