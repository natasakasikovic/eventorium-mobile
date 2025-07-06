package com.eventorium.data.solution.models.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalendarReservation implements Parcelable {
    private Long id;
    private String eventName;
    private String serviceName;
    private LocalDate date;

    protected CalendarReservation(Parcel in) {
        if (in.readByte() == 0) id = null;
        else id = in.readLong();

        eventName = in.readString();
        serviceName = in.readString();
        String dateString = in.readString();
        date = (dateString != null) ? LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) dest.writeByte((byte) 0);
        else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(eventName);
        dest.writeString(serviceName);
        dest.writeString(date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CalendarReservation> CREATOR = new Creator<>() {
        @Override
        public CalendarReservation createFromParcel(Parcel in) {
            return new CalendarReservation(in);
        }

        @Override
        public CalendarReservation[] newArray(int size) {
            return new CalendarReservation[size];
        }
    };
}
