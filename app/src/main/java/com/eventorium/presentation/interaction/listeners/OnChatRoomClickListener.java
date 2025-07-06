package com.eventorium.presentation.interaction.listeners;

import com.eventorium.data.interaction.models.ChatRoom;

public interface OnChatRoomClickListener {
    void navigateToChat(ChatRoom room);
    void navigateToUser(Long id);
}
