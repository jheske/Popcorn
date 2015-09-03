package com.nano.movies.activities;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.nano.movies.R;
import com.nano.movies.web.MovieServiceProxy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Tabs extends AppCompatActivity
        implements MovieMainGridFragment.MovieSelectionListener,
        FavoritesGridFragment.MovieSelectionListener {
    private final String TAG = getClass().getSimpleName();

    private boolean mIsTwoPane = false;

    private DetailFragment mMovieDetailFragment;
    private static final String MOVIE_FRAGMENT_TAG = "MOVIE_FRAGMENT_TAG";
    private static final String FAVORITES_FRAGMENT_TAG = "FAVORITES_FRAGMENT_TAG";
    private static final String SAVE_SPINNER_TAG = "SAVE_SPINNER_TAG";
    private Adapter mPagerAdapter;

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
//        mSharedPrefs = getSharedPreferences(PREFS_SAVE_STATE, Context.MODE_PRIVATE);

        setupStethoLibrary();
        setContentView(R.layout.layout_main);
        setupToolbar();
        mIsTwoPane = checkForDualPane();
        setupViewPager();
    }

    /**
     * Save spinner selection preference.
     */
    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences.Editor editor = mSharedPrefs.edit();
//        editor.putInt(SAVE_SPINNER_TAG, mSpinnerSelection.ordinal());
//        editor.apply();
    }

    /**
     * Replace default toolbar with custom toolbar defined
     * in layouts/app_bar.xml
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //JEH So we can swipe thru Tabs
    private void setupViewPager() {
        mMovieDetailFragment =
                (DetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_movie_detail);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new Adapter(getSupportFragmentManager());
        //Most Popular Fragment
        MovieMainGridFragment popularFragment = new MovieMainGridFragment();
        Bundle args = new Bundle();
        args.putString("SORT_BY", MovieServiceProxy.POPULARITY_DESC);
        popularFragment.setArguments(args);
        mPagerAdapter.addFragment(popularFragment, "Most Popular");
        //Voter Average Fragment
        MovieMainGridFragment voteAvgFragment = new MovieMainGridFragment();
        args = new Bundle();
        args.putString("SORT_BY", MovieServiceProxy.VOTE_AVERAGE_DESC);
        voteAvgFragment.setArguments(args);
        mPagerAdapter.addFragment(voteAvgFragment, "Highest Rated");
        viewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
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
     * Display Movie details.
     * If two-pane mode, then direct already-existing Fragment
     * to download details for the selected movie.
     * If one-pane, then start a new Activity tod
     * display the movie on its own screen.
     */
    public void onMovieSelected(int movieId, boolean isUserSelected) {
        //Gotta figure out which Page
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