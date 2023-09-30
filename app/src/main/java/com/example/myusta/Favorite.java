package com.example.myusta;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {

    double longitude;
    double latitude;
    int dist;
    long numDays;
    String gender;
    String zipCode;

    public Favorite(double lat, double lon, String gen, int distance, long days, String zip) {
        latitude = lat;
        longitude = lon;
        gender = gen;
        dist = distance;
        numDays = days;
        zipCode = zip;
    }

    protected Favorite(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
        dist = in.readInt();
        numDays = in.readLong();
        gender = in.readString();
        zipCode = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeInt(dist);
        dest.writeLong(numDays);
        dest.writeString(gender);
        dest.writeString(zipCode);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public long getNumDays() {
        return numDays;
    }

    public void setNumDays(long numDays) {
        this.numDays = numDays;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
