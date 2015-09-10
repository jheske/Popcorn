package com.nano.movies.utils;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.nano.movies.data.movie.MovieColumns;
import com.nano.movies.data.movie.MovieContentValues;
import com.nano.movies.data.movie.MovieCursor;
import com.nano.movies.data.movie.MovieSelection;
import com.nano.movies.data.review.ReviewContentValues;
import com.nano.movies.data.trailer.TrailerContentValues;
import com.nano.movies.web.Movie;
import com.nano.movies.web.Reviews;
import com.nano.movies.web.Trailers;

import java.util.List;

public class DatabaseUtils {

    private final static String TRAILER_ORIGIN_YOUTUBE = "youtube";

    public static void clearDatabase(Context context) {
        // Delete movies ((which should also delete Reviews and Trailers because
        // of the "on delete cascade" constraint)
        MovieSelection movieSelection = new MovieSelection();
        movieSelection.delete(context.getContentResolver());
    }

    public static void insertMovies(Context context, List<Movie> movies) {
        for (int position = 0; position < movies.size(); position++) {
            insertMovie(context, movies.get(position));
        }
    }

    /**
     * Insert movie first, which returns its primary key
     * to use as the movie_id foreign key when inserting trailers and reviews.
     * <p/>
     * Don't try to insert Reviews and Trailers. They
     * Then insert Reviews and Trailers.  A movie has one list of reviews and
     * two lists of trailers (Youtube and Quicktime (Quicktime REMOVED)).
     * Reviews and/or trailers may be empty.
     */
    public static void insertMovie(Context context, Movie movie) {
        long movieId = dbInsertMovie(context, movie);
        if (movie.getTrailers() != null) {
            insertTrailers(context, movie.getTrailers().getYoutube(), TRAILER_ORIGIN_YOUTUBE, movieId);
        }
        if (movie.getReviews() != null)
            insertReviews(context, movie.getReviews().getResults(), movieId);
    }

    /**
     * Insert a movie.
     *
     * @return the id of the created movie. It will be used as
     * foreign key when inserting reviews and trailers
     */
    private static long dbInsertMovie(Context context, Movie movie) {
        MovieContentValues values = new MovieContentValues();
        values.putTmdbId(movie.getId());
        values.putHomepage(movie.getHomePage());
        values.putOriginalTitle(movie.getOriginalTitle());
        values.putOverview(movie.getOverview());
        values.putPopularity(movie.getPopularity());
        values.putPosterPath(movie.getPosterPath());
        values.putReleaseDate(movie.getReleaseDate());
        values.putRuntime(movie.getRuntime());
        values.putTagline(movie.getTagline());
        values.putTitle(movie.getTitle());
        values.putVoteAverage(movie.getVoteAverage());
        values.putVoteCount(movie.getVoteCount());

        Uri uri = values.insert(context.getContentResolver());
        return ContentUris.parseId(uri);
    }

    /**
     * Insert reviews into Trailers table. This table has
     * foreign key movieId, (table column = tmdb_id) which references
     * the Movie table's _id (the auto-generated primary key)
     *
     * @param trailers Will be empty if movie has no trailers.
     * @param movieId  The foreign key linking this trailer to its associated
     *                 associated movie (Movie._id).
     * @param origin   Trailer plays on either Youtube, or Quicktime (REMOVED)
     */
    private static void insertTrailers(Context context,
                                       List<Trailers.Trailer> trailers,
                                       String origin, Long movieId) {
        Trailers.Trailer trailer;

        if (trailers == null)
            return;
        if (trailers.size() == 0)
            return;
        for (int i = 0; i < trailers.size(); i++) {
            trailer = trailers.get(i);
            dbInsertTrailer(context, trailer, origin, movieId);
        }
    }

    /**
     * Set up a ContentValues object with all the insert fields.
     * Pass the ContentResolver ContentValues.insert().  This
     * sends the data to the ContentProvider to be inserted into the db.
     *
     * @param trailer
     * @param origin  Youtube or Quicktime (I have removed Quicktime from JSon response)
     * @param movieId The _id of the movie associated with this trailer
     * @return The _id of the inserted record
     * <p/>
     * TrailerContentValues is a Trailer-specific wrapper containing a ContentValues object,
     * the Uri's to Trailer table operations, and CRUD wrapper methods.
     */
    private static long dbInsertTrailer(Context context, Trailers.Trailer trailer, String origin, Long movieId) {
        TrailerContentValues values = new TrailerContentValues();
        values.putMovieId(movieId);
        values.putName(trailer.getName());
        values.putSize(trailer.getSize());
        values.putSource(trailer.getSource());
        values.putType(trailer.getType());
        values.putOrigin(origin);

        Uri uri = values.insert(context.getContentResolver());
        return ContentUris.parseId(uri);
    }

    /**
     * Insert reviews into Review table. This table has
     * foreign key movieId, (table column = tmdb_id) into the Movie table.
     *
     * @param reviews Will be empty if movie has no reviews.
     * @param movieId
     */
    private static void insertReviews(Context context, List<Reviews.Review> reviews, Long movieId) {
        Reviews.Review review;

        if (reviews == null)
            return;
        if (reviews.size() == 0)
            return;
        for (int i = 0; i < reviews.size(); i++) {
            review = reviews.get(i);
            dbInsertReview(context, review, movieId);
        }
    }

    /**
     * Insert reviews into Review table. This table has
     * foreign key movieId, (table column = tmdb_id) which references
     * the movies table (column=_id)
     *
     * @param {review}
     * @param movieId
     * @return ReviewContentValues is a Review-specific wrapper class containing a ContentValues object,
     * the Uri's to Review table operations, and CRUD wrapper methods.
     */
    private static long dbInsertReview(Context context, Reviews.Review trailer, Long movieId) {
        ReviewContentValues values = new ReviewContentValues();

        values.putMovieId(movieId);
        values.putReviewId(trailer.getId());
        values.putAuthor(trailer.getAuthor());
        values.putContent(trailer.getContent());
        values.putUrl(trailer.getUrl());
        values.putMovieId(movieId);

        Uri uri = values.insert(context);
        return ContentUris.parseId(uri);
    }

    // Query one person
    public static boolean isFavoriteMovie(Context context, int movieId) {
        MovieSelection movieSelection = new MovieSelection();
        movieSelection.tmdbId(movieId);
        String[] projection = {MovieColumns._ID};
        MovieCursor cursor = movieSelection.query(context.getContentResolver(), projection);
        boolean isFavorite = cursor.getCount() != 0;
        cursor.close();
        return isFavorite;
    }

    public static void deleteMovie(Context context,int movieId) {
        MovieSelection movieSelection = new MovieSelection();
        movieSelection.tmdbId(movieId);
        movieSelection.delete(context.getContentResolver());
    }

    /**
     * Make Utils a utility class by preventing instantiation.
     */
    private DatabaseUtils() {
        throw new AssertionError();
    }
}
