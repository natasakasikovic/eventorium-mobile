package com.eventorium.presentation.chat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.interaction.models.ChatRoom;
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.databinding.FragmentChatRoomBinding;
import com.eventorium.presentation.chat.adapters.ChatRoomAdapter;
import com.eventorium.presentation.chat.listeners.OnChatRoomClickListener;
import com.eventorium.presentation.chat.viewmodels.ChatRoomViewModel;
import com.eventorium.presentation.user.fragments.UserProfileFragment;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatRoomFragment extends Fragment {

    private FragmentChatRoomBinding binding;
    private ChatRoomViewModel viewModel;
    private ChatRoomAdapter adapter;

    public ChatRoomFragment() {
    }

    public static ChatRoomFragment newInstance() {
        return new ChatRoomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false);
        configureAdapter();
        configureListener();
        binding.chatRoomsRecycleView.setAdapter(adapter);
        return binding.getRoot();
    }

    private void configureListener() {
        viewModel.getChatRooms().observe(getViewLifecycleOwner(), chatRooms -> {
            adapter.setData(chatRooms);
        });
    }

    private void configureAdapter() {
        adapter = new ChatRoomAdapter(new ArrayList<>(), new OnChatRoomClickListener() {
            @Override
            public void navigateToChat(ChatRoom room) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                String[] displayName = room.getDisplayName().split(" ");
                UserDetails sender = UserDetails.builder()
                        .id(room.getRecipientId())
                        .name(displayName[0])
                        .lastname(displayName[1])
                        .build();
                args.putParcelable(ChatFragment.ARG_RECIPIENT, sender);
                navController.navigate(R.id.chatFragment, args);
            }

            @Override
            public void navigateToUser(Long id) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(UserProfileFragment.ARG_ID, id);
                navController.navigate(R.id.otherProfileFragment, args);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}