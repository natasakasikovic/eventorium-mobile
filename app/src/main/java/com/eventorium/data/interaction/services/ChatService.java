package com.eventorium.data.interaction.services;

import com.eventorium.data.interaction.models.chat.ChatMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatService {
    @GET("messages/{sender-id}/{recipient-id}")
    Call<List<ChatMessage>> getMessages(
            @Path("sender-id") Long senderId,
            @Path("recipient-id") Long recipientId
    );

}
