package com.eventorium.data.interaction.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.chat.ChatMessage;
import com.eventorium.data.interaction.services.ChatService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {

    private final ChatService chatService;

    @Inject
    public ChatRepository(ChatService chatService) {
        this.chatService = chatService;
    }

    public LiveData<List<ChatMessage>> getMessages(Long senderId, Long recipientId) {
        MutableLiveData<List<ChatMessage>> result = new MutableLiveData<>();
        chatService.getMessages(senderId, recipientId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ChatMessage>> call,
                    @NonNull Response<List<ChatMessage>> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChatMessage>> call, @NonNull Throwable t) {
                result.setValue(new ArrayList<>());
            }
        });
        return result;
    }
}
