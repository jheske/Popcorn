package com.nano.movies.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nano.movies.R;
import com.nano.movies.adapters.TrailerAdapter;
import com.nano.movies.utils.DatabaseUtils;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Reviews;
import com.nano.movies.web.Tmdb;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This fragment attaches ONLY to MainActivity two-pane mode
 * MovieDetailActivity uses a different fragment, MovieDetailActivityFragment.
 */
public class MovieMainActivityDetailFragment extends Fragment {

    private final String TAG = MovieDetailActivityFragment.class.getSimpleName();

    private ImageView mImageViewThumbnail;
    private TextView mTextViewReleaseDate;
    private TextView mTextViewRuntime;
    private TextView mTextViewOverview;
    private TextView mTextViewVoteAverage;
    private RatingBar mRatingVoteAverage;
    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private TextView mTextViewReview1;
    private TextView mTextViewReview2;
    private TextView mTextViewReview3;
    private int mMovieId;
    private Movie mMovie;
    private final Tmdb tmdbManager = new Tmdb();
    // Tag for saving movie so it doesn't have to be re-downloaded on config change
    private final String BUNDLE_MOVIE = "SaveMovie";

    private TextView mTextViewTitle;
    // This callback interface that allows this Fragment to notify MainActivity when
    // user clicks on a List Item so MainActivity can have SelfieImageFragment
    // show the full-sized image.
    // DON'T FORGET TO DESTROY IT WHEN IN onDetach() OR IT WILL LEAK MEMORY
    //@TODO Abstract this into its own class
   /* public interface MovieDetailChangeListener {
        void onMovieDetailChanged(String backdropPath, String originalTitle);
    }

    private MovieDetailChangeListener mCallback = null;

    public MovieMainActivityDetailFragment() {
        setRetainInstance(true);
    } */

    /**
     * @TODO Hook up Favorite button
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mImageViewThumbnail = (ImageView) rootView.findViewById(R.id.img_thumbnail);
      //  mTextViewTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mTextViewReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        mTextViewRuntime = (TextView) rootView.findViewById(R.id.tv_runtime);
        mRatingVoteAverage = (RatingBar) rootView.findViewById(R.id.rating_bar_vote_average);
        mTextViewOverview = (TextView) rootView.findViewById(R.id.tv_overview);
        mTextViewVoteAverage = (TextView) rootView.findViewById(R.id.tv_vote_average);
        mTextViewReview1 = (TextView) rootView.findViewById(R.id.tv_review1);
        mTextViewReview2 = (TextView) rootView.findViewById(R.id.tv_review2);
        mTextViewReview3 = (TextView) rootView.findViewById(R.id.tv_review3);
        rootView.findViewById(R.id.btn_mark_fav).setOnClickListener(mOnClickListener);
        setupRecyclerView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(BUNDLE_MOVIE);
            if (mMovie != null)
                mMovieId = mMovie.getId();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_MOVIE, mMovie);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseUtils.insertMovie(getActivity(), mMovie);
        }
    };

    /*
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
    } */

    private void setupRecyclerView(View rootView) {
        Context context = getActivity();

        mTrailerAdapter = new TrailerAdapter(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_trailers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mTrailerAdapter);
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
        mTrailerAdapter.clear(true);
        mTrailerAdapter.addAll(movie.getTrailers().getYoutube());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        displayReviews(movie.getReviews().getResults());
        mTextViewReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        CharSequence runtime = Phrase.from(getActivity(), R.string.text_runtime)
                .put("runtime", movie.getRuntime().toString())
                .format();
        mTextViewRuntime.setText(runtime);
        mRatingVoteAverage.setRating(movie.getVoteAverage().floatValue());
        mTextViewOverview.setText(movie.getOverview());
        mTextViewVoteAverage.setText(movie.getVoteAverage().toString()
                + "/10 ("
                + movie.getVoteCount() + ")");
        loadPosterImage(movie);
        //Activity displays backdrop image in the AppBar
 //mCallback.onMovieDetailChanged(movie.getBackdropPath(), movie.getOriginalTitle());
    }

    /**
     * There's room for 3 reviews, then show MORE button.
     * MAYBE TRY fixed-height horizontal scrolling grid HERE??
     */
    private void displayReviews(List<Reviews.Review> reviews) {
        final int MAX_REVIEWS = 3;
        int reviewCount;
        Reviews.Review review;

        if (reviews == null) {
            mTextViewReview1.setText(getResources().getString(R.string.msg_no_reviews));
            return;
        }
        reviewCount = reviews.size();
        if (reviewCount == 0) {
            mTextViewReview1.setText(getResources().getString(R.string.msg_no_reviews));
            return;
        }

        // This is a fake because reviews are actually a "list"
        // but you can't have a vertical list inside of a ScrollView.
        // So punt after MAX_REVIEWS review and offer a "MORE REVIEWS" button.
        // THERE MUST BE A BETTER WAY!!!
        for (int i = 0; i < MAX_REVIEWS; i++) {
            if (i >= reviewCount)
                return;
            displayReview(reviews.get(i), i);
        }
    }

    private void displayReview(Reviews.Review review, int position) {
        if (review == null)
            return;
        switch (position) {
            case 0:
                mTextViewReview1.setText(review.getContent());
                break;
            case 1:
                mTextViewReview2.setText(review.getContent());
                break;
            case 2:
                mTextViewReview3.setText(review.getContent());
                break;
        }
    }

    private void loadPosterImage(Movie movie) {
        String movieImageUrl = Tmdb.getMoviePosterUrl(movie.getPosterPath(),
                Tmdb.IMAGE_POSTER_XSMALL);
        Picasso.with(getActivity()).load(movieImageUrl)
                .into(mImageViewThumbnail);
    }


}
