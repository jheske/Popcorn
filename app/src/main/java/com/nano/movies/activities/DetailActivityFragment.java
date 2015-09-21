/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nano.movies.R;
import com.nano.movies.web.Movie;

import butterknife.BindString;
import butterknife.ButterKnife;


/**
 * This fragment attaches to DetailActivity only.
 * MainActivity uses the parent DetailFragment because
 * it doesn't show an image in its AppBar so doesn't
 * implement MovieDetailChangeListener
 */
public class DetailActivityFragment extends DetailFragment {
    @BindString(R.string.error_implement_method)
    String errorMissingMethod;
    int mLayoutId;

    // This callback interface that allows this Fragment to notify MainActivity when
    // user clicks on a List Item so MainActivity can have SelfieImageFragment
    // show the full-sized image.
    // DON'T FORGET TO DESTROY IT WHEN IN onDetach() OR IT WILL LEAK MEMORY
    public interface MovieDetailChangeListener {
        void onMovieDetailChanged(String backdropPath, String originalTitle);
    }

    private MovieDetailChangeListener mCallback = null;

    /**
     * The layout has to be selected according to the calling Activity.
     * MainActivity and DetailActivity each has its own layout, NOT
     * necessarily dependent on device size or orientation.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_activity_detail, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // The hosting Activity must implement
        // MovieSelectionListener callback interface.
        try {
            mCallback = (MovieDetailChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + errorMissingMethod
                    + "MovieDetailChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    protected void displayMovieDetails(Movie movie) {
        super.displayMovieDetails(movie);

        //This is not called in two-pane mode, only by DetailActivity
        //after the user selects a movie.
        //DetailActivity displays backdrop image in the AppBar
        mCallback.onMovieDetailChanged(movie.getBackdropPath(), movie.getOriginalTitle());
    }
}
