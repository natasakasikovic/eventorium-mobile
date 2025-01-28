package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.Invitation;
import com.eventorium.data.event.repositories.InvitationRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class InvitationViewModel extends ViewModel {

    public InvitationRepository repository;

    @Inject
    public InvitationViewModel(InvitationRepository repository){
        this.repository = repository;
    }

    public void sendInvitations(Long id, List<Invitation> invitations){
        repository.sendInvitations(id, invitations);
    }
}