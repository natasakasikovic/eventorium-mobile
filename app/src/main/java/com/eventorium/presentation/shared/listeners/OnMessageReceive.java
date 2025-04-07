package com.eventorium.presentation.shared.listeners;

import com.eventorium.data.interaction.models.chat.ChatMessage;

public interface OnMessageReceive {
    void onNewChatMessage(ChatMessage chatMessage);
}
