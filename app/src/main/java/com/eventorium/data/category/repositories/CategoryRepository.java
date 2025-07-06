package com.eventorium.data.category.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleValidationResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.models.CategoryRequest;
import com.eventorium.data.category.services.CategoryService;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

public class CategoryRepository {

    private final CategoryService categoryService;

    @Inject
    public CategoryRepository(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public LiveData<Result<List<Category>>> getCategories() {
        MutableLiveData<Result<List<Category>>> liveData = new MutableLiveData<>();
        categoryService.getCategories().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Category>> updateCategory(Long id, CategoryRequest request) {
        MutableLiveData<Result<Category>> liveData = new MutableLiveData<>();
        categoryService.updateCategory(id, request).enqueue(handleValidationResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Category>> createCategory(CategoryRequest request) {
        MutableLiveData<Result<Category>> liveData = new MutableLiveData<>();
        categoryService.createCategory(request).enqueue(handleValidationResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> deleteCategory(Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        categoryService.deleteCategory(id).enqueue(handleVoidResponse(liveData));
        return liveData;
    }
}
