package com.eventorium.data.interaction.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.EventType;

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
public class MessageSender implements Parcelable {
    private Long id;
    private String name;
    private String lastname;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageSender> CREATOR = new Creator<MessageSender>() {
        @Override
        public MessageSender createFromParcel(Parcel in) {
            return new MessageSender(in);
        }

        @Override
        public MessageSender[] newArray(int size) {
            return new MessageSender[size];
        }
    };

    protected MessageSender(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.lastname = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(lastname);
    }

}
