package com.eventorium.data.solution.services;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.ServiceSummaryResponseDto;
import com.eventorium.data.solution.dtos.UpdateServiceRequestDto;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceService {

    @GET("services/all")
    Call<List<ServiceSummary>> getServices();

    @GET("services/{id}")
    Call<Service> getService(@Path("id") Long id);

    @GET("services/top-five-services")
    Call<List<ServiceSummary>> getTopServices();

    @GET("services/{id}/image")
    Call<ResponseBody> getServiceImage(@Path("id") Long id);

    @GET("services/{id}/images")
    Call<List<ImageResponseDto>> getServiceImages(@Path("id") Long id);

    @GET("services/suggestions")
    Call<List<ServiceSummary>> getSuggestions(@Query("categoryId") Long categoryId, @Query("price") Double price);

    @POST("services")
    Call<ServiceSummaryResponseDto> createService(@Body CreateServiceRequestDto dto);

    @Multipart
    @POST("services/{id}/images")
    Call<ResponseBody> uploadImages(@Path("id") Long id, @Part List<MultipartBody.Part> images);

    @PUT("services/{id}")
    Call<Service> updateService(@Path("id") Long id, @Body UpdateServiceRequestDto dto);

    @DELETE("services/{id}")
    Call<Void> deleteService(@Path("id") Long id);

    @GET("services/search/all")
    Call<List<ServiceSummary>> searchServices(@Query("keyword") String keyword);
}
