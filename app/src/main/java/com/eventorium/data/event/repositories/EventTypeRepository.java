package com.eventorium.data.event.repositories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.CreateEventType;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.services.EventTypeService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeRepository {

    private final EventTypeService eventTypeService;

    @Inject
    public EventTypeRepository(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    public LiveData<EventType> createEventType(CreateEventType eventType) {
        MutableLiveData<EventType> liveData = new MutableLiveData<>();

        eventTypeService.createEventType(eventType).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<EventType> call,
                    @NonNull Response<EventType> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<EventType> call,
                    @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });

        return liveData;
    }

    public LiveData<List<EventType>> getEventTypes() {
        MutableLiveData<List<EventType>> liveData = new MutableLiveData<>();

        eventTypeService.getEventTypes().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<EventType>> call,
                    @NonNull Response<List<EventType>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<EventType>> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getLocalizedMessage());
                liveData.postValue(new ArrayList<>());
            }
        });

        return liveData;
    }

    public LiveData<Result<Void>> updateEventType(EventType eventType) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();

        eventTypeService.updateEventType(eventType.getId(), eventType).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(Result.success(null));
                } else {
                    try {
                        String error = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException exception) {
                        liveData.postValue(Result.error(ErrorMessages.VALIDATION_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }

    public LiveData<Result<Void>> deleteEventType(Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();

        eventTypeService.delete(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(Result.success(null));
                } else {
                    try {
                        String error = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Boolean> uploadImage(Long id, Context context, Uri uri) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        MultipartBody.Part part;

        try {
            part = FileUtil.getImageFromUri(context, uri, "image");
        } catch (IOException e) {
            result.setValue(false);
            return result;
        }

        eventTypeService.uploadImage(id, part).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) result.postValue(true);
                else {
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<Bitmap> getImage(Long id) {
        MutableLiveData<Bitmap> liveData = new MutableLiveData<>();

        eventTypeService.getImage(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        liveData.postValue(bitmap);
                    } catch (Exception e) {
                        liveData.postValue(null);
                    }
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    public LiveData<Boolean> updateImage(Long id, Context context, Uri uri) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        MultipartBody.Part part;

        try {
            part = FileUtil.getImageFromUri(context, uri, "image");
        } catch (IOException e) {
            result.setValue(false);
            return result;
        }

        eventTypeService.updateImage(id, part).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) result.postValue(true);
                else {
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(false);
            }
        });
        return result;
    }
}
