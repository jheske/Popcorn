package com.nano.movies.data.trailer;

import android.net.Uri;
import android.provider.BaseColumns;

import com.nano.movies.data.MovieProvider;
import com.nano.movies.data.movie.MovieColumns;
import com.nano.movies.data.review.ReviewColumns;
import com.nano.movies.data.trailer.TrailerColumns;

/**
 * Movie trailers table.
 */
public class TrailerColumns implements BaseColumns {
    public static final String TABLE_NAME = "trailer";
    public static final Uri CONTENT_URI = Uri.parse(MovieProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * Id of movie.
     */
    public static final String MOVIE_ID = "movie_id";

    /**
     * Trailer title.
     */
    public static final String NAME = "name";

    public static final String SIZE = "size";

    /**
     * https://www.themoviedb.org/movie/150689-cinderella#play=<source>
     */
    public static final String SOURCE = "source";

    public static final String TYPE = "type";

    /**
     * Youtube or Quicktime
     */
    public static final String ORIGIN = "origin";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MOVIE_ID,
            NAME,
            SIZE,
            SOURCE,
            TYPE,
            ORIGIN
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
            if (c.equals(NAME) || c.contains("." + NAME)) return true;
            if (c.equals(SIZE) || c.contains("." + SIZE)) return true;
            if (c.equals(SOURCE) || c.contains("." + SOURCE)) return true;
            if (c.equals(TYPE) || c.contains("." + TYPE)) return true;
            if (c.equals(ORIGIN) || c.contains("." + ORIGIN)) return true;
        }
        return false;
    }

    public static final String PREFIX_MOVIE = TABLE_NAME + "__" + MovieColumns.TABLE_NAME;
}
