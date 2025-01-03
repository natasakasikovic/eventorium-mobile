package com.eventorium.presentation.chat.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.interaction.models.ChatMessage;
import com.eventorium.data.interaction.models.ChatMessageRequest;
import com.eventorium.data.interaction.repositories.ChatRepository;
import com.eventorium.data.util.services.WebSocketService;
import com.eventorium.presentation.util.listeners.OnMessageReceive;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final ChatRepository chatRepository;
    private WebSocketService webSocketService = null;

    @Inject
    public ChatViewModel(AuthRepository authRepository, ChatRepository chatRepository, WebSocketService webSocketService) {
        this.authRepository = authRepository;
        this.chatRepository = chatRepository;
        this.webSocketService = webSocketService;
    }

    public Long getUserId() {
        return authRepository.getUserId();
    }

    public LiveData<List<ChatMessage>> getMessages(Long senderId, Long recipientId) {
        return chatRepository.getMessages(senderId, recipientId);
    }

    public void setupMessageListener(OnMessageReceive onMessageReceive) {
        webSocketService.setChatMessageListener(onMessageReceive);
    }

    public void destroyMessageListener() {
        webSocketService.setChatMessageListener(null);
    }

    public void sendMessage(ChatMessage message) {
        webSocketService.sendMessage(ChatMessageRequest.builder()
                .chatName(message.getSenderId() + "_" + message.getRecipientId())
                .message(message.getMessage())
                .senderId(message.getSenderId())
                .recipientId(message.getRecipientId())
                .build());
    }
}
