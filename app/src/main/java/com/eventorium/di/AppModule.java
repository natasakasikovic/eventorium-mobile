package com.eventorium.di;

import com.eventorium.data.category.repositories.CategoryRepository;
import com.eventorium.data.category.services.CategoryService;
import com.eventorium.data.event.repositories.EventTypeRepository;
import com.eventorium.data.event.services.EventTypeService;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.solution.services.AccountServiceService;
import com.eventorium.data.solution.services.ServiceService;
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

    @Provides
    @Singleton
    public static EventTypeService provideEventTypeService() {
        return RetrofitApi.retrofit.create(EventTypeService.class);
    }
    @Provides
    @Singleton
    public static EventTypeRepository provideEventTypeRepository(EventTypeService service) {
        return new EventTypeRepository(service);
    }

    @Provides
    @Singleton
    public static ServiceService provideServiceService() {
        return RetrofitApi.retrofit.create(ServiceService.class);
    }

    @Provides
    @Singleton
    public static ServiceRepository provideServiceRepository(ServiceService serviceService) {
        return new ServiceRepository(serviceService);
    }

    @Provides
    @Singleton
    public static AccountServiceService provideAccountServiceService() {
        return RetrofitApi.retrofit.create(AccountServiceService.class);
    }

    @Provides
    @Singleton
    public static AccountServiceRepository provideAccountServiceRepository(AccountServiceService service) {
        return new AccountServiceRepository(service);
    }

}
