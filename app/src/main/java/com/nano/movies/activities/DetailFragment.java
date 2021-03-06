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
import com.nano.movies.web.Releases;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieService;
import com.nano.movies.web.Reviews;
import com.nano.movies.web.Reviews.Review;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.Trailers;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindString;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This fragment attaches ONLY to MainActivity two-pane mode
 * DetailActivity uses a different fragment, DetailActivityFragment.
 */

/**
 * This fragment handles most of the logic for DetailActivity and
 * DetailFragment (right-hand pane in MainActivity in two-pane).
 */
public class DetailFragment extends Fragment {
    protected final String TAG = DetailActivityFragment.class.getSimpleName();

    @BindView(R.id.img_thumbnail)
    protected ImageView mImageViewThumbnail;
    @BindView(R.id.tv_release_date)
    protected TextView mTextViewReleaseDate;
    @BindView(R.id.tv_runtime)
    protected TextView mTextViewRuntime;
    @BindView(R.id.tv_overview)
    protected TextView mTextViewOverview;
    @BindView(R.id.tv_mpaa_rating)
    protected TextView mTextViewMpaaRating;
    @BindView(R.id.rating_bar_vote_average)
    protected RatingBar mRatingVoteAverage;
    @BindView(R.id.tv_reviews)
    protected TextView mTextViewReviews;
    @BindView(R.id.tv_trailers_title)
    protected TextView mTextViewTrailersTitle;
    @BindView(R.id.tv_vote_count)
    protected TextView mTextViewVoteCount;
    @BindView(R.id.tv_genres)
    protected TextView mTextViewGenres;
    @Nullable
    @BindView(R.id.tv_movie_title)
    protected TextView mTextViewTitle;
    @BindString(R.string.msg_no_reviews)
    String msgNoReviews;
    @BindString(R.string.msg_no_trailers)
    String msgNoTrailers;
    @BindString(R.string.msg_trailers_not_available)
    String msgTrailersNotAvailable;
    @BindString(R.string.msg_reviews_not_available)
    String msgReviewsNotAvailable;
    @BindString(R.string.error_implement_method)
    String errorMissingMethod;
    @BindView(R.id.btn_mark_fav)
    Button btnMarkFavorite;

    protected RecyclerView mRecyclerView;
    protected TrailerAdapter mTrailerAdapter;
    protected int mMovieId;
    protected Movie mMovie;

    // Tag for saving movie so it doesn't have to be re-downloaded on config change
    public String BUNDLE_MOVIE = "SaveMovie";
    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * The layout has to be selected according to the calling Activity.
     * MainActivity and DetailActivity each has its own layout, NOT
     * necessarily dependent on device size or orientation.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

    /*    View rootView = inflater.inflate(R.layout.layout_fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView(rootView);
        return rootView; */
        return null;
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

    private void setupFavoritesButton() {
        if (DatabaseUtils.isFavoriteMovie(getActivity(), mMovie.getId()))
            btnMarkFavorite.setText("- Favorites");
        else
            btnMarkFavorite.setText("+ Favorites");
    }

