package com.eventorium.presentation.chat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.interaction.models.chat.ChatMessage;

import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;

    private List<ChatMessage> messages;
    private final Long userId;

    public ChatAdapter(Long userId, List<ChatMessage> messages) {
        this.messages = messages;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        return Objects.equals(messages.get(position).getSenderId(), userId)
                ? VIEW_TYPE_SENDER
                : VIEW_TYPE_RECEIVER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENDER) {
            View view = inflater.inflate(R.layout.sender_message, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.receiver_message, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof SenderViewHolder) {
            ((SenderViewHolder) holder).bind(message);
        } else if (holder instanceof ReceiverViewHolder) {
            ((ReceiverViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage newMessage) {
        messages.add(newMessage);
        notifyItemInserted(messages.size() - 1);
    }

    public void setData(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTextView;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message);
        }

        public void bind(ChatMessage message) {
            messageTextView.setText(message.getMessage());
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTextView;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message);
        }

        public void bind(ChatMessage message) {
            messageTextView.setText(message.getMessage());
        }
    }
}

