package com.nano.movies.data.trailer;

import com.nano.movies.data.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Movie trailers table.
 */
public interface TrailerModel extends BaseModel {

    /**
     * Id of movie.
     */
    long getMovieId();

    /**
     * Trailer title.
     * Cannot be {@code null}.
     */
    @NonNull
    String getName();

    /**
     * Get the {@code size} value.
     * Can be {@code null}.
     */
    @Nullable
    String getSize();

    /**
     * https://www.themoviedb.org/movie/150689-cinderella#play=<source>
     * Cannot be {@code null}.
     */
    @NonNull
    String getSource();

    /**
     * Get the {@code type} value.
     * Can be {@code null}.
     */
    @Nullable
    String getType();

    /**
     * Youtube or Quicktime (Quicktime REMOVED)
     * Cannot be {@code null}.
     */
    @NonNull
    String getOrigin();
}
