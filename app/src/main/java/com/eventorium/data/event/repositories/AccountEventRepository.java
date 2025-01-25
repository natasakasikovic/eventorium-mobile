package com.eventorium.data.event.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.services.AccountEventService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountEventRepository {

    private final AccountEventService service;

    public AccountEventRepository(AccountEventService service) {
        this.service = service;
    }

    public LiveData<Boolean> isFavouriteEvent(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        service.isFavouriteEvent(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) result.postValue(response.body());
                else result.postValue(false);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<Result<Void>> addToFavourites(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.addToFavourites(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) result.postValue(Result.success(null));
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
            public void onFailure(Call<Void> call, Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });
        return result;
    }

    public LiveData<Result<Void>> removeFromFavourites(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.removeFromFavourites(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) result.postValue(Result.success(null));
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });

        return result;
    }

    public LiveData<Result<List<EventSummary>>> getFavouriteEvents() {
        MutableLiveData<Result<List<EventSummary>>> result = new MutableLiveData<>();
        service.getFavouriteEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<EventSummary>> call, Response<List<EventSummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error("Error while loading favourite events"));
                }
            }

            @Override
            public void onFailure(Call<List<EventSummary>> call, Throwable t) {
                result.postValue(Result.error("Error while loading favourite events"));
            }
        });
        return result;
    }
}
