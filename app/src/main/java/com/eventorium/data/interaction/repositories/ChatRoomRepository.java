package com.eventorium.data.interaction.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.ChatRoom;
import com.eventorium.data.interaction.services.ChatRoomService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomRepository {

    private final ChatRoomService service;

    @Inject
    public ChatRoomRepository(ChatRoomService service) {
        this.service = service;
    }

    public LiveData<List<ChatRoom>> getChatRooms() {
        MutableLiveData<List<ChatRoom>> result = new MutableLiveData<>();
        service.getChatRooms().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ChatRoom>> call,
                    @NonNull Response<List<ChatRoom>> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChatRoom>> call, @NonNull Throwable t) {
                result.setValue(new ArrayList<>());
            }
        });

        return result;
    }
}