    /**
     * Toggle movie in/out of favorites
     *
     * @param favButton
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.btn_mark_fav)
    public void favoritesButtonClick(Button favButton) {
        if (DatabaseUtils.isFavoriteMovie(getActivity(), mMovie.getId())) {
            //Log.i(TAG, "Removing movie from favorites " + mMovie.getOriginalTitle());
            DatabaseUtils.deleteMovie(getActivity(), mMovie.getId());
            favButton.setText("+ Favorites");
        } else {
            //Log.i(TAG, "Adding movie to favorites " + mMovie.getOriginalTitle());
            DatabaseUtils.insertMovie(getActivity(), mMovie);
            favButton.setText("- Favorites");
        }
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

    protected void setupRecyclerView(View rootView) {
        mTrailerAdapter = new TrailerAdapter(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_trailers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mTrailerAdapter);
    }

    //Download the extra details associated with a Movie (trailers, reviews)
    //Called by MainActivity if Fragment already exists (two-pane mode),
    //or when the Fragment is created by its own separate activity
    //(DetailActivity), in single-pane mode.
    public void downloadMovie(int movieId) {
        Tmdb tmdbManager = getTmdbApp();
        final MovieService movieService = tmdbManager.getMovieService();
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
                        //Log.i(TAG, "Success!! Movie title = " + movie.getOriginalTitle());
                        //Log.i(TAG, "There are "
                        //        + movie.getTrailerCount() + " trailers and "
                        //        + movie.getReviewCount() + " reviews");
                        downloadReleases(movieService);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // Handle errors here.
                        Utils.showToast(getActivity(), "Failed to download movie " + mMovieId);
                    }
                });
    }

    /**
     * MAKES A GREAT CASE FOR RxJava/Android, NEXT ON MY LEARNING CURVE LIST
     * <p/>
     * Movie releases require a separate API call.
     * This call is nested in the downloadMovie call.
     * It is called after the main movie details completes.
     */
    private void downloadReleases(MovieService movieService) {
        movieService.releases(mMovieId,
                new Callback<Releases>() {
                    @Override
                    public void success(Releases releases, Response response) {
                        //Log.i(TAG, "There are " + releases.getCount() + " releases");
                        mMovie.setReleases(releases);
                        //Log.i(TAG, "US rating is " + mMovie.getUSRating());
                        displayMovieDetails(mMovie);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // Handle errors here.
                        Utils.showToast(getActivity(), "Failed to download releases " + mMovieId);
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
        //Log.d(TAG, "   Genres " + movie.getGenres());
        //Larger screens won't have title on the detail Layout
        /*if (mTextViewTitle != null) {
            mTextViewTitle.setText(movie.getOriginalTitle());
            Log.d(TAG,"NO title in layout, show it in backdrop");
        }
        else
            Log.d(TAG, "Title in layout, don't show it in backdrop");*/

        mTextViewMpaaRating.setText(movie.getUSRating());
        mTextViewGenres.setText(movie.getGenres());
        displayTrailers(movie.getTrailers());
        displayReviews(movie.getReviews());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (movie.getReleaseDate() != null)
            mTextViewReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        CharSequence runtime = Phrase.from(getActivity(), R.string.text_runtime)
                .put("runtime", movie.getRuntime().toString())
                .format();
        mTextViewRuntime.setText(runtime);
        mRatingVoteAverage.setRating(movie.getVoteAverage().floatValue());
        mTextViewVoteCount.setText(" (" + movie.getVoteCount() + ")");
        mTextViewOverview.setText(movie.getOverview());
        setupFavoritesButton();
        loadPosterImage(movie);
    }

    private void displayTrailers(Trailers trailers) {
        int trailerCount;
        String emptyMsg = msgNoTrailers;

        if (!getTmdbApp().isNetworkAvailable())
            emptyMsg = msgTrailersNotAvailable;

        mTrailerAdapter.clear(true);
        if (trailers == null) {
            mTextViewTrailersTitle.setText(emptyMsg);
            mRecyclerView.setVisibility(View.GONE);
            mTextViewTrailersTitle.setVisibility(View.VISIBLE);
            return;
        }
        List<Trailers.Trailer> trailerList = trailers.getYoutube();
        trailerCount = trailerList.size();
        if (trailerCount == 0) {
            mTextViewTrailersTitle.setText(emptyMsg);
            mRecyclerView.setVisibility(View.GONE);
            mTextViewTrailersTitle.setVisibility(View.VISIBLE);
            return;
        }
        mTrailerAdapter.addAll(trailerList);
        mRecyclerView.setVisibility(View.VISIBLE);
        mTextViewTrailersTitle.setVisibility(View.GONE);
    }

    //There's room for 3 reviews, then show MORE button.
    //MAYBE TRY fixed-height horizontal scrolling grid HERE??
    //See http://android--examples.blogspot.com/2015/01/textview-new-line-multiline-in-android.html
    //private void displayReviews(List<Review> reviews) {
    private void displayReviews(Reviews reviews) {
        int reviewCount;
        String emptyMsg = msgNoReviews;

        if (!getTmdbApp().isNetworkAvailable())
            emptyMsg = msgReviewsNotAvailable;

        if (reviews == null) {
            mTextViewReviews.setText(emptyMsg);
            return;
        }
        List<Review> reviewList = reviews.getResults();
        reviewCount = reviewList.size();
        if (reviewCount == 0) {
            mTextViewReviews.setText(emptyMsg);
            return;
        }
        displayReview(reviewList.get(0));

        // This is a BIG FAKE because reviews comprises a "list" of Review items,
        // but you can't have a vertical list inside of a ScrollView.,
        // so just make it one big multi-line string.
        for (int i = 1; i < reviewList.size(); i++) {
            //define new line by append android system line separator
            mTextViewReviews.append(System.getProperty("line.separator"));
            mTextViewReviews.append(System.getProperty("line.separator"));
            displayReview(reviewList.get(i));
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
        Picasso.get()
                .load(movieImageUrl)
                .into(mImageViewThumbnail);

    }
}
