package com.eventorium.data.event.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventSummary implements Parcelable {
    private Long id;
    private String name;
    private String city;
    private Integer photo;

    protected EventSummary(Parcel in) {
        name = in.readString();
        city = in.readString();
        photo = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(city);
        dest.writeInt(photo);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Parcelable.Creator<EventSummary> CREATOR = new Creator<EventSummary>() {
        @Override
        public EventSummary createFromParcel(Parcel in) { return new EventSummary(in); }

        @Override
        public EventSummary[] newArray(int size) { return new EventSummary[size]; }
    };
}
