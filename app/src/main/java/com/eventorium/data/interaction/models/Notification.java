package com.eventorium.data.interaction.models;

import com.eventorium.data.util.models.NotificationType;

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
public class Notification {
    private String title;
    private String message;
    private Boolean seen;
    private NotificationType type;
}
