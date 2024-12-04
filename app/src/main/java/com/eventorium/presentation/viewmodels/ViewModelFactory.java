package com.eventorium.presentation.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eventorium.data.repositories.CategoryRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final CategoryRepository categoryRepository;

    public ViewModelFactory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CategoryViewModel(categoryRepository);
    }
}
