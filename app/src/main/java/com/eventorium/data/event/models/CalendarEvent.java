package com.eventorium.data.event.models;

import android.os.Parcelable;

import lombok.*;

import java.time.LocalDate;

import android.os.Parcel;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@AllArgsConstructor
public class CalendarEvent implements Parcelable {
    private Long id;
    private String name;
    private LocalDate date;

    protected CalendarEvent(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        String dateString = in.readString();
        date = (dateString != null) ? LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CalendarEvent> CREATOR = new Creator<CalendarEvent>() {
        @Override
        public CalendarEvent createFromParcel(Parcel in) {
            return new CalendarEvent(in);
        }

        @Override
        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[size];
        }
    };

}
