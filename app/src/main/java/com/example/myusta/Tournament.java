package com.example.myusta;

import android.os.Parcel;
import android.os.Parcelable;

public class Tournament implements Parcelable{

    CharSequence tournamentId;
    CharSequence tournamentName;
    double distance;
    CharSequence urlSegment;


    public Tournament(CharSequence tournamentId, CharSequence tournamentName, double distance, CharSequence urlSegment) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.distance = distance;
        this.urlSegment = urlSegment;
    }

    protected Tournament(Parcel in) {
        tournamentId = in.readString();
        tournamentName = in.readString();
        distance = in.readDouble();
        urlSegment = in.readString();

    }

    public static final Parcelable.Creator<Tournament> CREATOR = new Parcelable.Creator() {
        @Override
        public Tournament createFromParcel(Parcel in) {
            return new Tournament(in);
        }

        @Override
        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };

    public Tournament() {

    }

    public CharSequence getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(CharSequence tournamentId) {
        this.tournamentId = tournamentId;
    }

    public CharSequence getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(CharSequence tournamentName) {
        this.tournamentName = tournamentName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public CharSequence getUrlSegment() {
        return urlSegment;
    }

    public void setUrlSegment(CharSequence urlSegment) {
        this.urlSegment = urlSegment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tournamentId.toString());
        parcel.writeString(tournamentName.toString());
        parcel.writeDouble(distance);
        parcel.writeString(urlSegment.toString());
    }
}

