package com.eventorium.data.auth.repositories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.auth.models.ChangePasswordRequest;
import com.eventorium.data.auth.models.Person;
import com.eventorium.data.auth.services.UserService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository {
    private final UserService service;

    @Inject
    public UserRepository(UserService userService) {
        this.service = userService;
    }

    public LiveData<Result<AccountDetails>> getCurrentUser() {
        MutableLiveData<Result<AccountDetails>> liveData = new MutableLiveData<>();

        service.getCurrentUser().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AccountDetails> call, Response<AccountDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error("Error while loading"));
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountDetails> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Bitmap> getProfilePhoto(Long id) {
        MutableLiveData<Bitmap> liveData = new MutableLiveData<>();

        service.getProfilePhoto(id).enqueue(new Callback<>() {
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

    public LiveData<Result<Void>> update(Person updateRequest) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();

        service.update(updateRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    liveData.postValue(Result.success(null));
                else {
                    try {
                        String error = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
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

    public LiveData<Boolean> uploadPhoto(Context context, Uri uri) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        MultipartBody.Part part;

        try {
            part = FileUtil.getImageFromUri(context, uri, "profilePhoto");
        } catch (IOException e) {
            result.setValue(false);
            return result;
        }

        service.uploadProfilePhoto(part).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) result.postValue(true);
                else result.postValue(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(false);
            }
        });

        return result;
    }

    public LiveData<Result<Void>> changePassword(ChangePasswordRequest request) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();

        service.changePassword(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    liveData.postValue(Result.success(null));
                else {
                    try {
                        String error = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
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

}
