package com.eventorium.data.interaction.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.eventorium.data.event.models.EventType;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessage {
    private Long senderId;
    private Long recipientId;
    private String message;
    private LocalDateTime timestamp;
    private MessageSender sender;

    public ChatMessage(Long senderId, Long recipientId, String message) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
    }

    public ChatMessage(Long senderId, Long recipientId, String message, MessageSender sender) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.sender = sender;
    }
}
