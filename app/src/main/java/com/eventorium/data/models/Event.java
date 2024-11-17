package com.eventorium.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Event implements Parcelable {

    private String name;
    private String location;
    private Integer photo;

    public String getName() { return name; }
    public String getLocation() { return location; }
    public Integer getPhoto(){ return photo; }

    public Event(String name, String location, Integer photo) {
        this.name = name;
        this.location = location;
        this.photo = photo;
    }

    public Event() {  }

    protected Event(Parcel in) {
        name = in.readString();
        location = in.readString();
        photo = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeInt(photo);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Parcelable.Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) { return new Event(in); }

        @Override
        public Event[] newArray(int size) { return new Event[size]; }
    };

}
