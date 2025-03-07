package com.eventorium.presentation.chat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.interaction.models.ChatRoom;
import com.eventorium.presentation.chat.listeners.OnChatRoomClickListener;
import com.google.android.material.button.MaterialButton;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {
    private List<ChatRoom> chatRooms;
    private OnChatRoomClickListener listener;

    public ChatRoomAdapter(List<ChatRoom> chatRoomList, OnChatRoomClickListener listener) {
        this.chatRooms = chatRoomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_card, parent, false);
        return new ChatRoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.bind(chatRoom);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public void setData(List<ChatRoom> data) {
        chatRooms = data;
        notifyDataSetChanged();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        Button userButton;
        TextView userText;
        TextView lastMessage;
        TextView lastMessageTime;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            userButton = itemView.findViewById(R.id.userButton);
            userText = itemView.findViewById(R.id.userText);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            lastMessageTime = itemView.findViewById(R.id.lastMessageTime);
        }

        private void bind(ChatRoom chatRoom) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yy hh:mm");
            userText.setText(chatRoom.getDisplayName());
            lastMessage.setText(chatRoom.getLastMessage());
            lastMessageTime.setText(chatRoom.getTimestamp().format(formatter));

            userButton.setOnClickListener(v -> listener.navigateToUser(chatRoom.getRecipientId()));
            itemView.setOnClickListener(v -> listener.navigateToChat(chatRoom));
        }
    }
}
