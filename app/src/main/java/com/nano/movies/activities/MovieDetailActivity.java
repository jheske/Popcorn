/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nano.movies.R;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_ID_EXTRA = "MOVIE ID EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetailFragment() {
        int movieId = getIntent().getIntExtra(MOVIE_ID_EXTRA, 0);
        MovieDetailFragment detailFragment = ((MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movie_detail));
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
}
