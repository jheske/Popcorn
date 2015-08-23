/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.facebook.stetho.Stetho;
import com.nano.movies.R;
import com.nano.movies.utils.DatabaseUtils;
import com.nano.movies.web.MovieServiceProxy;

/**
 * Multi-pane Layout:
 * The app is designed to adapt the layout to the device, specifically a
 * phone, or a tablet either in portrait or landscape mode.
 * Phones and tablets in portrait mode will use a one-pane layout,
 * while tablets in landscape will use a two-pane layout.
 * In order to determine which layout to use, Android looks for
 * layouts.xml in res/values (a one-pane layout) for phones,
 * res/values-large-land (two-pane), or res/values-large-port (one-pane).
 * Layout.xml will direct Android to the correct one-pane or two-pane layout
 * in the res/layouts directory.
 * <p/>
 * <p/>
 * Organization:
 * <p/>
 * MainActivity contains 2 Fragments:
 * <p/>
 * MovieGridFragment handles the
 * Main UI and maintains the list of
 * downloaded movies and corresponding view (in the adapter).
 * This Fragment communicates with MainActivity through callback
 * Interface MovieSelectionListener, which the Activity
 * Implements.
 * When the user selects an item on the grid,
 * the Fragment calls the Listener's onMovieSelected callback
 * method to pass the path to the selected image to MainActivity,
 * which will the pass it on to either
 * MovieDetailFragment (in two-pane layout) or MovieViewActivity
 * (one-pane layout)
 * <p/>
 * MovieDetailFragment displays movie details when
 * user selects a poster from the grid.
 * The fragment is only available in a two-pane layout,
 * and displays on the same screen and to the right
 * of MovieGridFragment, so it is not used at all on a phone or on a
 * tablet in portrait mode.  I one-pane layout, we use MovieDetailActivity
 * to load its fragment show the details on a separate screen instead.
 */
public class MainActivity extends AppCompatActivity
        implements MovieGridFragment.MovieSelectionListener {
    private final String TAG = getClass().getSimpleName();

    private MovieGridFragment mMovieGridFragment;
    private MovieDetailFragment mMovieDetailFragment;
    private boolean mIsTwoPane = false;
    private static final String MOVIEFRAGMENT_TAG = "MOVIEFRAGMENT_TAG";


    /**
     * Android will load either
     * activity_main_one_pane.xml
     * or  activity_main_two_pane.xml
     * depending on which version of layout_main
     * it loads.
     * <p/>
     * layout_main is defined in 2 different
     * versions of layouts.xml:
     * values/layouts.xml - one-pane layout for all devices in portrait mode
     * values-large-land/layouts.xml - two-pane layout for devices in landscape mode
     * <p/>
     * Android decides which one to load
     * depending on device orientation.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);
        setupToolbar();
        setupFragments();
        setupSpinner();
        mIsTwoPane = checkForDualPane();
        setupStethoLibrary();
        //@TODO take this out once app is working so data can persist
        //DatabaseUtils.clearDatabase(this);
    }

    /**
     * A very useful library for debugging Android apps
     * using Chrome, even has a database inspector!
     */
    private void setupStethoLibrary() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    /**
     * Replace default toolbar with custom toolbar defined
     * in layouts/app_bar.xml
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Retrieve Fragments
     * If we are in a one-pane layout,
     * mMovieDetailFragment will be null because it is not
     * defined in the one-pane layout.
     * <p/>
     * Static and Dynamic Fragments
     * http://stackoverflow.com/questions/21816650/activity-layout-with-static-and-dynamic-fragments
     * FrameLayout vs Fragment
     * http://stackoverflow.com/questions/19453530/android-when-why-should-i-use-framelayout-instead-of-fragment
     */
    private void setupFragments() {
        /*mMovieGridFragment = (MovieGridFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragment_movie_grid);*/
        /**
         *  The main grid container has to be a DYNAMIC FRAGMENT because the app has two different
         *  ones: one for Movies, which are stored in a List and displayed
         *  using a regular Adapter; and another for Favorite Movies, which need to persist
         *  and are stored in a database and loaded using a Loader and viewed using an
         *  Adapter with a Cursor.
         *
         *  @TODO select MovieGridFragment or FavoritesGridFragment based on user's current Spinner
         *  selection (Movies vs Favorites)
         */
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_grid_container, new MovieGridFragment(), MOVIEFRAGMENT_TAG)
                .commit();
        // This is a STATIC FRAGMENT because it never needs to be
        // swapped with a different fragment.
        mMovieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragment_movie_detail);
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
     * Set up spinner for selecting
     * sort by Most Popular
     * sort by Highest Rated
     * show Favorites (NOT IMPLEMENTED YET)
     */
    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_sort_by);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_by_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        SpinnerInteractionListener listener = new SpinnerInteractionListener();
        spinner.setOnTouchListener(listener);
        spinner.setOnItemSelectedListener(listener);
    }

    public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean isUserSelected = false;

        /**
         * This prevents the Spinner from firing unless the
         * user taps the screen to make a selection.  Otherwise
         * it fires itself automatically on initialization, annoying!!
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isUserSelected = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int position, long id) {
            if (isUserSelected) {
                String sortBy = (String) parent.getItemAtPosition(position);
                if (sortBy.equals(getString(R.string.option_most_popular)))
                    mMovieGridFragment.setSortBy(MovieServiceProxy.POPULARITY_DESC);
                else if (sortBy.equals(getString(R.string.option_highest_rated)))
                    mMovieGridFragment.setSortBy(MovieServiceProxy.VOTE_AVERAGE_DESC);
                    //@TODO Show favorites in its own fragment
                else
                    mMovieGridFragment.setSortBy(MovieServiceProxy.VOTE_AVERAGE_DESC);
                mMovieGridFragment.downloadMovies();
                isUserSelected = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * Display Movie details.
     * If two-pane mode, then direct already-existing Fragment
     * to download details for the selected movie.
     * If one-pane, then start a new Activity tod
     * display the movie on its own screen.
     */
    public void onMovieSelected(int movieId, boolean isUserSelected) {
        if (mIsTwoPane) {
            mMovieDetailFragment.downloadMovie(movieId);
        } else {
            if (isUserSelected)
                startMovieDetailActivity(movieId);
        }
    }

    private void startMovieDetailActivity(int movieId) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA, movieId);
        this.startActivity(intent);
    }
}
