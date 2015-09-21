/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nano.movies.R;
import com.nano.movies.web.Movie;
import com.squareup.picasso.Picasso;

import com.nano.movies.web.Tmdb;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
        implements DetailActivityFragment.MovieDetailChangeListener {

    private final String TAG = getClass().getSimpleName();
    public static final String MOVIE_ID_EXTRA = "MOVIE ID EXTRA";
    public static final String MOVIE_EXTRA = "MOVIE EXTRA";
    private boolean mIsTwoPane=false;

    //Small screens won't have a backdrop
    @Nullable
    @Bind(R.id.img_backdrop)
    ImageView mImageViewBackdrop;
    @Nullable
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    /**
     ******** TO INTEGRATE COORDINATOR WITH AppBar**********
     *
     * The CoordinatorLayout should wrap EVERYTHING,
     *  Collapsing AppBar (and its Backdrop Image)
     *
     *  And the fragment should show up below.
     *  The fragment should scroll and collapse the AppBar
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setupToolbar();
        mIsTwoPane = checkForDualPane();
        setupDetailFragment();
    }

    /**
     * Determine whether we are in two-pane mode, based
     * on layouts.xml-defined boolean value.
     */
    private boolean checkForDualPane() {
        // has_two_panes is defined in values/layouts.xml
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            Log.i(TAG, "Two-pane layout");
            return true;
        } else {
            Log.i(TAG, "One-pane layout");
            return false;
        }
    }

    /**
     * Set up custom toolbar and ensure that
     * the Up button takes user back to the screen's
     * Parent Activity as defined in Android docs:
     *
     * http://developer.android.com/intl/zh-cn/training/design-navigation/ancestral-temporal.html
     */
    private void setupToolbar() {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * If there is no internet connection and user is
     * displaying a Favorite, then movie was
     * loaded from the cache and can't be downloaded.
     */
    private void setupDetailFragment() {
        DetailActivityFragment detailFragment = ((DetailActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_activity_detail));

        int movieId = getIntent().getIntExtra(MOVIE_ID_EXTRA, -1);
        if (movieId == -1)  {
            Movie movie = getIntent().getParcelableExtra(MOVIE_EXTRA);
            detailFragment.displayMovieDetails(movie);
        }
        else
          detailFragment.downloadMovie(movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_item_share:
                // Defer to Fragment menu
                return false;
            default:
                return false;
        }
    }

    /**
     * Small screens don't have a title on the backdrop
     *
     * @param backdropPath
     * @param originalTitle
     */
    public void onMovieDetailChanged(String backdropPath, String originalTitle) {
        if(mImageViewBackdrop == null)
            return;
        //Display backdrop image in the AppBar
        String backdropUrl = Tmdb.getMovieBackdropUrl(backdropPath,
                Tmdb.IMAGE_POSTER_LARGE);
        Log.d(TAG, "Getting backdrop " + backdropUrl);
        Picasso.with(this).load(backdropUrl)
                .placeholder(R.drawable.placeholder_backdrop_w300)
                .error(R.drawable.placeholder_backdrop_w300)
                .into(mImageViewBackdrop);
        mCollapsingToolbar.setTitle(originalTitle);

    }
}
