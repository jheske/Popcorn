package com.nano.movies.data.movie;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nano.movies.data.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code movie} table.
 */
public class MovieCursor extends AbstractCursor implements MovieModel {
    public MovieCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(MovieColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Movie's unique tmdb.org id, used to identify the movie in api calls.
     */
    public long getTmdbId() {
        Long res = getLongOrNull(MovieColumns.TMDB_ID);
        if (res == null)
            throw new NullPointerException("The value of 'tmdb_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Movie's homepage url
     * Can be {@code null}.
     */
    @Nullable
    public String getHomepage() {
        String res = getStringOrNull(MovieColumns.HOMEPAGE);
        return res;
    }

    /**
     * Movie's original title.
     * Can be {@code null}.
     */
    @Nullable
    public String getOriginalTitle() {
        String res = getStringOrNull(MovieColumns.ORIGINAL_TITLE);
        return res;
    }

    /**
     * Movie synopsis.
     * Can be {@code null}.
     */
    @Nullable
    public String getOverview() {
        String res = getStringOrNull(MovieColumns.OVERVIEW);
        return res;
    }

    /**
     * Movie's popularity value.
     * Can be {@code null}.
     */
    @Nullable
    public Double getPopularity() {
        Double res = getDoubleOrNull(MovieColumns.POPULARITY);
        return res;
    }

    /**
     * Path to movie's image file, relative to http://image.tmdb.org/t/p/<imageSize>.
     * Can be {@code null}.
     */
    @Nullable
    public String getPosterPath() {
        String res = getStringOrNull(MovieColumns.POSTER_PATH);
        return res;
    }

    /**
     * Movie's release date, eg., 2015-03-13.
     * Can be {@code null}.
     */
    @Nullable
    public Date getReleaseDate() {
        Date res = getDateOrNull(MovieColumns.RELEASE_DATE);
        return res;
    }

    /**
     * Running time in minutes.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getRuntime() {
        Integer res = getIntegerOrNull(MovieColumns.RUNTIME);
        return res;
    }

    /**
     * Tagline.
     * Can be {@code null}.
     */
    @Nullable
    public String getTagline() {
        String res = getStringOrNull(MovieColumns.TAGLINE);
        return res;
    }

    /**
     * Movie's title.
     * Can be {@code null}.
     */
    @Nullable
    public String getTitle() {
        String res = getStringOrNull(MovieColumns.TITLE);
        return res;
    }

    /**
     * Average voter rating, from 1 to 10.
     */
    public double getVoteAverage() {
        Double res = getDoubleOrNull(MovieColumns.VOTE_AVERAGE);
        if (res == null)
            throw new NullPointerException("The value of 'vote_average' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Total votes cast for the movie.
     */
    public int getVoteCount() {
        Integer res = getIntegerOrNull(MovieColumns.VOTE_COUNT);
        if (res == null)
            throw new NullPointerException("The value of 'vote_count' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
