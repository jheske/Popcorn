/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Trailers implements Parcelable {
    @SerializedName("youtube")
    private List<Trailer> mYoutube;

    public List<Trailer> getYoutube() {
        return mYoutube;
    }

    public void setYoutube(List<Trailer> youtube) {
        this.mYoutube = youtube;
    }

    public int getCount() {
        return (mYoutube.size());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * INNER CLASS for marshalling/de-marshalling
     * a Trailer object
     */
    public static class Trailer implements Parcelable {
        @SerializedName("name")
        public String mName;
        @SerializedName("size")
        public String mSize;
        @SerializedName("source")
        public String mSource;
        @SerializedName("type")
        public String mType;

        public Trailer(String name,
                       String size,
                       String source,
                       String type) {
            mName = name;
            mSize = size;
            mSource = source;
            mType = type;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public String getSize() {
            return mSize;
        }

        public void setSize(String size) {
            this.mSize = size;
        }

        public String getSource() {
            return mSource;
        }

        public void setSource(String source) {
            this.mSource = source;
        }

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            this.mType = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mName);
            dest.writeString(mSize);
            dest.writeString(mSource);
            dest.writeString(mType);
        }

        private Trailer(Parcel in) {
            in.readString();
            in.readString();
            in.readString();
            in.readString();
        }

        public static final Parcelable.Creator<Trailer> CREATOR =
                new Parcelable.Creator<Trailer>() {
                    public Trailer createFromParcel(Parcel in) {
                        return new Trailer(in);
                    }

                    public Trailer[] newArray(int size) {
                        return new Trailer[size];
                    }
                };
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mYoutube);
    }

    private Trailers(Parcel in) {
        in.readTypedList(mYoutube,Trailer.CREATOR);
    }

    /**
     * public Parcelable.Creator for Trailers, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<Trailers> CREATOR =
            new Parcelable.Creator<Trailers>() {
                public Trailers createFromParcel(Parcel in) {
                    return new Trailers(in);
                }

                public Trailers[] newArray(int size) {
                    return new Trailers[size];
                }
            };
}