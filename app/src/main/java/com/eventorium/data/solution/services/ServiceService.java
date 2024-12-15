package com.eventorium.data.solution.services;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.ServiceSummaryResponseDto;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceService {

    @GET("services/all")
    Call<List<ServiceSummaryResponseDto>> getServices();

    @GET("services/{id}")
    Call<Service> getService(@Path("id") Long id);

    @GET("services/{id}/image")
    Call<String> getServiceImage(@Path("id") Long id);

    @GET("services/{id}/images")
    Call<List<ImageResponseDto>> getServiceImages(@Path("id") Long id);

    @POST("services")
    Call<ServiceSummaryResponseDto> createService(@Body CreateServiceRequestDto dto);

    @Multipart
    @POST("services/{id}/images")
    Call<ResponseBody> uploadImages(@Path("id") Long id, @Part List<MultipartBody.Part> images);
}
