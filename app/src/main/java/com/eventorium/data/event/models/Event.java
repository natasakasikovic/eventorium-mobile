package com.eventorium.data.event.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.eventorium.data.shared.models.City;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event implements Parcelable {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Privacy privacy;
    private Integer maxParticipants;
    private EventType type;
    private String address;

    protected Event(Parcel in) {
        id = (in.readByte() == 0) ? null : in.readLong();
        name = in.readString();
        description = in.readString();
        date = (LocalDate) in.readSerializable();
        privacy = in.readParcelable(Privacy.class.getClassLoader());
        maxParticipants = in.readByte() == 0 ? null : in.readInt();
        type = in.readParcelable(EventType.class.getClassLoader());
        address = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }

        dest.writeString(name);
        dest.writeString(description);
        dest.writeSerializable(date);
        dest.writeParcelable(privacy, flags);
        if (maxParticipants == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxParticipants);
        }
        dest.writeParcelable(type, flags);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

