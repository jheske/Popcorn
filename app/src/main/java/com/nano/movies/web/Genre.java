package com.nano.movies.web;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Genre implements Parcelable {
    @SerializedName("name")
    public String mName;

    public Genre(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    // Support Movie.getGenres()
    // which returns a comma separated list of genre names
    public String toString() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
    }

    private Genre(Parcel in) {
        in.readString();
    }

    public static final Parcelable.Creator<Genre> CREATOR =
            new Parcelable.Creator<Genre>() {
                public Genre createFromParcel(Parcel in) {
                    return new Genre(in);
                }

                public Genre[] newArray(int size) {
                    return new Genre[size];
                }
            };
}
