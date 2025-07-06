package com.eventorium.data.interaction.models;

import java.time.LocalDateTime;

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
public class ChatRoom {
    private Long id;
    private String displayName;
    private LocalDateTime timestamp;
    private String lastMessage;
    private Long recipientId;
}
