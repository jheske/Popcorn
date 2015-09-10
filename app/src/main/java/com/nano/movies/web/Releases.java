package com.nano.movies.web;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jill on 9/6/2015.
 */
public class Releases implements Parcelable {
    @SerializedName("countries")
    private List<Release> mCountries = new ArrayList<>();

    public int getCount() {
        return (mCountries.size());
    }

    public String getMpaaRating(String country) {
        for (Release release: mCountries) {
            if (release.mCountryName.equals(country))
              return (release.getCertification());
        }
        return "No rating";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class Release implements Parcelable {
        @SerializedName("iso_3166_1")
        public String mCountryName;
        @SerializedName("certification")
        public String mCertification;

        public Release(String countryName,
                      String certification) {
            mCountryName = countryName;
            mCertification = certification;
        }

        public String getCountryName() {
            return mCountryName;
        }

        public void setCountryName(String countryName) {
            this.mCountryName = countryName;
        }

        public String getCertification() {
            return mCertification;
        }

        public void setCertification(String certification) {
            this.mCertification = certification;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mCountryName);
            dest.writeString(mCertification);
        }

        private Release(Parcel in) {
            in.readString();
            in.readString();
        }

        public static final Parcelable.Creator<Release> CREATOR =
                new Parcelable.Creator<Release>() {
                    public Release createFromParcel(Parcel in) {
                        return new Release(in);
                    }

                    public Release[] newArray(int size) {
                        return new Release[size];
                    }
                };
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mCountries);
    }

    private Releases(Parcel in) {
        in.readTypedList(mCountries,Release.CREATOR);
    }

    public static final Parcelable.Creator<Releases> CREATOR =
            new Parcelable.Creator<Releases>() {
                public Releases createFromParcel(Parcel in) {
                    return new Releases(in);
                }

                public Releases[] newArray(int size) {
                    return new Releases[size];
                }
            };
}
