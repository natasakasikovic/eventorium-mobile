package com.eventorium.presentation.util.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eventorium.data.repositories.CategoryRepository;
import com.eventorium.data.repositories.EventTypeRepository;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private CategoryRepository categoryRepository;
    private EventTypeRepository eventTypeRepository;

    public ViewModelFactory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ViewModelFactory(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    public ViewModelFactory(CategoryRepository categoryRepository, EventTypeRepository eventTypeRepository) {
        this.categoryRepository = categoryRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(categoryRepository);
        } else if (modelClass.isAssignableFrom(EventTypeViewModel.class)) {
            return (T) new EventTypeViewModel(eventTypeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
