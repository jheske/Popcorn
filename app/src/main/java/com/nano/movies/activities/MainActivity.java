/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 *
 * A million thanks to Uwe Trottmann for publishing
 * sample java code demonstrating a Retrofit/Gson
 * interface to themoviedb.org api.
 * Portions of this project, particularly MovieServiceProxy,
 * are informed by that code base.
 * @See https://gitlab.com/winiceo/tmdb-java
 *
 */
package com.nano.movies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
 * MovieMainGridFragment handles the
 * Main UI and maintains the list of
 * downloaded movies and corresponding view (in the adapter).
 * This Fragment communicates with MainActivity through callback
 * Interface MovieSelectionListener, which the Activity
 * Implements.
 * When the user selects an item on the grid,
 * the Fragment calls the Listener's onMovieSelected callback
 * method to pass the path to the selected image to MainActivity,
 * which will the pass it on to either
 * DetailActivityFragment (in two-pane layout) or MovieViewActivity
 * (one-pane layout)
 * <p/>
 * DetailActivityFragment displays movie details when
 * user selects a poster from the grid.
 * The fragment is only available in a two-pane layout,
 * and displays on the same screen and to the right
 * of MovieMainGridFragment, so it is not used at all on a phone or on a
 * tablet in portrait mode.  I one-pane layout, we use MovieDetailActivity
 * to load its fragment show the details on a separate screen instead.
 */
public class MainActivity extends AppCompatActivity
        implements MovieMainGridFragment.MovieSelectionListener,
        FavoritesGridFragment.MovieSelectionListener {
    private final String TAG = getClass().getSimpleName();

    private boolean mIsTwoPane = false;

    //private DetailActivityFragment mMovieDetailFragment;
    private DetailFragment mMovieDetailFragment;
    private MovieMainGridFragment mMovieGridFragment;
    private static final String MOVIE_FRAGMENT_TAG = "MOVIE_FRAGMENT_TAG";
    private static final String FAVORITES_FRAGMENT_TAG = "FAVORITES_FRAGMENT_TAG";
    private static final String SAVE_SPINNER_TAG = "SAVE_SPINNER_TAG";
    private Spinner mSpinner;

    public enum SpinnerSelection {POPULARITY, HIGHEST_RATED, FAVORITES}

    private SpinnerSelection mSpinnerSelection = SpinnerSelection.HIGHEST_RATED;

    private SharedPreferences mSharedPrefs;
    private static final String PREFS_SAVE_STATE = "save_state";

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

        /**
         *  Retrieve spinner position from before config change.
         *  If not saved, initialize it to POPULARITY.
         */
        mSharedPrefs = getSharedPreferences(PREFS_SAVE_STATE, Context.MODE_PRIVATE);
        int spinnerPosition = mSharedPrefs.getInt(SAVE_SPINNER_TAG, SpinnerSelection.POPULARITY.ordinal());
        mSpinnerSelection = SpinnerSelection.values()[spinnerPosition];

        setupStethoLibrary();
        setContentView(R.layout.layout_main);
        setupToolbar();
        setupSpinner();
        mIsTwoPane = checkForDualPane();
        setupMovieGridFragment(savedInstanceState);
        //@TODO take this out once app is working so data can persist
        //DatabaseUtils.clearDatabase(this);
    }

    /**
     * Save spinner selection preference.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putInt(SAVE_SPINNER_TAG, mSpinnerSelection.ordinal());
        editor.apply();
    }

/*    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        dataFragment.setData(collectMyLoadedData());
    }*/


    /**
     * Replace default toolbar with custom toolbar defined
     * in layouts/app_bar.xml
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * The main grid container has to be a DYNAMIC FRAGMENT because the app has two different
     * ones: one for Movies, which are stored in a List and displayed
     * using a regular Adapter; and another for Favorite Movies, which need to persist
     * and are stored in a database and loaded using a Loader and viewed using an
     * Adapter with a Cursor.
     *
     * @TODO select MovieMainGridFragment or FavoritesGridFragment based on user's current Spinner
     * selection (Movies vs Favorites)
     * http://developer.android.com/guide/topics/resources/runtime-changes.html#RetainingAnObject
     */
    private void setupMovieGridFragment(Bundle savedInstanceState) {
        // find the retained fragment on activity restart
        // or swap it out on Spinner selection change
        FragmentManager fragmentManager = getSupportFragmentManager();

        // There are TWO layouts available for DetailFragment:
        // one for landscape and one for large-landscape.
        mMovieDetailFragment = (DetailFragment) fragmentManager.findFragmentById(
                R.id.fragment_movie_detail);
        if (mSpinnerSelection == SpinnerSelection.FAVORITES) {
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_movie_grid_container, new FavoritesGridFragment())
                        .commit();
            }
        } else {
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_movie_grid_container, new MovieMainGridFragment(), MOVIE_FRAGMENT_TAG)
                        .commit();
                //Otherwise findFragmentByTag will return null in initMovieGrid()
                fragmentManager.executePendingTransactions();
            }
            initMovieGrid();
        }
    }

    /**
     * If spinner changes, check to see whether grid_fragment has to change
     * between MovieGrid and FavoritesGrid
     */
    private void swapGridFragments() {
        //Swap from FavoritesGrid to MovieGrid or vice versa
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mSpinnerSelection == SpinnerSelection.FAVORITES) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movie_grid_container, new FavoritesGridFragment())
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_movie_grid_container, new MovieMainGridFragment(), MOVIE_FRAGMENT_TAG)
                    .commit();
            fragmentManager.executePendingTransactions();
            initMovieGrid();
        }
    }

    private void initMovieGrid() {
        mMovieGridFragment = (MovieMainGridFragment) getSupportFragmentManager()
                .findFragmentByTag(MOVIE_FRAGMENT_TAG);
        mMovieGridFragment.setSortBy(mSpinnerSelection);
        mMovieGridFragment.downloadMovies();
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
        mSpinner = (Spinner) findViewById(R.id.spinner_sort_by);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_by_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(mSpinnerSelection.ordinal());
        SpinnerInteractionListener listener = new SpinnerInteractionListener();
        mSpinner.setOnTouchListener(listener);
        mSpinner.setOnItemSelectedListener(listener);
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

        /**
         * If user is changing between Favorites and either
         * POPULARITY or VOTE_AVERAGE, will have to
         * swap between Favorites and MovieGrid fragments.
         *
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int position, long id) {
            //Do nothing if spinner selection hasn't changed
            //if (position == mSpinnerPosition)
            if (mSpinnerSelection == SpinnerSelection.values()[position])
                return;
            if (isUserSelected) {
                //mSpinnerPosition = position;
                mSpinnerSelection = SpinnerSelection.values()[position];
                isUserSelected = false;
                swapGridFragments();
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
}
