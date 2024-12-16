package com.eventorium.data.solution.models;


import android.graphics.Bitmap;
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
public class ServiceSummary implements Parcelable {
    private Long id;
    private String name;
    private Double price;
    private Bitmap image;

    protected ServiceSummary(Parcel in) {
        name = in.readString();
        price = in.readDouble();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<ServiceSummary> CREATOR = new Creator<>() {
        @Override
        public ServiceSummary createFromParcel(Parcel in) { return new ServiceSummary(in); }

        @Override
        public ServiceSummary[] newArray(int size) { return new ServiceSummary[size]; }
    };

}
