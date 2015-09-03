/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.app.Activity;

import com.nano.movies.R;
import com.nano.movies.web.Movie;


/**
 * This fragment attaches to MovieDetailActivity only.
 * MainActivity uses the parent DetailFragment because
 * it doesn't show an image in its AppBar so it doesn't
 * implement MovieDetailChangeListener
 */
public class DetailActivityFragment extends DetailFragment {
    // This callback interface that allows this Fragment to notify MainActivity when
    // user clicks on a List Item so MainActivity can have SelfieImageFragment
    // show the full-sized image.
    // DON'T FORGET TO DESTROY IT WHEN IN onDetach() OR IT WILL LEAK MEMORY
    public interface MovieDetailChangeListener {
        void onMovieDetailChanged(String backdropPath, String originalTitle);
    }

    private MovieDetailChangeListener mCallback = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // The hosting Activity must implement
        // MovieSelectionListener callback interface.
        try {
            mCallback = (MovieDetailChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + getResources().getString(R.string.error_implement_method)
                    + " MovieDetailChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    protected void displayMovieDetails(Movie movie) {
        super.displayMovieDetails(movie);

        //Activity displays backdrop image in the AppBar
        mCallback.onMovieDetailChanged(movie.getBackdropPath(), movie.getOriginalTitle());
    }
}
