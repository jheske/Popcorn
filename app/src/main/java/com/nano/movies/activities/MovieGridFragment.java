/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.SerializedName;
import com.nano.movies.R;
import com.nano.movies.adapters.MovieAdapter;
import com.nano.movies.utils.MovieRecyclerTouchListener;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieService;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.TmdbResults;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieGridFragment extends ErrorHandlerFragment {
    private final String TAG = MovieGridFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    //State vars that must survive a config change.
    private Parcelable mLayoutManagerSavedState;
    private int mLastPosition = 0;
    private String mSortBy;
    private List<Movie> mMovies = null;
    private ProgressDialog mProgressDialog;


    //Tags for storing/retrieving state on config change.
    private final String BUNDLE_RECYCLER_LAYOUT = "SaveLayoutState";
    private final String BUNDLE_LAST_POSITION = "SaveLastPosition";
    private final String BUNDLE_SORT_BY = "SaveSortBy";
    private final String BUNDLE_MOVIES = "SaveMovies";
    @BindString(R.string.error_download_movie_failed)
    String errorDownloadFailed;
    @BindString(R.string.error_implement_method)
    String errorMissingMethod;

    // Android recommends Fragments always communicate with each other
    // via the container Activity
    // @see https://developer.android.com/training/basics/fragments/communicating.html

    // This callback interface that allows this Fragment to notify MainActivity when
    // user clicks on a List Item so MainActivity can have SelfieImageFragment
    // show the full-sized image.
    // DON'T FORGET TO DESTROY IT WHEN IN onDetach() OR IT WILL LEAK MEMORY
    public interface MovieSelectionListener {
        boolean onRegisterMovie(int position, int movieId);
        void onMovieSelected(int position, int movieId, boolean isUserSelected);
    }

    private MovieSelectionListener mCallback = null;

    public static MovieGridFragment newInstance(String sortBy) {
        MovieGridFragment fragment = new MovieGridFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SORT_BY", sortBy);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        // Grid with 2 columns
        Context activityContext = getActivity();

        if (getArguments() != null)
            mSortBy = getArguments().getString("SORT_BY");
        else
            Log.d(TAG, "No args!!!!");
        mMovieAdapter = new MovieAdapter(activityContext);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        //Show two columns or three, depending device orientation.
        if (getActivity().getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new GridLayoutManager(activityContext, 1));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(activityContext, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.addOnItemTouchListener(new MovieRecyclerTouchListener(getActivity(),
                mRecyclerView, new ClickListener() {
            /**
             * onClick called back from the GestureDetector
             */
            @Override
            public void onClick(View view, int position) {
                //Get movie info from the adapter
                mLastPosition = position;
                Movie movie = mMovieAdapter.getItemAtPosition(mLastPosition);
                if (mSortBy.equals(MovieService.POPULARITY_DESC))
                    mCallback.onRegisterMovie(MainActivity.MOST_POPULAR, movie.getId());
                else
                    mCallback.onRegisterMovie(MainActivity.HIGHEST_RATED, movie.getId());
                selectCurrentMovie(true);
            }
        }));
        return rootView;
    }

    /**
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
            mMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES);
        }
        if (mMovies != null)
            displayPosters();
        else
            downloadMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SORT_BY, mSortBy);
        outState.putInt(BUNDLE_LAST_POSITION, mLastPosition);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(BUNDLE_MOVIES, (ArrayList) mMovies);
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
        }
    }

    private Tmdb getTmdbApp() {
        return (Tmdb) getActivity().getApplication();
    }

    /**
     * Useful for debugging
     */
    class RestError {
        @SerializedName("code")
        public int code;
        @SerializedName("error")
        public String errorDetails;
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * Called after parent Activity is created,
     * or after when changes Spinner selection
     * to either Popular or Highest-rated movies
     */
    public void downloadMovies() {
        //If mMovies exists, posters were already displayed
        //do don't download them again.
        if (mMovies != null)
            return;

        showProgressDialog();
        Tmdb tmdbManager = getTmdbApp();
        MovieService movieService = tmdbManager.getMovieService();
        tmdbManager.setIsDebug(false);
        movieService.discoverMovies(1, mSortBy, new Callback<TmdbResults>() {
            @Override
            public void success(TmdbResults results, Response response) {
                //Save movies and stash/restore then on
                //config changes so we can avoid a needless api call.
                mMovies = results.results;
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                displayPosters();
            }

            /**
             * A Json Syntax error can be a HUGE PAIN to debug.
             * Set a breakpoint in Gson.java fromJSon. When error occurs
             * look at the Variables trace for the "throwable.cause"
             *
             * @param error
             */
            //Errors are handled by an ApiErrorHandler,
            //set in Tmdb.RestAdapter.Builder.setErrorHandler
            @Override
            public void failure(RetrofitError error) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.i(TAG, error.getMessage() + " kind = " + error.getKind());
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // The hosting Activity must implement
        // MovieSelectionListener callback interface.
        try {
            mCallback = (MovieSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + errorMissingMethod
                    + " MovieSelectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void displayPosters() {
        mMovieAdapter.clear();
        mMovieAdapter.addAll(mMovies);
        //Tell main Activity that if it is in two-pane mode
        //it can display the movie at
        //mLastPosition, which will be 0 (first movie in the list)
        //if this is first time through.
        restoreLayoutManagerPosition();
        //false = this movie is being displayed as part of screen setup, not
        //as result of user selection
        if (registerCurrentMovie())
          selectCurrentMovie(false);
    }


    //Call back to MainActivity to handle the click event,
    //which will request a more detailed version of the Movie object.
    public boolean registerCurrentMovie() {
        Movie movie = mMovieAdapter.getItemAtPosition(mLastPosition);
        if (mSortBy.equals(MovieService.POPULARITY_DESC))
            return mCallback.onRegisterMovie(MainActivity.MOST_POPULAR,movie.getId());
        else
            return mCallback.onRegisterMovie(MainActivity.HIGHEST_RATED,movie.getId());
    }


    //Call back to MainActivity to handle the click event,
    //which will request a more detailed version of the Movie object.
    public void selectCurrentMovie(boolean isUserSelected) {
        Movie movie = mMovieAdapter.getItemAtPosition(mLastPosition);
        if (mSortBy.equals(MovieService.POPULARITY_DESC))
            mCallback.onMovieSelected(MainActivity.MOST_POPULAR,movie.getId(), isUserSelected);
        else
            mCallback.onMovieSelected(MainActivity.HIGHEST_RATED, movie.getId(), isUserSelected);
    }

    /**
     * Set up interface to handle onClick
     * This could also handle have methods to handle
     * onLongPress, or other gestures.
     */
    public interface ClickListener {
        void onClick(View view, int position);
    }

    public int getLastPosition() {
        return mLastPosition;
    }
}
