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

    private final static String TRAILER_ORIGIN_YOUTUBE="youtube";
    private final static String TRAILER_ORIGIN_QUICKTIME="quicktime";

    /* @TODO hook up Favorites button
     * for P1, Stage 2
     */
    private Button mButtonFavorite;
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
            dbInsertFavoriteMovie();
        }
    };

    /**
     *
     * Insert movie first, which returns its primary key
     * to use as the movie_id foreign key when inserting trailers and reviews.
     *
     * Then insert Reviews and Trailers.  A movie has one list of reviews and
     * two lists of trailers (Youtube and Quicktime).
     * Reviews and/or trailers may be empty.
     */
    private void dbInsertFavoriteMovie() {
        long movieId = dbInsertMovie();
        insertTrailers(mMovie.getTrailers().getQuicktime(), TRAILER_ORIGIN_QUICKTIME,movieId);
        insertTrailers(mMovie.getTrailers().getYoutube(),TRAILER_ORIGIN_YOUTUBE , movieId);
        insertReviews(mMovie.getReviews().getResults(),movieId);
    }

    /**
     * Insert a movie.
     *
     * @return the id of the created movie. It will be used as
     * foreign key when inserting reviews and trailers
     */
    private long dbInsertMovie() {
        MovieContentValues values = new MovieContentValues();
        values.putTmdbId(mMovie.getId());
        values.putHomepage(mMovie.getHomePage());
        values.putOriginalTitle(mMovie.getOriginalTitle());
        values.putOverview(mMovie.getOverview());
        values.putPopularity(mMovie.getPopularity());
        values.putPosterPath(mMovie.getOverview());
        values.putReleaseDate(mMovie.getReleaseDate());
        values.putRuntime(mMovie.getRuntime());
        values.putTagline(mMovie.getTagline());
        values.putTitle(mMovie.getTitle());
        values.putVoteAverage(mMovie.getVoteAverage());
        values.putVoteCount(mMovie.getVoteCount());

        Uri uri = values.insert(getActivity().getContentResolver());
        return ContentUris.parseId(uri);
    }

    /**
     *
     * @param movieId The primary key of the movie in Movie table. It's
     *                a foreign key in Reviews and Trailers tables (column = tmdb_id).
     */
    private void insertTrailers(Long movieId) {
    }

    /**
     * Insert reviews into Trailers table. This table has
     * foreign key movieId, (table column = tmdb_id) into the Movie table.
     *
     * @param trailers Will be empty if movie has no trailers.
     * @param movieId
     * @param origin Trailer is on either Youtube or Quicktime
     */
    private void insertTrailers(List<Trailer> trailers,String origin, Long movieId) {
        Trailer trailer;

        if (trailers.size() == 0)
            return;
        for (int i = 0; i < trailers.size(); i++) {
            trailer = trailers.get(i);
            dbInsertTrailer(trailer,origin,movieId);
        }
    }

    private long dbInsertTrailer(Trailer trailer, String origin, Long movieId) {
        TrailerContentValues values = new TrailerContentValues();
        values.putMovieId(movieId);
        values.putName(trailer.getName());
        values.putSize(trailer.getSize());
        values.putSource(trailer.getSource());
        values.putType(trailer.getType());
        values.putOrigin(origin);

        Uri uri = values.insert(getActivity().getContentResolver());
        return ContentUris.parseId(uri);
    }

    /**
     * Insert reviews into Reviews table. This table has
     * foreign key movieId, (table column = tmdb_id) into the Movie table.
     *
     * @param reviews Will be empty if movie has no reviews.
     * @param movieId
     */
    private void insertReviews(List<Reviews.Review> reviews,Long movieId) {
        Review review;

        if (reviews.size() == 0)
            return;
        for (int i = 0; i < reviews.size(); i++) {
            review = reviews.get(i);
            dbInsertReview(review,movieId);
        }
    }

    /**
     *
     * @param trailer
     * @param movieId
     * @return
     */
    private long dbInsertReview(Review trailer,Long movieId) {
        ReviewContentValues values = new ReviewContentValues();

        values.putMovieId(movieId);
        values.putReviewId(trailer.getId());
        values.putAuthor(trailer.getAuthor());
        values.putContent(trailer.getContent());
        values.putUrl(trailer.getUrl());
        values.putMovieId(movieId);

        Uri uri = values.insert(getActivity().getContentResolver());
        return ContentUris.parseId(uri);
    }

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
