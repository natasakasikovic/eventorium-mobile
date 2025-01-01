package com.eventorium.presentation.chat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.interaction.models.ChatMessage;
import com.eventorium.databinding.FragmentChatBinding;
import com.eventorium.presentation.chat.adapters.ChatAdapter;
import com.eventorium.presentation.chat.viewmodels.ChatViewModel;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ChatViewModel chatViewModel;
    public static final String ARG_RECIPIENT_ID = "ARG_RECIPIENT_ID";

    private ChatAdapter adapter;
    private Long senderId;
    private Long recipientId;

    public ChatFragment() {
    }

    public static ChatFragment newInstance(Long recipientId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPIENT_ID, recipientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            recipientId = getArguments().getLong(ARG_RECIPIENT_ID);
        }
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.setupMessageListener(message -> {
            adapter.addMessage(message);
        });
        senderId = chatViewModel.getUserId();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        adapter = new ChatAdapter(senderId, new ArrayList<>());
        binding.chatRecyclerView.setAdapter(adapter);
        binding.sendButton.setOnClickListener(v -> {
            String message = Objects.requireNonNull(binding.messageInputEditText.getText()).toString();
            if(!message.isEmpty()) {
                ChatMessage chatMessage = new ChatMessage(senderId, recipientId, message);
                chatViewModel.sendMessage(chatMessage);
                adapter.addMessage(chatMessage);
                binding.messageInputEditText.setText("");
            }
        });
        loadMessages();
        return binding.getRoot();
    }

    private void loadMessages() {
        chatViewModel.getMessages(senderId, recipientId).observe(getViewLifecycleOwner(), messages -> {
            adapter.setData(messages);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}