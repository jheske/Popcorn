package com.nano.movies.data.movie;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.nano.movies.data.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code movie} table.
 */
public class MovieContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MovieColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MovieSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param {contentResolver} The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MovieSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Movie's unique tmdb.org id, used to identify the movie in api calls.
     */
    public MovieContentValues putTmdbId(int value) {
        mContentValues.put(MovieColumns.TMDB_ID, value);
        return this;
    }


    /**
     * Movie's homepage url
     */
    public MovieContentValues putHomepage(@Nullable String value) {
        mContentValues.put(MovieColumns.HOMEPAGE, value);
        return this;
    }

    public MovieContentValues putHomepageNull() {
        mContentValues.putNull(MovieColumns.HOMEPAGE);
        return this;
    }

    /**
     * Movie's original title.
     */
    public MovieContentValues putOriginalTitle(@Nullable String value) {
        mContentValues.put(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public MovieContentValues putOriginalTitleNull() {
        mContentValues.putNull(MovieColumns.ORIGINAL_TITLE);
        return this;
    }

    /**
     * Movie synopsis.
     */
    public MovieContentValues putOverview(@Nullable String value) {
        mContentValues.put(MovieColumns.OVERVIEW, value);
        return this;
    }

    public MovieContentValues putOverviewNull() {
        mContentValues.putNull(MovieColumns.OVERVIEW);
        return this;
    }

    /**
     * Movie's popularity value.
     */
    public MovieContentValues putPopularity(@Nullable Double value) {
        mContentValues.put(MovieColumns.POPULARITY, value);
        return this;
    }

    public MovieContentValues putPopularityNull() {
        mContentValues.putNull(MovieColumns.POPULARITY);
        return this;
    }

    /**
     * Path to movie's image file, relative to http://image.tmdb.org/t/p/<imageSize>.
     */
    public MovieContentValues putPosterPath(@Nullable String value) {
        mContentValues.put(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public MovieContentValues putPosterPathNull() {
        mContentValues.putNull(MovieColumns.POSTER_PATH);
        return this;
    }

    /**
     * Movie's release date, eg., 2015-03-13.
     */
    public MovieContentValues putReleaseDate(@Nullable Date value) {
        mContentValues.put(MovieColumns.RELEASE_DATE, value == null ? null : value.getTime());
        return this;
    }

    public MovieContentValues putReleaseDateNull() {
        mContentValues.putNull(MovieColumns.RELEASE_DATE);
        return this;
    }

    public MovieContentValues putReleaseDate(@Nullable Long value) {
        mContentValues.put(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    /**
     * Running time in minutes.
     */
    public MovieContentValues putRuntime(@Nullable Integer value) {
        mContentValues.put(MovieColumns.RUNTIME, value);
        return this;
    }

    public MovieContentValues putRuntimeNull() {
        mContentValues.putNull(MovieColumns.RUNTIME);
        return this;
    }

    /**
     * Tagline.
     */
    public MovieContentValues putTagline(@Nullable String value) {
        mContentValues.put(MovieColumns.TAGLINE, value);
        return this;
    }

    public MovieContentValues putTaglineNull() {
        mContentValues.putNull(MovieColumns.TAGLINE);
        return this;
    }

    /**
     * Movie's title.
     */
    public MovieContentValues putTitle(@Nullable String value) {
        mContentValues.put(MovieColumns.TITLE, value);
        return this;
    }

    public MovieContentValues putTitleNull() {
        mContentValues.putNull(MovieColumns.TITLE);
        return this;
    }

    /**
     * Average voter rating, from 1 to 10.
     */
    public MovieContentValues putVoteAverage(double value) {
        mContentValues.put(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }


    /**
     * Total votes cast for the movie.
     */
    public MovieContentValues putVoteCount(int value) {
        mContentValues.put(MovieColumns.VOTE_COUNT, value);
        return this;
    }

}
