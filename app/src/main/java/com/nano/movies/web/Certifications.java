package com.nano.movies.web;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jill on 9/6/2015.
 */
public class Certifications implements Parcelable {
    @SerializedName("youtube")
    private List<Country> mCountries;

    public int getCount() {
        return (mCountries.size());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class Country implements Parcelable {
        @SerializedName("iso_3166_1")
        public String mCountryName;
        @SerializedName("certification")
        public String mCertification;

        public Country(String countryName,
                      String certification) {
            mCountryName = countryName;
            mCertification = certification;
        }

        public String getCountryName() {
            return mCountryName;
        }

        public void setCountryName(String mCountryName) {
            this.mCountryName = mCountryName;
        }

        public String getCertification() {
            return mCertification;
        }

        public void setCertification(String mCertification) {
            this.mCertification = mCertification;
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

        private Country(Parcel in) {
            in.readString();
            in.readString();
        }

        public static final Parcelable.Creator<Country> CREATOR =
                new Parcelable.Creator<Country>() {
                    public Country createFromParcel(Parcel in) {
                        return new Country(in);
                    }

                    public Country[] newArray(int size) {
                        return new Country[size];
                    }
                };
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mCountries);
    }

    private Certifications(Parcel in) {
        in.readTypedList(mCountries,Country.CREATOR);
    }

    public static final Parcelable.Creator<Certifications> CREATOR =
            new Parcelable.Creator<Certifications>() {
                public Certifications createFromParcel(Parcel in) {
                    return new Certifications(in);
                }

                public Certifications[] newArray(int size) {
                    return new Certifications[size];
                }
            };
}
