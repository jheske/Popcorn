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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nano.movies.R;
import com.nano.movies.adapters.TrailerAdapter;
import com.nano.movies.utils.DatabaseUtils;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieService;
import com.nano.movies.web.Reviews.Review;
import com.nano.movies.web.Tmdb;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @Bind(R.id.tv_mpaa_rating)
    protected TextView mTextViewMpaaRating;
    @Bind(R.id.rating_bar_vote_average)
    protected RatingBar mRatingVoteAverage;
    @Bind(R.id.tv_reviews)
    protected TextView mTextViewReviews;
    @Nullable
    @Bind(R.id.tv_movie_title)
    protected TextView mTextViewTitle;

    protected RecyclerView mRecyclerView;
    protected TrailerAdapter mTrailerAdapter;
    protected int mMovieId;
    protected Movie mMovie;

    // Tag for saving movie so it doesn't have to be re-downloaded on config change
    protected final String BUNDLE_MOVIE = "SaveMovie";

    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;
    @BindString(R.string.msg_no_reviews)
    String msgNoReviews;
    @Bind(R.id.btn_mark_fav)
    Button btnMarkFavorite;

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
        ButterKnife.bind(this, rootView);
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

    @OnClick(R.id.btn_mark_fav)
    final void favoritesbuttonClick() {
        Log.i(TAG, "Adding movie to favorites " + mMovie.getOriginalTitle());
        DatabaseUtils.insertMovie(getActivity(), mMovie);
    }

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

    private Tmdb getTmdbApp() {
        return (Tmdb) getActivity().getApplication();
    }

    private void setShareTrailerIntent() {
        if (mMovie.getTrailers().getYoutube().size() == 0)
            return;
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
        Tmdb tmdbManager = getTmdbApp();
        MovieService movieService = tmdbManager.getMovieService();
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
        movieService.summary(movieId,
                MovieService.REVIEWS_AND_TRAILERS,
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

    /**
     * This requires a lot of NULL checking because
     * movies will often have missing fields,
     * especially trailers and reviews.
     *
     * @param movie
     */
    protected void displayMovieDetails(Movie movie) {
        Log.d(TAG, "Display movie " + movie.getId() + "  " + movie.getOriginalTitle());
        if (mTextViewTitle.getVisibility() == View.VISIBLE)
            mTextViewTitle.setText(movie.getOriginalTitle());
        if (movie.getTrailers() != null) {
            mTrailerAdapter.clear(true);
            mTrailerAdapter.addAll(movie.getTrailers().getYoutube());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        displayReviews(movie.getReviews().getResults());
        if (movie.getReleaseDate() == null)
            Log.d(TAG, "NULL RELEASE DATE");
        else
            mTextViewReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        CharSequence runtime = Phrase.from(getActivity(), R.string.text_runtime)
                .put("runtime", movie.getRuntime().toString())
                .format();
        mTextViewRuntime.setText(runtime);
        mRatingVoteAverage.setRating(movie.getVoteAverage().floatValue());
        mTextViewOverview.setText(movie.getOverview());
        loadPosterImage(movie);
    }

    //There's room for 3 reviews, then show MORE button.
    //MAYBE TRY fixed-height horizontal scrolling grid HERE??
    //See http://android--examples.blogspot.com/2015/01/textview-new-line-multiline-in-android.html
    private void displayReviews(List<Review> reviews) {
        final int MAX_REVIEWS = 3;
        int reviewCount;

        if (reviews == null) {
            mTextViewReviews.setText(msgNoReviews);
            return;
        }
        reviewCount = reviews.size();
        if (reviewCount == 0) {
            mTextViewReviews.setText(msgNoReviews);
            return;
        }
        displayReview(reviews.get(0));

        // This is a BIG FAKE because reviews is actually a "list" of Review items,
        // but you can't have a vertical list inside of a ScrollView.
        for (int i = 1; i < MAX_REVIEWS; i++) {
            if (i >= reviewCount)
                return;
            //define new line by append android system line separator
            mTextViewReviews.append(System.getProperty("line.separator"));
            mTextViewReviews.append(System.getProperty("line.separator"));
            displayReview(reviews.get(i));
        }
    }

    private void displayReview(Review review) {
        mTextViewReviews.setText(review.getAuthor());
        mTextViewReviews.append(System.getProperty("line.separator"));
        mTextViewReviews.append(review.getContent());
    }

    private void loadPosterImage(Movie movie) {
        String movieImageUrl = Tmdb.getMoviePosterUrl(movie.getPosterPath(),
                Tmdb.IMAGE_POSTER_XSMALL);
        Picasso.with(getActivity()).load(movieImageUrl)
                .into(mImageViewThumbnail);
    }
}
