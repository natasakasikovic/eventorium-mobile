package com.eventorium.data.interaction.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.ChatRoom;
import com.eventorium.data.interaction.services.ChatRoomService;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;

import java.util.List;

import javax.inject.Inject;

public class ChatRoomRepository {

    private final ChatRoomService service;

    @Inject
    public ChatRoomRepository(ChatRoomService service) {
        this.service = service;
    }

    public LiveData<Result<List<ChatRoom>>> getChatRooms() {
        MutableLiveData<Result<List<ChatRoom>>> result = new MutableLiveData<>();
        service.getChatRooms().enqueue(RetrofitCallbackHelper.handleGeneralResponse(result));
        return result;
    }
}
