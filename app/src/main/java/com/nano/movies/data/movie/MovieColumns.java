package com.nano.movies.data.movie;

import android.net.Uri;
import android.provider.BaseColumns;

import com.nano.movies.data.MovieProvider;

/**
 * Favorite movies database.
 */
public class MovieColumns implements BaseColumns {
    public static final String TABLE_NAME = "movie";
    public static final Uri CONTENT_URI = Uri.parse(MovieProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * Movie's unique tmdb.org id, used to identify the movie in api calls.
     */
    public static final String TMDB_ID = "tmdb_id";

    /**
     * Movie's homepage url
     */
    public static final String HOMEPAGE = "homepage";

    /**
     * Movie's original title.
     */
    public static final String ORIGINAL_TITLE = "original_title";

    /**
     * Movie synopsis.
     */
    public static final String OVERVIEW = "overview";

    /**
     * Movie's popularity value.
     */
    public static final String POPULARITY = "popularity";

    /**
     * Path to movie's image file, relative to http://image.tmdb.org/t/p/<imageSize>.
     */
    public static final String POSTER_PATH = "poster_path";

    /**
     * Movie's release date, eg., 2015-03-13.
     */
    public static final String RELEASE_DATE = "release_date";

    /**
     * Running time in minutes.
     */
    public static final String RUNTIME = "runtime";

    /**
     * Tagline.
     */
    public static final String TAGLINE = "tagline";

    /**
     * Movie's title.
     */
    public static final String TITLE = "title";

    /**
     * Average voter rating, from 1 to 10.
     */
    public static final String VOTE_AVERAGE = "vote_average";

    /**
     * Total votes cast for the movie.
     */
    public static final String VOTE_COUNT = "vote_count";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            TMDB_ID,
            HOMEPAGE,
            ORIGINAL_TITLE,
            OVERVIEW,
            POPULARITY,
            POSTER_PATH,
            RELEASE_DATE,
            RUNTIME,
            TAGLINE,
            TITLE,
            VOTE_AVERAGE,
            VOTE_COUNT
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(TMDB_ID) || c.contains("." + TMDB_ID)) return true;
            if (c.equals(HOMEPAGE) || c.contains("." + HOMEPAGE)) return true;
            if (c.equals(ORIGINAL_TITLE) || c.contains("." + ORIGINAL_TITLE)) return true;
            if (c.equals(OVERVIEW) || c.contains("." + OVERVIEW)) return true;
            if (c.equals(POPULARITY) || c.contains("." + POPULARITY)) return true;
            if (c.equals(POSTER_PATH) || c.contains("." + POSTER_PATH)) return true;
            if (c.equals(RELEASE_DATE) || c.contains("." + RELEASE_DATE)) return true;
            if (c.equals(RUNTIME) || c.contains("." + RUNTIME)) return true;
            if (c.equals(TAGLINE) || c.contains("." + TAGLINE)) return true;
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(VOTE_AVERAGE) || c.contains("." + VOTE_AVERAGE)) return true;
            if (c.equals(VOTE_COUNT) || c.contains("." + VOTE_COUNT)) return true;
        }
        return false;
    }

}
