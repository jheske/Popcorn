package com.nano.movies.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nano.movies.R;
import com.nano.movies.adapters.MovieAdapterWithCursor;
import com.nano.movies.data.movie.MovieColumns;
import com.nano.movies.data.movie.MovieSelection;
import com.nano.movies.utils.DatabaseUtils;
import com.nano.movies.utils.FavoritesRecyclerTouchListener;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.TmdbResults;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FavoritesGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String TAG = FavoritesGridFragment.class.getSimpleName();

    private Context mActivityContext;

    private RecyclerView mRecyclerView;
    private MovieAdapterWithCursor mMovieAdapter;
    //private MovieAdapter mMovieAdapter;

    //Cursor loader variables
    private static final int MOVIE_LOADER = 0;
    private int mPosition = RecyclerView.NO_POSITION;

    //Manages communication between activities
    //and themoviedb.org service proxies
    private final Tmdb tmdbManager = new Tmdb();

    //State vars that must survive a config change.
    private Parcelable mLayoutManagerSavedState;
    private int mLastPosition = 0;
    private String mSortBy = MovieServiceProxy.POPULARITY_DESC;
    private List<Movie> mMovies = null;

    //Tags for storing/retrieving state on config change.
    private final String BUNDLE_RECYCLER_LAYOUT = "SaveLayoutState";
    private final String BUNDLE_LAST_POSITION = "SaveLastPosition";
    private final String BUNDLE_SORT_BY = "SaveSortBy";

    // Android recommends Fragments always communicate with each other
    // via the container Activity
    // @see https://developer.android.com/training/basics/fragments/communicating.html

    // This callback interface that allows this Fragment to notify MainActivity when
    // user clicks on a List Item so MainActivity can have SelfieImageFragment
    // show the full-sized image.
    // DON'T FORGET TO DESTROY IT WHEN IN onDetach() OR IT WILL LEAK MEMORY
    public interface MovieSelectionListener {
        void onMovieSelected(int movieId, boolean isUserSelected);
    }

    private MovieSelectionListener mCallback = null;

    public FavoritesGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mActivityContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        //The adapter has a cursor for linking the RecyclerView with the database
        mMovieAdapter = new MovieAdapterWithCursor(mActivityContext);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        //Layout is a grid with two columns or three, depending device orientation.
        if (getActivity().getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new GridLayoutManager(mActivityContext, 2));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(mActivityContext, 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.addOnItemTouchListener(new FavoritesRecyclerTouchListener(getActivity(),
                mRecyclerView, new ClickListener() {
            /**
             * onClick called back from the GestureDetector
             * mCallback calls into MainActivity which
             * implements this fragment's MovieSelectionListener
             * Note that MovieGridFragment works the same way.
             */
            @Override
            public void onClick(View view, int position) {
                //Move database cursor to new position
                mMovieAdapter.moveCursorToPosition(position);
                //Get latest movie info from the database
                Movie movie = mMovieAdapter.getItemAtPosition(position);
                //Call back to MainActivity to handle the click event
                //true = Movie selected by user
                mLastPosition = position;
                mCallback.onMovieSelected(movie.getId(), true);
            }
        }));
        return rootView;
    }

    /**
     * Will probably also need to add something like
     * onMoviesChanged() {
     * updateMovies();
     * getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
     * }
     * but that might just be if I'm using SyncAdapter
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mSortBy = savedInstanceState.getString(BUNDLE_SORT_BY);
            mLastPosition = savedInstanceState.getInt(BUNDLE_LAST_POSITION);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
            //    mMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES);
        } //else
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_LAST_POSITION, mLastPosition);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    /**
     * If this is called after a recent config change,
     * mLayoutManagerSavedState will hold pre-config state,
     * including the most recently viewed movie position
     * and the LayoutManager's state.
     * <p/>
     * Retrieve that information and reinitialize the
     * saved states.
     */
    private void restoreLayoutManagerPosition() {
        if (mLayoutManagerSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
        } else {
            mLastPosition = 0;
            mLayoutManagerSavedState = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // The hosting Activity must implement
        // MovieSelectionListener callback interface.
        try {
            mCallback = (MovieSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + getResources().getString(R.string.error_implement_method) + " MovieSelectionListener");
        }
        //Favorite movies are in the database so no download needed.
        //They should display automatically once Loader initializes.
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    public void displayMovieDetails() {
        //Tell main Activity that if it is in two-pane mode
        //it can display the movie at
        //mLastPosition, which will be 0 (first movie in the list)
        //if this is first time through.
        Movie movie = mMovieAdapter.getItemAtPosition(mLastPosition);
        //false = Movie not selected by user
        mCallback.onMovieSelected(movie.getId(), false);
    }

    /**
     * The CursorLoader derives from AsyncTaskLoader so it is
     * executed in a background thread.
     *
     * @param i
     * @param bundle
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // We want to get all the favorites, but the query can also be filtered.

        // Sort order:  Ascending, by title.
        String sortOrder = MovieColumns.ORIGINAL_TITLE + " ASC";
        MovieSelection movieSelection = new MovieSelection();
        //String[] projection = MovieColumns.ALL_COLUMNS;
        //MovieCursor cursor = movieSelection.query(getActivity().getContentResolver(), projection);
        //cursor.moveToPosition(0);
        //int count = cursor.getCount();
        //Movie movie = new Movie(cursor);
        //Log.d(TAG,cursor.getOriginalTitle());
        //Uri uri = movieSelection.uri();
        Loader<Cursor> loader = new CursorLoader(getActivity(),
                movieSelection.uri(),
                MovieColumns.ALL_COLUMNS,
                null,
                null,
                sortOrder);
        return loader;
    }

    /**
     * Called when the loader completes and the data is ready.
     * ** Call swapCursor to use the data in mForecastAdapter
     * ** Do any other UI updates based on the data.
     * <p/>
     * RecyclerView.NO_POSITION
     * http://stackoverflow.com/questions/29684154/recyclerview-viewholder-getlayoutposition-vs-getadapterposition
     *
     * @param loader
     * @param {data}
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int count = cursor.getCount();
        Log.d(TAG, "MovieCount = " + count);

        mMovieAdapter.swapCursor(cursor);
        mRecyclerView.smoothScrollToPosition(mLastPosition);
        mMovieAdapter.moveCursorToPosition(mLastPosition);
        displayMovieDetails();
    }

    /**
     * Called when the loader is being destroyed.
     * Remove all references to the loader data by
     * calling swapCursor(null)
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    /**
     * Set up interface to handle onClick
     * This could also handle have methods to handle
     * onLongPress, or other gestures.
     */
    public interface ClickListener {
        void onClick(View view, int position);
    }
}
