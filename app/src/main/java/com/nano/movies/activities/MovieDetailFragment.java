/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.activities;

import android.content.ContentUris;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nano.movies.R;
import com.nano.movies.data.movie.MovieContentValues;
import com.nano.movies.data.review.ReviewContentValues;
import com.nano.movies.data.trailer.TrailerColumns;
import com.nano.movies.data.trailer.TrailerContentValues;
import com.nano.movies.utils.DatabaseUtils;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Reviews;
import com.nano.movies.web.Reviews.Review;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.Trailers;
import com.nano.movies.web.Trailers.Trailer;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetailFragment extends Fragment {
    private final String TAG = "[MovieDetailFragment]";

    private ImageView mImageViewThumbnail;
    private TextView mTextViewTitle;
    private TextView mTextViewReleaseDate;
    private TextView mTextViewRuntime;
    private TextView mTextViewOverview;
    private TextView mTextViewVoteAverage;
    private RatingBar mRatingVoteAverage;

    private int mMovieId;
    private Movie mMovie;
    private final Tmdb tmdbManager = new Tmdb();

    // Tag for saving movie so it doesn't have to be re-downloaded on config change
    private final String BUNDLE_MOVIE = "SaveMovie";

    public MovieDetailFragment() {
        setRetainInstance(true);
    }

    /**
     * @TODO Hook up Favorite button
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mImageViewThumbnail = (ImageView) rootView.findViewById(R.id.img_thumbnail);
        mTextViewTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mTextViewReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        mTextViewRuntime = (TextView) rootView.findViewById(R.id.tv_runtime);
        mRatingVoteAverage = (RatingBar) rootView.findViewById(R.id.rating_bar_vote_average);
        mTextViewOverview = (TextView) rootView.findViewById(R.id.tv_overview);
        mTextViewVoteAverage = (TextView) rootView.findViewById(R.id.tv_vote_average);
        rootView.findViewById(R.id.btn_mark_fav).setOnClickListener(mOnClickListener);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //@TODO replace this with database persistence
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(BUNDLE_MOVIE);
            mMovieId = mMovie.getId();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //@TODO replace this with database persistence
        outState.putParcelable(BUNDLE_MOVIE, mMovie);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.showToast(getActivity(), "Click!!!");
            DatabaseUtils.insertFavoriteMovie(getActivity(), mMovie);
        }
    };

    /**
     * Called by MainActivity if Fragment already exists (two-pane mode),
     * or when the Fragment is created by its own separate activity
     * (MovieDetailActivity), in single-pane mode.
     */
    public void downloadMovie(int movieId) {
        // If user is displaying the same movie,
        // then don't download it again.
        if (mMovie != null) {
            if (mMovie.getId() == movieId) {
                displayMovieDetails(mMovie);
                return;
            }
        }
        //Member var so it's available in
        //callback for error handling
        mMovieId = movieId;
        tmdbManager.setIsDebug(false);
        tmdbManager.moviesServiceProxy().summary(movieId,
                MovieServiceProxy.REVIEWS_AND_TRAILERS,
                new Callback<Movie>() {
                    @Override
                    public void success(Movie movie, Response response) {
                        mMovie = movie;
                        displayMovieDetails(movie);
                        Log.i(TAG, "Success!! Movie title = " + movie.getOriginalTitle());
                        Log.i(TAG, "There are "
                                + movie.getTrailerCount() + " trailers and "
                                + movie.getReviewCount() + " reviews");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // Handle errors here.
                        Utils.showToast(getActivity(), "Failed to download movie " + mMovieId);
                    }
                });
    }

    private void displayMovieDetails(Movie movie) {
        mTextViewTitle.setText(movie.getOriginalTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        mTextViewReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        CharSequence runtime = Phrase.from(getActivity(), R.string.text_runtime)
                .put("runtime", movie.getRuntime().toString())
                .format();
        mTextViewRuntime.setText(runtime);
        mRatingVoteAverage.setRating(movie.getVoteAverage().floatValue());
        mTextViewOverview.setText(movie.getOverview());
        mTextViewVoteAverage.setText(movie.getVoteAverage().toString()
                + "/10 "
                + getVoteCountStr(movie.getVoteCount()));
        String movieImageUrl = Tmdb.getMovieImageUrl(movie.getPosterPath(),
                Tmdb.IMAGE_POSTER_SMALL);
        Picasso.with(getActivity()).load(movieImageUrl)
                .into(mImageViewThumbnail);
    }

    private CharSequence getVoteCountStr(int voteCount) {
        CharSequence voteStr;

        if (voteCount == 1) {
            voteStr = "(" + Phrase.from(getActivity(), R.string.text_vote)
                    .format() + ")";
        } else
            voteStr = "(" + Phrase.from(getActivity(), R.string.text_votes)
                    .put("votes", voteCount)
                    .format() + ")";
        return voteStr;
    }

}
