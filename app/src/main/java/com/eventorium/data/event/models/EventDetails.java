package com.eventorium.data.event.models;

import com.eventorium.data.auth.models.ChatUserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDetails {
    private String name;
    private String description;
    private String eventType;
    private String maxParticipants;
    private String privacy;
    private String address;
    private String city;
    private String date;
    private ChatUserDetails organizer;
}
