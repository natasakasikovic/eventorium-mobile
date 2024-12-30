package com.eventorium.data.solution.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.dtos.UpdatePriceListRequestDto;
import com.eventorium.data.solution.models.PriceListItem;
import com.eventorium.data.solution.services.PriceListService;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListRepository {

    private final PriceListService priceListService;

    @Inject
    public PriceListRepository(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    public LiveData<Result<List<PriceListItem>>> getServices() {
        MutableLiveData<Result<List<PriceListItem>>> result = new MutableLiveData<>();
        priceListService.getServices().enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<List<PriceListItem>>> getProducts() {
        MutableLiveData<Result<List<PriceListItem>>> result = new MutableLiveData<>();
        priceListService.getProducts().enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<PriceListItem>> updateService(Long id, UpdatePriceListRequestDto dto) {
        MutableLiveData<Result<PriceListItem>> result = new MutableLiveData<>();
        priceListService.updateService(id, dto).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<PriceListItem>> updateProduct(Long id, UpdatePriceListRequestDto dto) {
        MutableLiveData<Result<PriceListItem>> result = new MutableLiveData<>();
        priceListService.updateProduct(id, dto).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<Uri>> downloadPdf(Context context) {
        MutableLiveData<Result<Uri>> pdfFile = new MutableLiveData<>();
        priceListService.downloadPdf().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Uri uri = FileUtil.savePdfToDownloads(context, response.body());
                        if(uri != null) {
                            pdfFile.postValue(Result.success(uri));
                        } else {
                            pdfFile.postValue(Result.error("Failed to save pdf"));
                        }
                    } catch (IOException e) {
                        pdfFile.postValue(Result.error("Failed to download pdf: " + e.getMessage()));
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                pdfFile.postValue(Result.error("Failed to download pdf: " + t.getMessage()));
            }
        });

        return pdfFile;
    }

    private <T> Callback<T> handleResponse(MutableLiveData<Result<T>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error(response.message()));
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(Result.error(t.getMessage()));
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        };
    }
}
