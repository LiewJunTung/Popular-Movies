package org.pandawarrior.popularmovie.ui.main.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.pandawarrior.popularmovie.ApiService;
import org.pandawarrior.popularmovie.R;
import org.pandawarrior.popularmovie.data.Movie;
import org.pandawarrior.popularmovie.data.MovieResponse;
import org.pandawarrior.popularmovie.database.MovieProvider;
import org.pandawarrior.popularmovie.databinding.FragmentMainBinding;
import org.pandawarrior.popularmovie.ui.main.MainActivity;
import org.pandawarrior.popularmovie.ui.main.MainActivityRecyclerAdapter;
import org.pandawarrior.popularmovie.ui.main.MovieClickHandler;
import org.pandawarrior.popularmovie.utils.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.pandawarrior.popularmovie.ui.main.MainActivity.ACTIVITY_FAVOURITE_LIST;
import static org.pandawarrior.popularmovie.ui.main.MainActivity.ACTIVITY_FILTER_TYPE;
import static org.pandawarrior.popularmovie.ui.main.MainActivity.ACTIVITY_MOVIE_LIST;
import static org.pandawarrior.popularmovie.ui.main.MainActivity.MOVIE_FAV_LIST_LOADER;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.ADULT;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.BACKDROP_PATH;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.MOVIE_COLUMNS;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.ORIGINAL_LANGUAGE;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.ORIGINAL_TITLE;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.OVERVIEW;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.POPULARITY;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.POSTER_PATH;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.RELEASE_DATE;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.TITLE;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.VIDEO;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.VOTE_AVERAGE;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils.VOTE_COUNT;
import static org.pandawarrior.popularmovie.utils.DatabaseUtils._ID;

/**
 * Popular Movie App
 * Created by jtlie on 8/20/2016.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ApiService mApiService;
    MainActivityRecyclerAdapter mainActivityRecyclerAdapter;

    FragmentMainBinding mainBinding;
    ArrayList<Movie> movieArrayList;

    @ApiUtils.ApiFilter
    int mApiFilterType;
    private ArrayList<Movie> mFavouriteList;
    private boolean mTwoPanes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavouriteList = new ArrayList<>();
        mApiService = ApiUtils.initApiService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        initRecyclerview();
        initData(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_FAV_LIST_LOADER, null, this);
        mainBinding.mainRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1, true);
            }
        });
        return mainBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(MOVIE_FAV_LIST_LOADER, null, this);
    }

    private void initRecyclerview() {
        mainActivityRecyclerAdapter = new MainActivityRecyclerAdapter(null);
        mainActivityRecyclerAdapter.setEmptyView(mainBinding.mainEmptyText);
        mainBinding.activityMainRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mainActivityRecyclerAdapter.setClickHandler(new MovieClickHandler<Movie>() {
            @Override
            public void onItemClick(Movie movie) {
                ((MainActivity)getActivity()).onMovieClick(movie);
            }
        });
        mainBinding.activityMainRecyclerView.setAdapter(mainActivityRecyclerAdapter);
    }

    private void stopRefresh(){
        if (mainBinding.mainRefreshLayout != null && mainBinding.mainRefreshLayout.isRefreshing()){
            mainBinding.mainRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressWarnings("ResourceType")
    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            movieArrayList = savedInstanceState.getParcelableArrayList(ACTIVITY_MOVIE_LIST);
            mApiFilterType = savedInstanceState.getInt(ACTIVITY_FILTER_TYPE);
            mFavouriteList = savedInstanceState.getParcelableArrayList(ACTIVITY_FAVOURITE_LIST);
            if (mApiFilterType != ApiUtils.API_FAVOURITES){
                mainActivityRecyclerAdapter.setMovieList(movieArrayList);
            }
        } else {
            mApiFilterType = ApiUtils.API_POPULAR;
            loadData(1, true);
        }
        setSubtitle();
    }

    private void setSubtitle(){
        int subtitle;
        switch (mApiFilterType) {
            case ApiUtils.API_POPULAR:
                subtitle = R.string.text_popular;
                break;
            case ApiUtils.API_TOP_RATED:
                subtitle = R.string.text_top_rated;
                break;
            case ApiUtils.API_FAVOURITES:
                subtitle = R.string.text_favourite;
                break;
            default:
                subtitle = R.string.text_popular;
        }
        ((MainActivity)getActivity()).onSetFilter(subtitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_sort_popular:
                mApiFilterType = ApiUtils.API_POPULAR;
                break;
            case R.id.menu_sort_top:
                mApiFilterType = ApiUtils.API_TOP_RATED;
                break;
            case R.id.menu_sort_favourite:
                mApiFilterType = ApiUtils.API_FAVOURITES;
                break;
        }
        setSubtitle();
        loadData(1, true);
        return super.onOptionsItemSelected(item);
    }

    private void loadData(int page, final boolean isStart) {
        Call<MovieResponse> call;
        if (mApiFilterType == ApiUtils.API_POPULAR) {
            call = mApiService.getPopularMovie();
        } else if (mApiFilterType == ApiUtils.API_TOP_RATED) {
            call = mApiService.getTopRatedMovie();
        } else if (mApiFilterType == ApiUtils.API_FAVOURITES) {
            if (mFavouriteList != null){
                mainActivityRecyclerAdapter.setMovieList(mFavouriteList);
                stopRefresh();
            }
            return;
        } else {
            return;
        }
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    movieArrayList = (ArrayList<Movie>) response.body().getData();
                    if (isStart) {
                        mainActivityRecyclerAdapter.setMovieList(movieArrayList);
                    } else {
                        mainActivityRecyclerAdapter.addMovieList(movieArrayList);
                    }
                }
                stopRefresh();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                stopRefresh();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ACTIVITY_MOVIE_LIST, movieArrayList);
        outState.putInt(ACTIVITY_FILTER_TYPE, mApiFilterType);
        super.onSaveInstanceState(outState);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getActivity(),
                MovieProvider.Movies.MOVIES_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        mFavouriteList = new ArrayList<>();
        try {
            while (data.moveToNext()){
                Movie movie = new Movie(
                        data.getInt(_ID),
                        data.getInt(ADULT),
                        data.getString(POSTER_PATH),
                        data.getString(OVERVIEW),
                        data.getString(ORIGINAL_TITLE),
                        data.getString(ORIGINAL_LANGUAGE),
                        data.getString(TITLE),
                        data.getString(BACKDROP_PATH),
                        data.getDouble(POPULARITY),
                        data.getInt(VOTE_COUNT),
                        data.getInt(VIDEO),
                        data.getDouble(VOTE_AVERAGE),
                        data.getString(RELEASE_DATE)
                );
                mFavouriteList.add(movie);
            }
            if (mApiFilterType == ApiUtils.API_FAVOURITES){
                mainActivityRecyclerAdapter.setMovieList(mFavouriteList);
            }
        } finally {
            data.close();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mainActivityRecyclerAdapter.setMovieList(null);
    }

    public void setTwoPanes(boolean twoPanes) {
        this.mTwoPanes = twoPanes;
    }

    public void reloadFavourite() {
        getLoaderManager().restartLoader(MOVIE_FAV_LIST_LOADER, null, this);
    }
}
