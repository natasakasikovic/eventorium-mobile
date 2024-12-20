package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.models.ServiceSummary;

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

    @GET("account/services/all")
    Call<List<ServiceSummary>> getManageableServices();

    @GET("account/services/filter/all")
    Call<List<ServiceSummary>> filterManageableServices(@QueryMap Map<String, String> params);

    @GET("account/services/search/all")
    Call<List<ServiceSummary>> searchManageableServices(@Query("keyword") String keyword);

    @GET("account/services/favourites")
    Call<List<ServiceSummary>> getFavouriteServices();

    @GET("account/services/favourites/{id}")
    Call<Boolean> isFavouriteService(@Path("id") Long id);

    @POST("account/services/favourites/{id}")
    Call<Service> addFavouriteService(@Path("id") Long id);

    @DELETE("account/services/favourites/{id}")
    Call<ResponseBody> removeFavouriteService(@Path("id") Long id);

}
