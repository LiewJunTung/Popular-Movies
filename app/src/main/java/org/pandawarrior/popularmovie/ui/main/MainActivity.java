package org.pandawarrior.popularmovie.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.databinding.ActivityMainBinding;
import org.pandawarrior.popularmovie.ui.detail.MovieDetailActivity;
import org.pandawarrior.popularmovie.ui.detail.fragment.DetailFragment;
import org.pandawarrior.popularmovie.ui.main.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements MainInterface{

    public static final String ACTIVITY_MOVIE_LIST = "mainActivityMovieList";
    public static final String ACTIVITY_FILTER_TYPE = "mainActivityFilterType";
    public static final int MOVIE_FAV_LIST_LOADER = 10;
    public static final String ACTIVITY_FAVOURITE_LIST = "mainActivityFavouriteList";
    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";

    ActivityMainBinding mainBinding;
    boolean mTwoPanes;
    private String subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initActionBar();
        if (mainBinding.detailFragment != null){
            mTwoPanes = true;
            ((MainFragment) getFragmentManager()
                    .findFragmentById(R.id.main_fragment))
                    .setTwoPanes(mTwoPanes);
        } else {
            mTwoPanes = false;
        }
    }

    private void initActionBar() {
        setSupportActionBar(mainBinding.activityMainToolbar.toolbar);
        if (subtitle != null && getSupportActionBar() != null){
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (mTwoPanes){
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailFragment.MOVIE_PARCELABLE, movie);
            detailFragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment, detailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_INTENT, movie);
            startActivity(intent);
        }

    }

    @Override
    public void onFavouriteClick() {
        if (mTwoPanes){
            ((MainFragment)getFragmentManager().findFragmentById(R.id.main_fragment)).reloadFavourite();
        }
    }

    @Override
    public void onSetFilter(int resName) {
        if (getSupportActionBar() != null){
            getSupportActionBar().setSubtitle(resName);
        } else {
            subtitle = getString(resName);
        }
    }
}
