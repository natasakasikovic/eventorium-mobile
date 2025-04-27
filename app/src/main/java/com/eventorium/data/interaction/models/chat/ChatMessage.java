package com.eventorium.data.interaction.models.chat;

import com.eventorium.data.auth.models.UserDetails;

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
    private UserDetails sender;

    public ChatMessage(Long senderId, Long recipientId, String message) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
    }

    public ChatMessage(Long senderId, Long recipientId, String message, UserDetails sender) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.sender = sender;
    }
}
