package com.eventorium.data.auth.models;

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
public class UserReportResponse implements Parcelable {

    private Long id;
    private String reason;
    private String offender;
    private String reporter;

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id != null ? id : -1);
        dest.writeString(reason);
        dest.writeString(offender);
        dest.writeString(reporter);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<UserReportResponse> CREATOR = new Creator<>() {
        @Override
        public UserReportResponse createFromParcel(Parcel in) {
            Long id = in.readLong();
            if (id == -1) id = null;
            String reason = in.readString();
            String offender = in.readString();
            String reporter = in.readString();
            return new UserReportResponse(id, reason, offender, reporter);
        }

        @Override
        public UserReportResponse[] newArray(int size) {
            return new UserReportResponse[size];
        }
    };
}