package com.eventorium.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.eventorium.BuildConfig;
import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.data.category.repositories.CategoryProposalRepository;
import com.eventorium.data.category.repositories.CategoryRepository;
import com.eventorium.data.category.services.CategoryProposalService;
import com.eventorium.data.category.services.CategoryService;
import com.eventorium.data.event.repositories.EventTypeRepository;
import com.eventorium.data.event.services.EventTypeService;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.solution.services.AccountServiceService;
import com.eventorium.data.solution.services.ServiceService;
import com.eventorium.data.util.AuthInterceptor;
import com.eventorium.data.util.adapters.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/v1/";

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        return new Retrofit.Builder()
                .baseUrl(SERVICE_API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public static OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public static SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public static AuthInterceptor provideAuthInterceptor(SharedPreferences sharedPreferences) {
        return new AuthInterceptor(sharedPreferences);
    }

    @Provides
    @Singleton
    @Inject
    public CategoryService provideCategoryService(Retrofit retrofit) {
        return retrofit.create(CategoryService.class);
    }

    @Provides
    @Singleton
    public static CategoryRepository provideCategoryRepository(CategoryService categoryService) {
        return new CategoryRepository(categoryService);
    }

    @Provides
    @Singleton
    @Inject
    public EventTypeService provideEventTypeService(Retrofit retrofit) {
        return retrofit.create(EventTypeService.class);
    }

    @Provides
    @Singleton
    public static EventTypeRepository provideEventTypeRepository(EventTypeService service) {
        return new EventTypeRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public ServiceService provideServiceService(Retrofit retrofit) {
        return retrofit.create(ServiceService.class);
    }

    @Provides
    @Singleton
    public static ServiceRepository provideServiceRepository(ServiceService serviceService) {
        return new ServiceRepository(serviceService);
    }

    @Provides
    @Singleton
    @Inject
    public AccountServiceService provideAccountServiceService(Retrofit retrofit) {
        return retrofit.create(AccountServiceService.class);
    }

    @Provides
    @Singleton
    @Inject
    public CategoryProposalService provideCategoryProposalService(Retrofit retrofit) {
        return retrofit.create(CategoryProposalService.class);
    }

    @Provides
    @Singleton
    public CategoryProposalRepository provideCategoryProposalRepository(
            CategoryProposalService service
    ) {
        return new CategoryProposalRepository(service);
    }

    @Provides
    @Singleton
    public static AccountServiceRepository provideAccountServiceRepository(AccountServiceService service) {
        return new AccountServiceRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public AuthService provideAuthService(Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }

    @Provides
    @Singleton
    public static AuthRepository authRepository(AuthService service,
                                                SharedPreferences sharedPreferences)
    {
        return new AuthRepository(service, sharedPreferences);
    }

}
