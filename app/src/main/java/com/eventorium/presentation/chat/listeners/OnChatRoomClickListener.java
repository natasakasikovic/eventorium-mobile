package com.eventorium.presentation.chat.listeners;

import com.eventorium.data.interaction.models.ChatRoom;

public interface OnChatRoomClickListener {
    void navigateToChat(ChatRoom room);
    void navigateToUser(Long id);
}
