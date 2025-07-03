package com.eventorium.data.interaction.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.chat.ChatMessage;
import com.eventorium.data.interaction.services.ChatService;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

public class ChatRepository {

    private final ChatService chatService;

    @Inject
    public ChatRepository(ChatService chatService) {
        this.chatService = chatService;
    }

    public LiveData<Result<List<ChatMessage>>> getMessages(Long senderId, Long recipientId) {
        MutableLiveData<Result<List<ChatMessage>>> result = new MutableLiveData<>();
        chatService.getMessages(senderId, recipientId).enqueue(handleGeneralResponse(result));
        return result;
    }
}
