package com.eventorium.data.interaction.services;

import com.eventorium.data.interaction.models.ChatRoom;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChatRoomService {

    @GET("chat-rooms/all")
    Call<List<ChatRoom>> getChatRooms();
}
