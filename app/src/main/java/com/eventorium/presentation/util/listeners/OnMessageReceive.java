package com.eventorium.presentation.util.listeners;

import com.eventorium.data.interaction.models.ChatMessage;

public interface OnMessageReceive {
    void onNewChatMessage(ChatMessage chatMessage);
}
