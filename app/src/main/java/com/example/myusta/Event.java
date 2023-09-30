package com.example.myusta;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    CharSequence gender;
    CharSequence eventType;
    CharSequence price;
    CharSequence age;


    public Event(CharSequence gender, CharSequence eventType, CharSequence price, CharSequence age) {
        this.gender = gender;
        this.eventType = eventType;
        this.price = price;
        this.age = age;
    }

    protected Event(Parcel in) {
        gender = in.readString();
        eventType = in.readString();
        price = in.readString();
        age = in.readString();
    }

    public static final Parcelable.Creator<Tournament> CREATOR = new Parcelable.Creator() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event() {

    }

    public CharSequence getgender() {
        return gender;
    }

    public void setgender(CharSequence gender) {
        this.gender = gender;
    }

    public CharSequence geteventType() {
        return eventType;
    }

    public void seteventType(CharSequence eventType) {
        this.eventType = eventType;
    }

    public CharSequence getprice() {
        return price;
    }

    public void setprice(CharSequence price) {
        this.price = price;
    }

    public CharSequence getAge() {
        return age;
    }

    public void setAge(CharSequence age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gender.toString());
        parcel.writeString(eventType.toString());
        parcel.writeString(price.toString());
        parcel.writeString(age.toString());
    }
}
