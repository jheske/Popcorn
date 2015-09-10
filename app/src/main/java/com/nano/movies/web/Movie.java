/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.facebook.stetho.common.StringUtil;
import com.google.gson.annotations.SerializedName;
import com.nano.movies.data.movie.MovieCursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data of
 * interest downloaded in Json from the MovieService.  We
 * don't care about all the data offered by the service,
 * just the fields defined in this class.
 */
public class Movie implements Parcelable {
    /*
     * These fields store the Tmdb state.
     * The @SerializedName annotation makes an explicit mapping
     * between each member variable and its Json name.
     * If we named these fields the same as the Json
     * names we wouldn't need to use this annotation.
     */
    @SerializedName("id")
    private Integer id;
    @SerializedName("homepage")
    private String mHomePage;
    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("popularity")
    private Double mPopularity;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("release_date")
    private Date mReleaseDate;
    @SerializedName("runtime")
    private Integer mRuntime;
    @SerializedName("tagline")
    private String mTagline;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("vote_average")
    private Double mVoteAverage;
    @SerializedName("vote_count")
    private Integer mVoteCount;
    @SerializedName("genres")
    private List<Genre> mGenres = new ArrayList<>();
    // Used with append_to_response=trailers
    @SerializedName("trailers")
    private Trailers mTrailers;
    // Used with append_to_response=reviews
    @SerializedName("reviews")
    private Reviews mReviews;
    private Releases mReleases;
    private final String TAG = getClass().getSimpleName();


    /**
     * Instantiate a movie from a database row.
     *
     * @param cursor
     */
    public Movie(Cursor cursor) {
        MovieCursor movieCursor = new MovieCursor(cursor);
        id = movieCursor.getTmdbId();
        mHomePage = movieCursor.getHomepage();
        mOriginalTitle = movieCursor.getOriginalTitle();
        mOverview = movieCursor.getOverview();
        mPopularity = movieCursor.getPopularity();
        mPosterPath = movieCursor.getPosterPath();
        mReleaseDate = movieCursor.getReleaseDate();
        mRuntime = movieCursor.getRuntime();
        mTagline = movieCursor.getTagline();
        mTitle = movieCursor.getTitle();
        mVoteAverage = movieCursor.getVoteAverage();
        mVoteCount = movieCursor.getVoteCount();
    }

    public Movie(Integer id, String homePage,
                 String originalTitle,
                 String overview,
                 Double popularity,
                 String posterPath,
                 Date releaseDate,
                 Integer runtime,
                 String tagline,
                 String title,
                 Double voteAverage,
                 Integer voteCount) {
        this.id = id;
        this.mHomePage = homePage;
        this.mOriginalTitle = originalTitle;
        this.mOverview = overview;
        this.mPopularity = popularity;
        this.mPosterPath = posterPath;
        this.mReleaseDate = releaseDate;
        this.mRuntime = runtime;
        this.mTagline = tagline;
        this.mTitle = title;
        this.mVoteAverage = voteAverage;
        this.mVoteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHomePage() {
        return mHomePage;
    }

    public void setHomePage(String mHomePage) {
        this.mHomePage = mHomePage;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.mBackdropPath = backdropPath;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public Double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Double mPopularity) {
        this.mPopularity = mPopularity;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setReleases(Releases releases) {
        mReleases = releases;
    }

    public String getUSRating() {
        if (mReleases == null)
            return "";
        return mReleases.getMpaaRating("US");
    }

    public Integer getRuntime() {
        if (mRuntime == null)
            return 0;
        return mRuntime;
    }

    public void setRuntime(Integer mRuntime) {
        this.mRuntime = mRuntime;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String mTagline) {
        this.mTagline = mTagline;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public Integer getVoteCount() {
        return mVoteCount;
    }

    //Just get the first one in the list, good enough
    public String getGenres() {
        if (mGenres == null)
            return ("");
        if (mGenres.size() == 0)
            return ("");

        String allGenres = TextUtils.join(", ", mGenres);
        return (allGenres);
    }

    public int getTrailerCount() {
        return mTrailers.getCount();
    }

    public Trailers getTrailers() {
        return mTrailers;
    }

    public int getReviewCount() {
        return mReviews.getCount();
    }

    public Reviews getReviews() {
        return mReviews;
    }


    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an data from the Parcel of data.
     * <p/>
     * The order of reading in variables
     * MUST MATCH the order in
     * writeToParcel(Parcel, int)
     */
    private Movie(Parcel in) {
        id = in.readInt();
        mHomePage = in.readString();
        mBackdropPath = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mPopularity = in.readDouble();
        mPosterPath = in.readString();
        mReleaseDate = new Date(in.readLong());
        try {
            mRuntime = in.readInt();
        } catch (IllegalArgumentException x) {
            mRuntime = 0;
        }
        mTagline = in.readString();
        mTitle = in.readString();
        mVoteAverage = in.readDouble();
        mVoteCount = in.readInt();
        in.readTypedList(mGenres, Genre.CREATOR);
        mTrailers = in.readParcelable(Trailers.class.getClassLoader());
        mReviews = in.readParcelable(Reviews.class.getClassLoader());
    }

    /**
     * Used to marshall the data to the parcel.
     * <p/>
     * The order of writing out variables
     * MUST MATCH the order in
     * the constructor Movie(Parcel in)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mHomePage);
        dest.writeString(mBackdropPath);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeDouble(mPopularity);
        dest.writeString(mPosterPath);
        dest.writeLong((mReleaseDate == null) ? 0 : mReleaseDate.getTime());
        dest.writeInt((mRuntime == null) ? 0 : mRuntime);
        dest.writeString(mTagline);
        dest.writeString(mTitle);
        dest.writeDouble((mVoteAverage == null) ? 0 : mVoteAverage);
        dest.writeInt((mVoteCount == null) ? 0 : mVoteCount);
        dest.writeTypedList(mGenres);
        dest.writeParcelable(mTrailers, flags);
        dest.writeParcelable(mReviews, flags);
    }

    /**
     * A required bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * public Parcelable.Creator for Movie, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of the Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>() {
                public Movie createFromParcel(Parcel in) {
                    return new Movie(in);
                }

                public Movie[] newArray(int size) {
                    return new Movie[size];
                }
            };
}



