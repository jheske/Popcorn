package com.nano.movies.data.movie;

import com.nano.movies.data.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Favorite movies database.
 */
public interface MovieModel extends BaseModel {

    /**
     * Movie's unique tmdb.org id, used to identify the movie in api calls.
     */
    long getTmdbId();

    /**
     * Movie's homepage url
     * Can be {@code null}.
     */
    @Nullable
    String getHomepage();

    /**
     * Movie's original title.
     * Can be {@code null}.
     */
    @Nullable
    String getOriginalTitle();

    /**
     * Movie synopsis.
     * Can be {@code null}.
     */
    @Nullable
    String getOverview();

    /**
     * Movie's popularity value.
     * Can be {@code null}.
     */
    @Nullable
    Double getPopularity();

    /**
     * Path to movie's image file, relative to http://image.tmdb.org/t/p/<imageSize>.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterPath();

    /**
     * Movie's release date, eg., 2015-03-13.
     * Can be {@code null}.
     */
    @Nullable
    Date getReleaseDate();

    /**
     * Running time in minutes.
     * Can be {@code null}.
     */
    @Nullable
    Integer getRuntime();

    /**
     * Tagline.
     * Can be {@code null}.
     */
    @Nullable
    String getTagline();

    /**
     * Movie's title.
     * Can be {@code null}.
     */
    @Nullable
    String getTitle();

    /**
     * Average voter rating, from 1 to 10.
     */
    double getVoteAverage();

    /**
     * Total votes cast for the movie.
     */
    int getVoteCount();
}
