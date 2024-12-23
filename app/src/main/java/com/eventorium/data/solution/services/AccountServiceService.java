package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.ServiceSummary;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AccountServiceService {

    @GET("account/services/all")
    Call<List<ServiceSummary>> getManageableServices();

    @GET("account/services/filter/all")
    Call<List<ServiceSummary>> filterManageableServices(@QueryMap Map<String, String> params);

    @GET("account/services/search/all")
    Call<List<ServiceSummary>> searchManageableServices(@Query("keyword") String keyword);

}
