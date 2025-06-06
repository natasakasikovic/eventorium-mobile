package com.eventorium.data.event.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Invitation;
import com.eventorium.data.event.models.InvitationDetails;
import com.eventorium.data.event.services.InvitationService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationRepository {

    private final InvitationService service;

    @Inject
    public InvitationRepository(InvitationService service){
        this.service = service;
    }

    public void sendInvitations(Long id, List<Invitation> invitations) {
        service.sendInvitations(id, invitations).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Invitations sent successfully!"); // TODO: remove this and notify user that everything is sent successfully
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // TODO: handle error
            }
        });
    }

    public LiveData<Result<List<InvitationDetails>>> getInvitations() {
        MutableLiveData<Result<List<InvitationDetails>>> result = new MutableLiveData<>();

        service.getInvitations().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<InvitationDetails>> call, Response<List<InvitationDetails>> response) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success(response.body()));
                else {
                    try {
                        String err = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(err)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<InvitationDetails>> call, Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });

        return result;
    }

}