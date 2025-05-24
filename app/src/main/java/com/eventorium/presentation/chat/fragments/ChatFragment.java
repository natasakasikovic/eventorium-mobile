package com.eventorium.presentation.chat.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.chat.ChatMessage;
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
    public static final String ARG_RECIPIENT = "ARG_RECIPIENT";

    private ChatAdapter adapter;
    private Long senderId;
    private UserDetails recipient;

    public ChatFragment() {
    }

    public static ChatFragment newInstance(UserDetails sender) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPIENT, sender);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            recipient = getArguments().getParcelable(ARG_RECIPIENT);
        }
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.setupMessageListener(message -> {
            if(binding.chatTitle.getText().toString().isEmpty()) {
                binding.chatTitle.setText(message.getSender().getName() + " " + message.getSender().getLastname());
            }
            adapter.addMessage(message);
        });
        senderId = chatViewModel.getUserId();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        adapter = new ChatAdapter(senderId, new ArrayList<>());
        binding.chatTitle.setText(recipient.getName() + " " + recipient.getLastname());
        binding.chatRecyclerView.setAdapter(adapter);
        binding.sendButton.setOnClickListener(v -> sendMessage());
        loadMessages();
        return binding.getRoot();
    }

    private void sendMessage() {
        String message = Objects.requireNonNull(binding.messageInputEditText.getText()).toString();
        if(message.trim().length() != 0) {
            ChatMessage chatMessage = new ChatMessage(senderId, recipient.getId(), message);
            chatViewModel.sendMessage(chatMessage);
            adapter.addMessage(chatMessage);
            binding.messageInputEditText.setText("");
        }
    }

    private void loadMessages() {
        chatViewModel.getMessages(senderId, recipient.getId()).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                adapter.setData(result.getData());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        chatViewModel.destroyMessageListener();
    }
}