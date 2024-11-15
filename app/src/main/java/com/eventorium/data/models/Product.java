package com.eventorium.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {

    private String name;
    private Double price;
    public Integer photo;

    public String getName() { return name; }
    public Double getPrice() { return price; }
    public Integer getPhoto() { return photo; }

    public Product(String name, Double price, Integer photo) {
        this.name = name;
        this.price = price;
        this.photo = photo;
    }

    public Product() {  }

    protected Product(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        photo = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(photo);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) { return new Product(in); }

        @Override
        public Product[] newArray(int size) { return new Product[size]; }
    };
}

