package com.nano.movies.data.review;

import com.nano.movies.data.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Movie reviews table.
 */
public interface ReviewModel extends BaseModel {

    /**
     * Id of movie in movies table.
     */
    long getMovieId();

    /**
     * https://www.themoviedb.org/review/<review_id>
     * Cannot be {@code null}.
     */
    @NonNull
    String getReviewId();

    /**
     * Get the {@code author} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getAuthor();

    /**
     * Get the {@code content} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getContent();

    /**
     * Links to https://www.themoviedb.org/review/<review_id>
     * Cannot be {@code null}.
     */
    @NonNull
    String getUrl();
}
