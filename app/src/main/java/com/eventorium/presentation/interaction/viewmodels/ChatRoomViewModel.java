package com.eventorium.presentation.interaction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.interaction.models.ChatRoom;
import com.eventorium.data.interaction.repositories.ChatRoomRepository;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatRoomViewModel extends ViewModel {

    private final ChatRoomRepository repository;

    @Inject
    public ChatRoomViewModel(ChatRoomRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<ChatRoom>>> getChatRooms() {
        return repository.getChatRooms();
    }

}
