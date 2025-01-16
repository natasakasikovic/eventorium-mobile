package com.eventorium.data.company.services;

import com.eventorium.data.company.models.Company;
import com.eventorium.data.company.models.CreateCompany;
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

public interface CompanyService {

    @POST("companies")
    Call<Company> registerCompany(@Body CreateCompany company);

    @Multipart
    @POST("companies/{id}/images")
    Call<ResponseBody> uploadImages(@Path("id") Long id, @Part List<MultipartBody.Part> images);

    @GET("companies/my-company")
    Call<Company> getCompany();

    @GET("companies/{id}/images")
    Call<List<ImageResponseDto>> getImages(@Path("id") Long id);
}
