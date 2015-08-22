package com.nano.movies.data.trailer;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nano.movies.data.base.AbstractCursor;
import com.nano.movies.data.movie.*;

/**
 * Cursor wrapper for the {@code trailer} table.
 */
public class TrailerCursor extends AbstractCursor implements TrailerModel {
    public TrailerCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(TrailerColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Id of movie.
     */
    public long getMovieId() {
        Long res = getLongOrNull(TrailerColumns.MOVIE_ID);
        if (res == null)
            throw new NullPointerException("The value of 'movie_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Movie's unique tmdb.org id, used to identify the movie in api calls.
     */
    public long getMovieTmdbId() {
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
     * Trailer title.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getName() {
        String res = getStringOrNull(TrailerColumns.NAME);
        if (res == null)
            throw new NullPointerException("The value of 'name' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code size} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getSize() {
        String res = getStringOrNull(TrailerColumns.SIZE);
        return res;
    }

    /**
     * https://www.themoviedb.org/movie/150689-cinderella#play=<source>
     * Cannot be {@code null}.
     */
    @NonNull
    public String getSource() {
        String res = getStringOrNull(TrailerColumns.SOURCE);
        if (res == null)
            throw new NullPointerException("The value of 'source' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code type} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getType() {
        String res = getStringOrNull(TrailerColumns.TYPE);
        return res;
    }

    /**
     * Youtube or Quicktime
     * Cannot be {@code null}.
     */
    @NonNull
    public String getOrigin() {
        String res = getStringOrNull(TrailerColumns.ORIGIN);
        if (res == null)
            throw new NullPointerException("The value of 'origin' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
