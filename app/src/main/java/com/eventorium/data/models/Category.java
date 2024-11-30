package com.eventorium.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Category implements Parcelable {

    private Long id;
    private String name;
    private String description;

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }


    public Category() {}
    public Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    protected Category(Parcel in) {
        this.id = in.readLong();
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
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
    }
}
