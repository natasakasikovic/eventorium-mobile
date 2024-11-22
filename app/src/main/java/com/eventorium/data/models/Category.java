package com.eventorium.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Category implements Parcelable {

    private String name;
    private String description;

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Category() {}
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Category(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) { return new Category(in); }

        @Override
        public Category[] newArray(int size) { return new Category[size]; }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }
}
