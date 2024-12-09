package com.eventorium.data.category.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category implements Parcelable {

    private Long id;
    private String name;
    private String description;

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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
