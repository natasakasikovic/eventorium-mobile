package com.eventorium.data.company.repositories;

import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.company.models.Company;
import com.eventorium.data.company.models.CreateCompany;
import com.eventorium.data.company.services.CompanyService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyRepository {

    private final CompanyService service;

    @Inject
    public CompanyRepository(CompanyService service) {
        this.service = service;
    }

    public LiveData<Result<Company>> registerCompany(CreateCompany company) {
        MutableLiveData<Result<Company>> liveData = new MutableLiveData<>();
        service.registerCompany(company).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error(ErrorMessages.VALIDATION_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return  liveData;
    }

    public LiveData<Boolean> uploadImages(Long companyId, Context context, List<Uri> uris) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        List<MultipartBody.Part> parts;

        try {
            parts = FileUtil.getImagesFromUris(context, uris, "images");
        } catch (IOException e) {
            Log.e("IMAGES_ERROR", Objects.requireNonNull(e.getLocalizedMessage()));
            result.setValue(false);
            return result;
        }

        service.uploadImages(companyId, parts).enqueue(new Callback<>() {
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

    public LiveData<Result<Company>> getCompany() {
        MutableLiveData<Result<Company>> result = new MutableLiveData<>();
        service.getCompany().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        });
        return result;
    }

    public LiveData<Result<List<Bitmap>>> getImages(Long id) {
        MutableLiveData<Result<List<Bitmap>>> result = new MutableLiveData<>();
        service.getImages(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ImageResponseDto>> call, Response<List<ImageResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(
                            response.body().stream()
                                    .map(ImageResponseDto::getData)
                                    .map(FileUtil::convertToBitmap)
                                    .collect(toList())
                    ));
                } else {
                    result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                }
            }

            @Override
            public void onFailure(Call<List<ImageResponseDto>> call, Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        });

        return result;
    }

}
