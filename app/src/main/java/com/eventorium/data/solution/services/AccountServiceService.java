package com.eventorium.data.solution.services;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.presentation.shared.viewmodels.PagedViewModel;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AccountServiceService {

    @GET("account/services")
    Call<PagedResponse<ServiceSummary>> getManageableServices(@Query("page") int page, @Query("size") int size);

    @GET("account/services/filter")
    Call<PagedResponse<ServiceSummary>> filterManageableServices(@QueryMap Map<String, String> params);

    @GET("account/services/search")
    Call<PagedResponse<ServiceSummary>> searchManageableServices(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("account/services/favourites")
    Call<List<ServiceSummary>> getFavouriteServices();

    @GET("account/services/favourites/{id}")
    Call<Boolean> isFavouriteService(@Path("id") Long id);

    @POST("account/services/favourites/{id}")
    Call<ResponseBody> addFavouriteService(@Path("id") Long id);

    @DELETE("account/services/favourites/{id}")
    Call<ResponseBody> removeFavouriteService(@Path("id") Long id);

}
