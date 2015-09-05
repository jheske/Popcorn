package com.nano.movies.activities;

import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This fragment attaches ONLY to MainActivity two-pane mode
 * DetailActivity uses a different fragment, DetailActivityFragment.
 */
public class DetailFragment extends Fragment {
    protected final String TAG = DetailActivityFragment.class.getSimpleName();

    @Bind(R.id.img_thumbnail)
    protected ImageView mImageViewThumbnail;
    @Bind(R.id.tv_release_date)
    protected TextView mTextViewReleaseDate;
    @Bind(R.id.tv_runtime)
    protected TextView mTextViewRuntime;
    @Bind(R.id.tv_overview)
    protected TextView mTextViewOverview;
    @Bind(R.id.tv_vote_average)
    protected TextView mTextViewVoteAverage;
    @Bind(R.id.rating_bar_vote_average)
    protected RatingBar mRatingVoteAverage;
    @Bind(R.id.tv_review1)
    protected TextView mTextViewReview1;
    @Bind(R.id.tv_review2)
    protected TextView mTextViewReview2;
    @Bind(R.id.tv_review3)
    protected TextView mTextViewReview3;
    @Nullable
    @Bind(R.id.tv_movie_title)
    protected TextView mTextViewTitle;
    protected RecyclerView mRecyclerView;
    protected TrailerAdapter mTrailerAdapter;
    protected int mMovieId;
    protected Movie mMovie;
    protected final Tmdb tmdbManager = new Tmdb();
    // Tag for saving movie so it doesn't have to be re-downloaded on config change
    protected final String BUNDLE_MOVIE = "SaveMovie";

    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;
    @BindString(R.string.msg_no_reviews)
    String msgNoReviews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this,rootView);
        rootView.findViewById(R.id.btn_mark_fav).setOnClickListener(mOnClickListener);
        setupRecyclerView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(BUNDLE_MOVIE);
            if (mMovie != null) {
                mMovieId = mMovie.getId();
                setShareTrailerIntent();
            }
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

    //http://stackoverflow.com/questions/25093706/shareactionprovider-is-null
    //https://github.com/commonsguy/cw-omnibus/blob/ef269a785353b9dc2704aee9f7bc3b16abf186cc/EmPubLite/T15-Share/src/com/commonsware/empublite/NoteFragment.java
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_fragment, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(mShareIntent);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setShareTrailerIntent() {
        if (mShareActionProvider == null) {
            Log.d(TAG, "Null ShareActionProvider");
            return;
        }
        String trailerPath = mMovie.getTrailers().getYoutube().get(0).getSource();
        mShareIntent.putExtra(Intent.EXTRA_TEXT, Tmdb.getYoutubeUrl(trailerPath))
                .setType("text/plain");
    }

    private void setupRecyclerView(View rootView) {
        mTrailerAdapter = new TrailerAdapter(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_trailers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mTrailerAdapter);
    }

    //Called by MainActivity if Fragment already exists (two-pane mode),
    //or when the Fragment is created by its own separate activity
    //(DetailActivity), in single-pane mode.
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
                        setShareTrailerIntent();
                        //String trailerPath = mMovie.getTrailers().getYoutube().get(0).getSource();
                        //mShareIntent.putExtra(Intent.EXTRA_TEXT,Tmdb.getYoutubeUrl(trailerPath));
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

    protected void displayMovieDetails(Movie movie) {
        if (mTextViewTitle.getVisibility() == View.VISIBLE)
            mTextViewTitle.setText(movie.getOriginalTitle());
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
    }

    //There's room for 3 reviews, then show MORE button.
    //MAYBE TRY fixed-height horizontal scrolling grid HERE??
    private void displayReviews(List<Reviews.Review> reviews) {
        final int MAX_REVIEWS = 3;
        int reviewCount;

        if (reviews == null) {
            mTextViewReview1.setText(msgNoReviews);
            return;
        }
        reviewCount = reviews.size();
        if (reviewCount == 0) {
            mTextViewReview1.setText(msgNoReviews);
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
                mTextViewReview2.setVisibility(View.VISIBLE);
                mTextViewReview2.setText(review.getContent());
                break;
            case 2:
                mTextViewReview3.setVisibility(View.VISIBLE);
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
