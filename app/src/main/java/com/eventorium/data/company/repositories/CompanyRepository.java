package com.eventorium.data.company.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.company.models.Company;
import com.eventorium.data.company.models.CompanyDetails;
import com.eventorium.data.company.models.CreateCompany;
import com.eventorium.data.company.services.CompanyService;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.RemoveImageRequest;
import com.eventorium.presentation.shared.models.ImageItem;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;

public class CompanyRepository {

    private final CompanyService service;

    @Inject
    public CompanyRepository(CompanyService service) {
        this.service = service;
    }

    public LiveData<Result<Company>> registerCompany(CreateCompany company) {
        MutableLiveData<Result<Company>> liveData = new MutableLiveData<>();
        service.registerCompany(company).enqueue(handleValidationResponse(liveData));
        return  liveData;
    }

    public LiveData<Boolean> uploadImages(Long companyId, Context context, List<Uri> uris) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        List<MultipartBody.Part> parts;

        try {
            parts = FileUtil.getImagesFromUris(context, uris, "images");
        } catch (IOException e) {
            result.setValue(false);
            return result;
        }
        service.uploadImages(companyId, parts).enqueue(handleSuccessAsBoolean(result));

        return result;
    }

    public LiveData<Result<Company>> getCompany() {
        MutableLiveData<Result<Company>> result = new MutableLiveData<>();
        service.getCompany().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<CompanyDetails>> getCompany(Long id) {
        MutableLiveData<Result<CompanyDetails>> result = new MutableLiveData<>();
        service.getCompany(id).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<ImageItem>>> getImages(Long id) {
        MutableLiveData<Result<List<ImageItem>>> result = new MutableLiveData<>();
        service.getImages(id).enqueue(handleGetImages(result));
        return result;
    }

    public LiveData<Result<Company>> update(Company company) {
        MutableLiveData<Result<Company>> result = new MutableLiveData<>();
        service.update(company).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<Void>> removeImages(List<RemoveImageRequest> removedImages) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.removeImages(removedImages).enqueue(handleVoidResponse(result));
        return result;
    }

}
