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
    @SerializedName("quicktime")
    private List<Trailer> mQuicktime = new ArrayList<Trailer>();
    @SerializedName("youtube")
    private List<Trailer> mYoutube = new ArrayList<Trailer>();

    public List<Trailer> getQuicktime() {
        return mQuicktime;
    }

    public void setQuicktime(List<Trailer> quicktime) {
        this.mQuicktime = quicktime;
    }

    public List<Trailer> getYoutube() {
        return mYoutube;
    }

    public void setYoutube(List<Trailer> youtube) {
        this.mYoutube = youtube;
    }

    public int getCount() {
        return (mQuicktime.size() + mYoutube.size());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * INNER CLASS for marshalling/de-marshalling
     * a Trailer object
     */
    public static class Trailer {
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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final Trailer quickTime = mQuicktime.get(0);
        dest.writeString(quickTime.getName());
        dest.writeString(quickTime.getSize());
        dest.writeString(quickTime.getSource());
        dest.writeString(quickTime.getType());
        final Trailer youtube = mYoutube.get(0);
        dest.writeString(youtube.getName());
        dest.writeString(youtube.getSize());
        dest.writeString(youtube.getSource());
        dest.writeString(youtube.getType());
    }

    private Trailers(Parcel in) {
        mYoutube.add(new Trailer(in.readString(),
                in.readString(),
                in.readString(),
                in.readString()));

        mQuicktime.add(new Trailer(in.readString(),
                in.readString(),
                in.readString(),
                in.readString()));
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