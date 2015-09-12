package com.nano.movies.activities;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.nano.movies.R;
import com.nano.movies.adapters.MovieStatePagerAdapter;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieService;

public class MainActivity extends AppCompatActivity
        implements MovieGridFragment.MovieSelectionListener,
        FavoritesGridFragment.MovieSelectionListener {
    private final String TAG = getClass().getSimpleName();

    private boolean mIsTwoPane = false;

    private DetailFragment mMovieDetailFragment;
    private SharedPreferences mSharedPrefs;
    private static final String PREFS_SAVE_STATE = "save_state";
    public static final int MOST_POPULAR = 0;
    public static final int HIGHEST_RATED = 1;
    public static final int FAVORITES = 2;
    private int[] mMovieIds = new int[]{0, 0, 0};

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    MoviePagerAdapter mPagerAdapter;


    /**
     * Android will load either
     * activity_main_one_pane_old_old.xml
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
        setupStethoLibrary();
        setContentView(R.layout.layout_activity_main);
        setupToolbar();
        mIsTwoPane = checkForDualPane();
        setupViewPager();
    }

    /**
     * Replace default toolbar with custom toolbar defined
     * in layouts/app_bar.xml
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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


    private int getMovieId(int position) {
        return mMovieIds[position];
    }

    public boolean onRegisterMovie(int position, int movieId) {
        boolean firstMovie=false;
        //This is a boundary condition to handle displaying the first movie
        //if app comes up in two-pane mode, before any of the fragments
        // register their movies
        //onMovieSelected(position, movieId, false);
        if ((mMovieIds[position] == 0) && mIsTwoPane)
            firstMovie = true;
        mMovieIds[position] = movieId;
        return firstMovie;
    }


    /**
     * Display Movie details.
     * If two-pane mode, then direct already-existing Fragment
     * to download details for the selected movie.
     * If one-pane, then start a new Activity tod
     * display the movie on its own screen.
     */
    public void onMovieSelected(int position, int movieId, boolean isUserSelected) {
        //This is only true if the movie has never been registered
        //boolean displayMovie = registerMovieId(position, movieId);
        if (movieId == 0)
            return;
        if (mIsTwoPane && (position == mViewPager.getCurrentItem())) {
            mMovieDetailFragment.downloadMovie(movieId);
        } else {
            //Don't display movie unless user selected it.
            if (isUserSelected) {
                startMovieDetailActivity(movieId);
            }
        }
    }

    /**
     * The networks is not available, so the Favorite movie
     * was taken from the database.
     */
    public void onCachedFavoriteSelected(Movie movie, boolean isUserSelected) {
//        onRegisterMovieId(FRAGMENT_FAVORITES, movie.getId());
        if (mIsTwoPane) {
            mMovieDetailFragment.displayMovieDetails(movie);
        } else {
            if (isUserSelected)
                startMovieDetailActivity(movie);
        }
    }

    private void startMovieDetailActivity(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DetailActivity.MOVIE_EXTRA, movie);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private void startMovieDetailActivity(int movieId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_ID_EXTRA, movieId);
        this.startActivity(intent);
    }

    // Extend from a MovieStatePagerAdapter now instead for more dynamic ViewPager items
    public static class MoviePagerAdapter extends MovieStatePagerAdapter {
        private static int NUM_ITEMS = 3;

        private String mTabTitles[] = new String[]{"Most Popular", "Highest Rated", "Favorites"};

        public MoviePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case MOST_POPULAR:
                    return MovieGridFragment.newInstance(MovieService.POPULARITY_DESC);
                case HIGHEST_RATED:
                    return MovieGridFragment.newInstance(MovieService.VOTE_AVERAGE_DESC);
                case FAVORITES:
                    return FavoritesGridFragment.newInstance();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

    }

    private void setupViewPager() {
        mMovieDetailFragment =
                (DetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_movie_detail);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                //This is the workaround for the null fragment issue
                int movieId = getMovieId(position);
                //@TODO FIX THIS !!
                //Retrieve associated fragment and
                //Get its current movieId so
                //the movie can be displayed in DetailFragment (if in two-pane mode).
                switch (position) {
                    case MOST_POPULAR:
                    case HIGHEST_RATED:
                        //@TODO find out why the fragment is sometimes null on config changes
//                        MovieGridFragment movieFragment =
//                                (MovieGridFragment) mPagerAdapter
//                                        .getRegisteredFragment(position);
//                        movieId = movieFragment.getLatestMovieId();
                        break;
                    case FAVORITES:
                        //@TODO find out why the fragment is sometimes null on config changes
//                        FavoritesGridFragment favoritesFragment =
//                                (FavoritesGridFragment) mPagerAdapter.getRegisteredFragment(position);
//                        movieId = favoritesFragment.getLatestMovieId();
//                        ((FavoritesGridFragment)fragment).selectCurrentMovie(false);
                        break;
                }
                onMovieSelected(position, movieId, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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

