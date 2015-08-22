package com.nano.movies.data.trailer;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.nano.movies.data.base.AbstractSelection;
import com.nano.movies.data.movie.*;

/**
 * Selection for the {@code trailer} table.
 */
public class TrailerSelection extends AbstractSelection<TrailerSelection> {
    @Override
    protected Uri baseUri() {
        return TrailerColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TrailerCursor} object, which is positioned before the first entry, or null.
     */
    public TrailerCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public TrailerCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TrailerCursor} object, which is positioned before the first entry, or null.
     */
    public TrailerCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public TrailerCursor query(Context context) {
        return query(context, null);
    }


    public TrailerSelection id(long... value) {
        addEquals("trailer." + TrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection idNot(long... value) {
        addNotEquals("trailer." + TrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection orderById(boolean desc) {
        orderBy("trailer." + TrailerColumns._ID, desc);
        return this;
    }

    public TrailerSelection orderById() {
        return orderById(false);
    }

    public TrailerSelection movieId(long... value) {
        addEquals(TrailerColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieIdNot(long... value) {
        addNotEquals(TrailerColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieIdGt(long value) {
        addGreaterThan(TrailerColumns.MOVIE_ID, value);
        return this;
    }

    public TrailerSelection movieIdGtEq(long value) {
        addGreaterThanOrEquals(TrailerColumns.MOVIE_ID, value);
        return this;
    }

    public TrailerSelection movieIdLt(long value) {
        addLessThan(TrailerColumns.MOVIE_ID, value);
        return this;
    }

    public TrailerSelection movieIdLtEq(long value) {
        addLessThanOrEquals(TrailerColumns.MOVIE_ID, value);
        return this;
    }

    public TrailerSelection orderByMovieId(boolean desc) {
        orderBy(TrailerColumns.MOVIE_ID, desc);
        return this;
    }

    public TrailerSelection orderByMovieId() {
        orderBy(TrailerColumns.MOVIE_ID, false);
        return this;
    }

    public TrailerSelection movieTmdbId(long... value) {
        addEquals(MovieColumns.TMDB_ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieTmdbIdNot(long... value) {
        addNotEquals(MovieColumns.TMDB_ID, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieTmdbIdGt(long value) {
        addGreaterThan(MovieColumns.TMDB_ID, value);
        return this;
    }

    public TrailerSelection movieTmdbIdGtEq(long value) {
        addGreaterThanOrEquals(MovieColumns.TMDB_ID, value);
        return this;
    }

    public TrailerSelection movieTmdbIdLt(long value) {
        addLessThan(MovieColumns.TMDB_ID, value);
        return this;
    }

    public TrailerSelection movieTmdbIdLtEq(long value) {
        addLessThanOrEquals(MovieColumns.TMDB_ID, value);
        return this;
    }

    public TrailerSelection orderByMovieTmdbId(boolean desc) {
        orderBy(MovieColumns.TMDB_ID, desc);
        return this;
    }

    public TrailerSelection orderByMovieTmdbId() {
        orderBy(MovieColumns.TMDB_ID, false);
        return this;
    }

    public TrailerSelection movieHomepage(String... value) {
        addEquals(MovieColumns.HOMEPAGE, value);
        return this;
    }

    public TrailerSelection movieHomepageNot(String... value) {
        addNotEquals(MovieColumns.HOMEPAGE, value);
        return this;
    }

    public TrailerSelection movieHomepageLike(String... value) {
        addLike(MovieColumns.HOMEPAGE, value);
        return this;
    }

    public TrailerSelection movieHomepageContains(String... value) {
        addContains(MovieColumns.HOMEPAGE, value);
        return this;
    }

    public TrailerSelection movieHomepageStartsWith(String... value) {
        addStartsWith(MovieColumns.HOMEPAGE, value);
        return this;
    }

    public TrailerSelection movieHomepageEndsWith(String... value) {
        addEndsWith(MovieColumns.HOMEPAGE, value);
        return this;
    }

    public TrailerSelection orderByMovieHomepage(boolean desc) {
        orderBy(MovieColumns.HOMEPAGE, desc);
        return this;
    }

    public TrailerSelection orderByMovieHomepage() {
        orderBy(MovieColumns.HOMEPAGE, false);
        return this;
    }

    public TrailerSelection movieOriginalTitle(String... value) {
        addEquals(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public TrailerSelection movieOriginalTitleNot(String... value) {
        addNotEquals(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public TrailerSelection movieOriginalTitleLike(String... value) {
        addLike(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public TrailerSelection movieOriginalTitleContains(String... value) {
        addContains(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public TrailerSelection movieOriginalTitleStartsWith(String... value) {
        addStartsWith(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public TrailerSelection movieOriginalTitleEndsWith(String... value) {
        addEndsWith(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public TrailerSelection orderByMovieOriginalTitle(boolean desc) {
        orderBy(MovieColumns.ORIGINAL_TITLE, desc);
        return this;
    }

    public TrailerSelection orderByMovieOriginalTitle() {
        orderBy(MovieColumns.ORIGINAL_TITLE, false);
        return this;
    }

    public TrailerSelection movieOverview(String... value) {
        addEquals(MovieColumns.OVERVIEW, value);
        return this;
    }

    public TrailerSelection movieOverviewNot(String... value) {
        addNotEquals(MovieColumns.OVERVIEW, value);
        return this;
    }

    public TrailerSelection movieOverviewLike(String... value) {
        addLike(MovieColumns.OVERVIEW, value);
        return this;
    }

    public TrailerSelection movieOverviewContains(String... value) {
        addContains(MovieColumns.OVERVIEW, value);
        return this;
    }

    public TrailerSelection movieOverviewStartsWith(String... value) {
        addStartsWith(MovieColumns.OVERVIEW, value);
        return this;
    }

    public TrailerSelection movieOverviewEndsWith(String... value) {
        addEndsWith(MovieColumns.OVERVIEW, value);
        return this;
    }

    public TrailerSelection orderByMovieOverview(boolean desc) {
        orderBy(MovieColumns.OVERVIEW, desc);
        return this;
    }

    public TrailerSelection orderByMovieOverview() {
        orderBy(MovieColumns.OVERVIEW, false);
        return this;
    }

    public TrailerSelection moviePopularity(Double... value) {
        addEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public TrailerSelection moviePopularityNot(Double... value) {
        addNotEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public TrailerSelection moviePopularityGt(double value) {
        addGreaterThan(MovieColumns.POPULARITY, value);
        return this;
    }

    public TrailerSelection moviePopularityGtEq(double value) {
        addGreaterThanOrEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public TrailerSelection moviePopularityLt(double value) {
        addLessThan(MovieColumns.POPULARITY, value);
        return this;
    }

    public TrailerSelection moviePopularityLtEq(double value) {
        addLessThanOrEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public TrailerSelection orderByMoviePopularity(boolean desc) {
        orderBy(MovieColumns.POPULARITY, desc);
        return this;
    }

    public TrailerSelection orderByMoviePopularity() {
        orderBy(MovieColumns.POPULARITY, false);
        return this;
    }

    public TrailerSelection moviePosterPath(String... value) {
        addEquals(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public TrailerSelection moviePosterPathNot(String... value) {
        addNotEquals(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public TrailerSelection moviePosterPathLike(String... value) {
        addLike(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public TrailerSelection moviePosterPathContains(String... value) {
        addContains(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public TrailerSelection moviePosterPathStartsWith(String... value) {
        addStartsWith(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public TrailerSelection moviePosterPathEndsWith(String... value) {
        addEndsWith(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public TrailerSelection orderByMoviePosterPath(boolean desc) {
        orderBy(MovieColumns.POSTER_PATH, desc);
        return this;
    }

    public TrailerSelection orderByMoviePosterPath() {
        orderBy(MovieColumns.POSTER_PATH, false);
        return this;
    }

    public TrailerSelection movieReleaseDate(Date... value) {
        addEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public TrailerSelection movieReleaseDateNot(Date... value) {
        addNotEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public TrailerSelection movieReleaseDate(Long... value) {
        addEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public TrailerSelection movieReleaseDateAfter(Date value) {
        addGreaterThan(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public TrailerSelection movieReleaseDateAfterEq(Date value) {
        addGreaterThanOrEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public TrailerSelection movieReleaseDateBefore(Date value) {
        addLessThan(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public TrailerSelection movieReleaseDateBeforeEq(Date value) {
        addLessThanOrEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public TrailerSelection orderByMovieReleaseDate(boolean desc) {
        orderBy(MovieColumns.RELEASE_DATE, desc);
        return this;
    }

    public TrailerSelection orderByMovieReleaseDate() {
        orderBy(MovieColumns.RELEASE_DATE, false);
        return this;
    }

    public TrailerSelection movieRuntime(Integer... value) {
        addEquals(MovieColumns.RUNTIME, value);
        return this;
    }

    public TrailerSelection movieRuntimeNot(Integer... value) {
        addNotEquals(MovieColumns.RUNTIME, value);
        return this;
    }

    public TrailerSelection movieRuntimeGt(int value) {
        addGreaterThan(MovieColumns.RUNTIME, value);
        return this;
    }

    public TrailerSelection movieRuntimeGtEq(int value) {
        addGreaterThanOrEquals(MovieColumns.RUNTIME, value);
        return this;
    }

    public TrailerSelection movieRuntimeLt(int value) {
        addLessThan(MovieColumns.RUNTIME, value);
        return this;
    }

    public TrailerSelection movieRuntimeLtEq(int value) {
        addLessThanOrEquals(MovieColumns.RUNTIME, value);
        return this;
    }

    public TrailerSelection orderByMovieRuntime(boolean desc) {
        orderBy(MovieColumns.RUNTIME, desc);
        return this;
    }

    public TrailerSelection orderByMovieRuntime() {
        orderBy(MovieColumns.RUNTIME, false);
        return this;
    }

    public TrailerSelection movieTagline(String... value) {
        addEquals(MovieColumns.TAGLINE, value);
        return this;
    }

    public TrailerSelection movieTaglineNot(String... value) {
        addNotEquals(MovieColumns.TAGLINE, value);
        return this;
    }

    public TrailerSelection movieTaglineLike(String... value) {
        addLike(MovieColumns.TAGLINE, value);
        return this;
    }

    public TrailerSelection movieTaglineContains(String... value) {
        addContains(MovieColumns.TAGLINE, value);
        return this;
    }

    public TrailerSelection movieTaglineStartsWith(String... value) {
        addStartsWith(MovieColumns.TAGLINE, value);
        return this;
    }

    public TrailerSelection movieTaglineEndsWith(String... value) {
        addEndsWith(MovieColumns.TAGLINE, value);
        return this;
    }

    public TrailerSelection orderByMovieTagline(boolean desc) {
        orderBy(MovieColumns.TAGLINE, desc);
        return this;
    }

    public TrailerSelection orderByMovieTagline() {
        orderBy(MovieColumns.TAGLINE, false);
        return this;
    }

    public TrailerSelection movieTitle(String... value) {
        addEquals(MovieColumns.TITLE, value);
        return this;
    }

    public TrailerSelection movieTitleNot(String... value) {
        addNotEquals(MovieColumns.TITLE, value);
        return this;
    }

    public TrailerSelection movieTitleLike(String... value) {
        addLike(MovieColumns.TITLE, value);
        return this;
    }

    public TrailerSelection movieTitleContains(String... value) {
        addContains(MovieColumns.TITLE, value);
        return this;
    }

    public TrailerSelection movieTitleStartsWith(String... value) {
        addStartsWith(MovieColumns.TITLE, value);
        return this;
    }

    public TrailerSelection movieTitleEndsWith(String... value) {
        addEndsWith(MovieColumns.TITLE, value);
        return this;
    }

    public TrailerSelection orderByMovieTitle(boolean desc) {
        orderBy(MovieColumns.TITLE, desc);
        return this;
    }

    public TrailerSelection orderByMovieTitle() {
        orderBy(MovieColumns.TITLE, false);
        return this;
    }

    public TrailerSelection movieVoteAverage(double... value) {
        addEquals(MovieColumns.VOTE_AVERAGE, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieVoteAverageNot(double... value) {
        addNotEquals(MovieColumns.VOTE_AVERAGE, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieVoteAverageGt(double value) {
        addGreaterThan(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public TrailerSelection movieVoteAverageGtEq(double value) {
        addGreaterThanOrEquals(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public TrailerSelection movieVoteAverageLt(double value) {
        addLessThan(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public TrailerSelection movieVoteAverageLtEq(double value) {
        addLessThanOrEquals(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public TrailerSelection orderByMovieVoteAverage(boolean desc) {
        orderBy(MovieColumns.VOTE_AVERAGE, desc);
        return this;
    }

    public TrailerSelection orderByMovieVoteAverage() {
        orderBy(MovieColumns.VOTE_AVERAGE, false);
        return this;
    }

    public TrailerSelection movieVoteCount(int... value) {
        addEquals(MovieColumns.VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieVoteCountNot(int... value) {
        addNotEquals(MovieColumns.VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public TrailerSelection movieVoteCountGt(int value) {
        addGreaterThan(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public TrailerSelection movieVoteCountGtEq(int value) {
        addGreaterThanOrEquals(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public TrailerSelection movieVoteCountLt(int value) {
        addLessThan(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public TrailerSelection movieVoteCountLtEq(int value) {
        addLessThanOrEquals(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public TrailerSelection orderByMovieVoteCount(boolean desc) {
        orderBy(MovieColumns.VOTE_COUNT, desc);
        return this;
    }

    public TrailerSelection orderByMovieVoteCount() {
        orderBy(MovieColumns.VOTE_COUNT, false);
        return this;
    }

    public TrailerSelection name(String... value) {
        addEquals(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameNot(String... value) {
        addNotEquals(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameLike(String... value) {
        addLike(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameContains(String... value) {
        addContains(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameStartsWith(String... value) {
        addStartsWith(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection nameEndsWith(String... value) {
        addEndsWith(TrailerColumns.NAME, value);
        return this;
    }

    public TrailerSelection orderByName(boolean desc) {
        orderBy(TrailerColumns.NAME, desc);
        return this;
    }

    public TrailerSelection orderByName() {
        orderBy(TrailerColumns.NAME, false);
        return this;
    }

    public TrailerSelection size(String... value) {
        addEquals(TrailerColumns.SIZE, value);
        return this;
    }

    public TrailerSelection sizeNot(String... value) {
        addNotEquals(TrailerColumns.SIZE, value);
        return this;
    }

    public TrailerSelection sizeLike(String... value) {
        addLike(TrailerColumns.SIZE, value);
        return this;
    }

    public TrailerSelection sizeContains(String... value) {
        addContains(TrailerColumns.SIZE, value);
        return this;
    }

    public TrailerSelection sizeStartsWith(String... value) {
        addStartsWith(TrailerColumns.SIZE, value);
        return this;
    }

    public TrailerSelection sizeEndsWith(String... value) {
        addEndsWith(TrailerColumns.SIZE, value);
        return this;
    }

    public TrailerSelection orderBySize(boolean desc) {
        orderBy(TrailerColumns.SIZE, desc);
        return this;
    }

    public TrailerSelection orderBySize() {
        orderBy(TrailerColumns.SIZE, false);
        return this;
    }

    public TrailerSelection source(String... value) {
        addEquals(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceNot(String... value) {
        addNotEquals(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceLike(String... value) {
        addLike(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceContains(String... value) {
        addContains(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceStartsWith(String... value) {
        addStartsWith(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection sourceEndsWith(String... value) {
        addEndsWith(TrailerColumns.SOURCE, value);
        return this;
    }

    public TrailerSelection orderBySource(boolean desc) {
        orderBy(TrailerColumns.SOURCE, desc);
        return this;
    }

    public TrailerSelection orderBySource() {
        orderBy(TrailerColumns.SOURCE, false);
        return this;
    }

    public TrailerSelection type(String... value) {
        addEquals(TrailerColumns.TYPE, value);
        return this;
    }

    public TrailerSelection typeNot(String... value) {
        addNotEquals(TrailerColumns.TYPE, value);
        return this;
    }

    public TrailerSelection typeLike(String... value) {
        addLike(TrailerColumns.TYPE, value);
        return this;
    }

    public TrailerSelection typeContains(String... value) {
        addContains(TrailerColumns.TYPE, value);
        return this;
    }

    public TrailerSelection typeStartsWith(String... value) {
        addStartsWith(TrailerColumns.TYPE, value);
        return this;
    }

    public TrailerSelection typeEndsWith(String... value) {
        addEndsWith(TrailerColumns.TYPE, value);
        return this;
    }

    public TrailerSelection orderByType(boolean desc) {
        orderBy(TrailerColumns.TYPE, desc);
        return this;
    }

    public TrailerSelection orderByType() {
        orderBy(TrailerColumns.TYPE, false);
        return this;
    }

    public TrailerSelection origin(String... value) {
        addEquals(TrailerColumns.ORIGIN, value);
        return this;
    }

    public TrailerSelection originNot(String... value) {
        addNotEquals(TrailerColumns.ORIGIN, value);
        return this;
    }

    public TrailerSelection originLike(String... value) {
        addLike(TrailerColumns.ORIGIN, value);
        return this;
    }

    public TrailerSelection originContains(String... value) {
        addContains(TrailerColumns.ORIGIN, value);
        return this;
    }

    public TrailerSelection originStartsWith(String... value) {
        addStartsWith(TrailerColumns.ORIGIN, value);
        return this;
    }

    public TrailerSelection originEndsWith(String... value) {
        addEndsWith(TrailerColumns.ORIGIN, value);
        return this;
    }

    public TrailerSelection orderByOrigin(boolean desc) {
        orderBy(TrailerColumns.ORIGIN, desc);
        return this;
    }

    public TrailerSelection orderByOrigin() {
        orderBy(TrailerColumns.ORIGIN, false);
        return this;
    }
}
