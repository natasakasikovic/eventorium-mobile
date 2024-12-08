package com.eventorium.data.solution.services;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.ServiceResponseDto;
import com.eventorium.data.solution.models.Service;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceService {

    @POST("services")
    Call<ServiceResponseDto> createService(@Body CreateServiceRequestDto dto);

    @Multipart
    @POST("service/{id}/images")
    Call<String> uploadImages(@Path("id") Long id, @Part List<MultipartBody.Part> images);
}
