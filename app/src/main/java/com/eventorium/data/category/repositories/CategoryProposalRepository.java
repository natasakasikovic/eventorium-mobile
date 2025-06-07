package com.eventorium.data.category.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.category.models.CategoryRequest;
import com.eventorium.data.category.models.UpdateCategoryStatus;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.services.CategoryProposalService;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

public class CategoryProposalRepository {

    private final CategoryProposalService service;
    @Inject
    public CategoryProposalRepository(CategoryProposalService service) {
        this.service = service;
    }

    public LiveData<Result<List<Category>>> getCategoryProposals() {
        MutableLiveData<Result<List<Category>>> liveData = new MutableLiveData<>();
        service.getServiceProposals().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Category>> updateCategoryStatus(Long id, UpdateCategoryStatus updateCategoryStatus) {
        MutableLiveData<Result<Category>> liveData = new MutableLiveData<>();
        service.updateCategoryStatus(id, updateCategoryStatus).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Category>> updateCategoryProposal(Long id, CategoryRequest request) {
        MutableLiveData<Result<Category>> liveData = new MutableLiveData<>();
        service.updateCategoryProposal(id, request).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Category>> changeCategory(Long id, CategoryRequest categoryRequest) {
        MutableLiveData<Result<Category>> liveData = new MutableLiveData<>();
        service.changeCategoryProposal(id, categoryRequest).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }
}
