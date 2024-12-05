package com.eventorium.di;

import com.eventorium.data.category.repositories.CategoryRepository;
import com.eventorium.data.category.services.CategoryService;
import com.eventorium.data.util.RetrofitApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public static CategoryService provideCategoryService() {
        return RetrofitApi.retrofit.create(CategoryService.class);
    }

    @Provides
    @Singleton
    public static CategoryRepository provideCategoryRepository(CategoryService categoryService) {
        return new CategoryRepository(categoryService);
    }


}
