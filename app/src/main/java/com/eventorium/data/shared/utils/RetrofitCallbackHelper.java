package com.eventorium.data.shared.utils;

import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.ImageResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.ImageItem;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallbackHelper {

    public static Callback<ResponseBody> handleVoidResponse(MutableLiveData<Result<Void>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (response.isSuccessful()) {
                    result.postValue(Result.success(null));
                } else {
                    try {
                        String error = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Throwable t
            ) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }

    public static<T> Callback<T> handleGeneralResponse(MutableLiveData<Result<T>> result) {
        return handleResponse(result, ErrorMessages.GENERAL_ERROR);
    }

    public static<T> Callback<T> handleValidationResponse(MutableLiveData<Result<T>> result) {
        return handleResponse(result, ErrorMessages.VALIDATION_ERROR);
    }

    public static<T> Callback<T> handleSuccessfulResponse(MutableLiveData<T> data) {
        return new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response.body() != null && response.isSuccessful()) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        };
    }

    public static Callback<ResponseBody> handleGetImage(MutableLiveData<Bitmap> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        result.postValue(bitmap);
                    } catch (Exception e) {
                        result.postValue(null);
                    }
                } else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(null);
            }
        };
    }

    public static<T> Callback<T> handleSuccessAsBoolean(MutableLiveData<Boolean> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if (response.isSuccessful()) result.postValue(true);
                else result.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(false);
            }
        };
    }

    public static Callback<List<ImageResponse>> handleGetImages(MutableLiveData<Result<List<ImageItem>>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ImageResponse>> call,
                    @NonNull Response<List<ImageResponse>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(
                            response.body().stream()
                                    .map(image -> new ImageItem(image.getId(), FileUtil.convertToBitmap(image.getData())))
                                    .collect(toList())
                    ));
                } else {
                    result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ImageResponse>> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }
    public static<T> Callback<T> handleResponse(MutableLiveData<Result<T>> result, String error) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success((response.body())));
                else {
                    try {
                        String errorResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(error));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }

    public static Callback<ResponseBody> handlePdfExport(Context context, MutableLiveData<Result<Uri>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Uri uri = FileUtil.savePdfToDownloads(context, response.body());
                        if (uri != null) result.postValue(Result.success(uri));
                        else handlePdfErrorResponse(response, result);
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.PDF_ERROR));
                    }
                }
                else result.postValue(Result.error(ErrorMessages.PDF_ERROR));
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.PDF_ERROR));
            }
        };
    }

    private static void handlePdfErrorResponse(Response<ResponseBody> response, MutableLiveData<Result<Uri>> result) {
        try {
            if (response.errorBody() != null) {
                String err = response.errorBody().string();
                result.postValue(Result.error(ErrorResponse.getErrorMessage(err)));
            }
            else result.postValue(Result.error(ErrorMessages.PDF_ERROR));
        } catch (IOException e) {
            result.postValue(Result.error(ErrorMessages.PDF_ERROR));
        }
    }

}
