/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nano.movies.R;
import com.nano.movies.data.MovieAdapter;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.TmdbResults;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieGridFragment extends Fragment {
    private final String TAG = "[MovieGridFragment]";

    private Context mActivityContext;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    //Manages communication between activities
    //and themoviedb.org service proxies
    private final Tmdb tmdbManager = new Tmdb();

    //State vars that must survive a config change.
    private Parcelable mLayoutManagerSavedState;
    private int mLastPosition = 0;
    private String mSortBy = MovieServiceProxy.POPULARITY_DESC;

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

    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivityContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        // Grid with 2 columns
        mMovieAdapter = new MovieAdapter(mActivityContext);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        //Show two columns or three, depending device orientation.
        if (getActivity().getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new GridLayoutManager(mActivityContext, 2));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(mActivityContext, 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = mMovieAdapter.getItemAtPosition(position);
                //Call back to MainActivity to handle the click event
                //true = Movie selected by user
                mLastPosition = position;
                mCallback.onMovieSelected(movie.getId(), true);
            }
        }));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mSortBy = savedInstanceState.getString(BUNDLE_SORT_BY);
            mLastPosition = savedInstanceState.getInt(BUNDLE_LAST_POSITION);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
        }
        downloadMovies();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SORT_BY, mSortBy);
        outState.putInt(BUNDLE_LAST_POSITION, mLastPosition);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    /**
     * If this is called after a recent config change,
     * mLayoutManagerSavedState will hold pre-config state,
     * including the most recently viewed movie position
     * and the LayoutManager's state.
     *
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

    public void downloadMovies() {
        tmdbManager.setIsDebug(false);
        tmdbManager.moviesServiceProxy().discoverMovies(1, mSortBy, new Callback<TmdbResults>() {
            @Override
            public void success(TmdbResults results, Response response) {
                displayPosters(results.results);
                //Tell main Activity it can display the first movie in the list
                //if MovieDetailFragment exists (two-pane mode).
                //False = Movie not selected by user
                restoreLayoutManagerPosition();
                Movie movie = mMovieAdapter.getItemAtPosition(mLastPosition);
                mCallback.onMovieSelected(movie.getId(), false);
            }

            @Override
            public void failure(RetrofitError error) {
                // Handle errors here
                String errorMsg = getResources()
                        .getString(R.string.error_download_movies_failed);
                Utils.showToast(getActivity(), errorMsg);
                Log.i(TAG, errorMsg);
            }
        });
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
    }

    private void displayPosters(List<Movie> movies) {
        mMovieAdapter.clear();
        mMovieAdapter.addAll(movies);
    }

    public void setSortBy(String sortBy) {
        mSortBy = sortBy;
        mLastPosition = 0;
    }

    /**
     * Set up interface to handle onClick
     * This could also handle have methods to handle
     * onLongPress, or other gestures.
     */
    public interface ClickListener {
         void onClick(View view, int position);
    }


    /*****
     * The RecyclerTouchListener class sets up the RecyclerView's gesture
     * management. It intercepts and handles user clicks on grid items
     */
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector mGestureDetector;
        private ClickListener mClickListener;

        /**
         * Set up Simple listener to detect singleTapUp .  I can add
         * additional gestures later, like onLongPress, if I want.
         */
        public RecyclerTouchListener(Context context, RecyclerView recyclerView, ClickListener clickListener) {
            mClickListener = clickListener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //True = this view has handled the event so
                    //it won't be propagated any farther.
                    return true;
                }
            });
        }

        /**
         * Required method called only for ViewGroups (like RecyclerView),
         * not for plain Views (like TextViews).
         * Handle the RecyclerView's grid item click event here.
         *
         */
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mClickListener != null && mGestureDetector.onTouchEvent(e)) {
                mClickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        /**
         * Required method called on View where very first touch occurs.
         *
         */
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        /**
         * Required method called when a child of RecyclerView does not want
         * RecyclerView and its ancestors to intercept touch events.
         */
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
