package com.nano.movies.data.review;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nano.movies.data.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code review} table.
 */
public class ReviewContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ReviewColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable ReviewSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable ReviewSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Id of movie in movies table.
     */
    public ReviewContentValues putMovieId(long value) {
        mContentValues.put(ReviewColumns.MOVIE_ID, value);
        return this;
    }


    /**
     * https://www.themoviedb.org/review/<review_id>
     */
    public ReviewContentValues putReviewId(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("reviewId must not be null");
        mContentValues.put(ReviewColumns.REVIEW_ID, value);
        return this;
    }


    public ReviewContentValues putAuthor(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("author must not be null");
        mContentValues.put(ReviewColumns.AUTHOR, value);
        return this;
    }


    public ReviewContentValues putContent(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("content must not be null");
        mContentValues.put(ReviewColumns.CONTENT, value);
        return this;
    }


    /**
     * Links to https://www.themoviedb.org/review/<review_id>
     */
    public ReviewContentValues putUrl(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("url must not be null");
        mContentValues.put(ReviewColumns.URL, value);
        return this;
    }

}
