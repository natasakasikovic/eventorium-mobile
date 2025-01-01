package com.eventorium.data.solution.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.eventorium.data.util.models.Status;

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
public class ProductSummary implements Parcelable {
    private Long id;
    private String name;
    private Double price;
    private Bitmap image;
    private Double discount;
    private Boolean available;
    private Boolean visible;
    private Double rating;
    private Status status;
    protected ProductSummary(Parcel in) {
        id = in.readLong();
        name = in.readString();
        price = in.readDouble();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeDouble(price);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<ProductSummary> CREATOR = new Creator<ProductSummary>() {
        @Override
        public ProductSummary createFromParcel(Parcel in) { return new ProductSummary(in); }

        @Override
        public ProductSummary[] newArray(int size) { return new ProductSummary[size]; }
    };
}

