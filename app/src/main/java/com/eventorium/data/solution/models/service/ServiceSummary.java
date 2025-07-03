package com.eventorium.data.solution.models.service;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.eventorium.data.shared.models.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ServiceSummary implements Parcelable {
    private Long id;
    private String name;
    private Double price;
    private Bitmap image;
    private Boolean available;
    private Boolean visible;
    private Status status;
    private Double discount;

    protected ServiceSummary(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        available = in.readBoolean();
        visible = in.readBoolean();
        discount = in.readDouble();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeBoolean(available);
        dest.writeBoolean(visible);
        dest.writeDouble(discount);
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
