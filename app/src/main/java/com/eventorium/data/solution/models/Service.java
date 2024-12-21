package com.eventorium.data.solution.models;


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
public class Service implements Parcelable {

    private String name;
    private Double price;
    private Integer photo;

    protected Service(Parcel in) {
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

    public static final Creator<Service> CREATOR = new Creator<>() {
        @Override
        public Service createFromParcel(Parcel in) { return new Service(in); }

        @Override
        public Service[] newArray(int size) { return new Service[size]; }
    };

}
