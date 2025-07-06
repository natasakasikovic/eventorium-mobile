package com.eventorium.data.event.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public enum Privacy implements Parcelable {
    OPEN,
    CLOSED;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.ordinal());
    }

        public static final Parcelable.Creator<Privacy> CREATOR = new Parcelable.Creator<>() {
        @Override
        public Privacy createFromParcel(Parcel in) {
            int ordinal = in.readInt();
            return Privacy.values()[ordinal];
        }

        @Override
        public Privacy[] newArray(int size) {
            return new Privacy[size];
        }
    };
}
