package com.nano.movies.data.review;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nano.movies.data.base.AbstractCursor;
import com.nano.movies.data.movie.*;

/**
 * Cursor wrapper for the {@code review} table.
 */
public class ReviewCursor extends AbstractCursor implements ReviewModel {
    public ReviewCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(ReviewColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Id of movie in movies table.
     */
    public long getMovieId() {
        Long res = getLongOrNull(ReviewColumns.MOVIE_ID);
        if (res == null)
            throw new NullPointerException("The value of 'movie_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Movie's unique tmdb.org id, used to identify the movie in api calls.
     */
    public int getMovieTmdbId() {
        Integer res = getIntegerOrNull(MovieColumns.TMDB_ID);
        if (res == null)
            throw new NullPointerException("The value of 'tmdb_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Movie's homepage url
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieHomepage() {
        String res = getStringOrNull(MovieColumns.HOMEPAGE);
        return res;
    }

    /**
     * Movie's original title.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieOriginalTitle() {
        String res = getStringOrNull(MovieColumns.ORIGINAL_TITLE);
        return res;
    }

    /**
     * Movie synopsis.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieOverview() {
        String res = getStringOrNull(MovieColumns.OVERVIEW);
        return res;
    }

    /**
     * Movie's popularity value.
     * Can be {@code null}.
     */
    @Nullable
    public Double getMoviePopularity() {
        Double res = getDoubleOrNull(MovieColumns.POPULARITY);
        return res;
    }

    /**
     * Path to movie's image file, relative to http://image.tmdb.org/t/p/<imageSize>.
     * Can be {@code null}.
     */
    @Nullable
    public String getMoviePosterPath() {
        String res = getStringOrNull(MovieColumns.POSTER_PATH);
        return res;
    }

    /**
     * Movie's release date, eg., 2015-03-13.
     * Can be {@code null}.
     */
    @Nullable
    public Date getMovieReleaseDate() {
        Date res = getDateOrNull(MovieColumns.RELEASE_DATE);
        return res;
    }

    /**
     * Running time in minutes.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getMovieRuntime() {
        Integer res = getIntegerOrNull(MovieColumns.RUNTIME);
        return res;
    }

    /**
     * Tagline.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieTagline() {
        String res = getStringOrNull(MovieColumns.TAGLINE);
        return res;
    }

    /**
     * Movie's title.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieTitle() {
        String res = getStringOrNull(MovieColumns.TITLE);
        return res;
    }

    /**
     * Average voter rating, from 1 to 10.
     */
    public double getMovieVoteAverage() {
        Double res = getDoubleOrNull(MovieColumns.VOTE_AVERAGE);
        if (res == null)
            throw new NullPointerException("The value of 'vote_average' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Total votes cast for the movie.
     */
    public int getMovieVoteCount() {
        Integer res = getIntegerOrNull(MovieColumns.VOTE_COUNT);
        if (res == null)
            throw new NullPointerException("The value of 'vote_count' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * https://www.themoviedb.org/review/<review_id>
     * Cannot be {@code null}.
     */
    @NonNull
    public String getReviewId() {
        String res = getStringOrNull(ReviewColumns.REVIEW_ID);
        if (res == null)
            throw new NullPointerException("The value of 'review_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code author} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getAuthor() {
        String res = getStringOrNull(ReviewColumns.AUTHOR);
        if (res == null)
            throw new NullPointerException("The value of 'author' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code content} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getContent() {
        String res = getStringOrNull(ReviewColumns.CONTENT);
        if (res == null)
            throw new NullPointerException("The value of 'content' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Links to https://www.themoviedb.org/review/<review_id>
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUrl() {
        String res = getStringOrNull(ReviewColumns.URL);
        if (res == null)
            throw new NullPointerException("The value of 'url' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
