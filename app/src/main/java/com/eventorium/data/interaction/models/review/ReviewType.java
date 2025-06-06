package com.eventorium.data.interaction.models.review;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public enum ReviewType implements Parcelable {
    PRODUCT,
    SERVICE,
    EVENT;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.ordinal());
    }

    public static final Parcelable.Creator<ReviewType> CREATOR = new Parcelable.Creator<>() {
        @Override
        public ReviewType createFromParcel(Parcel in) {
            int ordinal = in.readInt();
            return ReviewType.values()[ordinal];
        }

        @Override
        public ReviewType[] newArray(int size) {
            return new ReviewType[size];
        }
    };
}
