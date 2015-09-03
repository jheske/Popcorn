/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nano.movies.R;
import com.squareup.picasso.Picasso;

import com.nano.movies.web.Tmdb;

public class MovieDetailActivity extends AppCompatActivity
        implements DetailActivityFragment.MovieDetailChangeListener {

    private final String TAG = getClass().getSimpleName();
    public static final String MOVIE_ID_EXTRA = "MOVIE ID EXTRA";
    private ImageView mImageViewBackdrop;
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
        setContentView(R.layout.activity_movie_detail_coordinated);
        setupToolbar();
        setupDetailFragment();
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
        mCollapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mImageViewBackdrop = (ImageView) findViewById(R.id.img_backdrop);
    }

    private void setupDetailFragment() {
        int movieId = getIntent().getIntExtra(MOVIE_ID_EXTRA, 0);
        DetailActivityFragment detailFragment = ((DetailActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movie_detail_coordinated));
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

        if (id == R.id.action_settings) {
            return true;
        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMovieDetailChanged(String backdropPath, String originalTitle) {
        //Display backdrop image in the AppBar
        String backdropUrl = Tmdb.getMovieBackdropUrl(backdropPath,
                Tmdb.IMAGE_POSTER_LARGE);
        Log.d(TAG, "Getting backdrop " + backdropUrl);
        Picasso.with(this).load(backdropUrl)
                .into(mImageViewBackdrop);
        mCollapsingToolbar.setTitle(originalTitle);

    }
}
