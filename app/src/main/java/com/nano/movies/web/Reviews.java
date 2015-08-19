/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Reviews implements Parcelable {
    @SerializedName("results")
    private List<Review> mResults = new ArrayList<Review>();

    public List<Review> getResults() {
        return mResults;
    }

    public void setResults(List<Review> results) {
        this.mResults = results;
    }

    public int getCount() {
        return mResults.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * INNER CLASS for marshalling/de-marshalling
     * a Review object
     *
     */
    public static class Review implements Parcelable {
        @SerializedName("id")
        public String mId;
        @SerializedName("author")
        public String mAuthor;
        @SerializedName("content")
        public String mContent;
        @SerializedName("url")
        public String mUrl;

        public Review(String id,
                      String author,
                      String content,
                      String url) {
            mId = id;
            mAuthor = author;
            mContent = content;
            mUrl = url;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            this.mId = id;
        }

        public String getAuthor() {
            return mAuthor;
        }

        public void setAuthor(String author) {
            this.mAuthor = author;
        }

        public String getContent() {
            return mContent;
        }

        public void setContent(String content) {
            this.mContent = content;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String url) {
            this.mUrl = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mAuthor);
            dest.writeString(mContent);
            dest.writeString(mUrl);
        }

        private Review(Parcel in) {
            in.readString();
            in.readString();
            in.readString();
            in.readString();
        }

        public static final Parcelable.Creator<Review> CREATOR =
                new Parcelable.Creator<Review>() {
                    public Review createFromParcel(Parcel in) {
                        return new Review(in);
                    }

                    public Review[] newArray(int size) {
                        return new Review[size];
                    }
                };
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mResults);
    }

    private Reviews(Parcel in) {
        in.readTypedList(mResults, Review.CREATOR);
    }

    /**
     * public Parcelable.Creator for Reviews which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<Reviews> CREATOR =
            new Parcelable.Creator<Reviews>() {
                public Reviews createFromParcel(Parcel in) {
                    return new Reviews(in);
                }

                public Reviews[] newArray(int size) {
                    return new Reviews[size];
                }
            };
}
