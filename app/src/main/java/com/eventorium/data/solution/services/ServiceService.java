package com.eventorium.data.solution.services;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.service.CalendarReservation;
import com.eventorium.data.solution.models.service.CreateService;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.models.service.UpdateService;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.data.shared.models.ImageResponse;
import com.eventorium.presentation.shared.models.RemoveImageRequest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ServiceService {

    @GET("services")
    Call<PagedResponse<ServiceSummary>> getServices(@Query("page") int page, @Query("size") int size);

    @GET("services/{id}")
    Call<Service> getService(@Path("id") Long id);

    @GET("services/top-five-services")
    Call<List<ServiceSummary>> getTopServices();

    @GET("services/{id}/image")
    Call<ResponseBody> getServiceImage(@Path("id") Long id);

    @GET("services/{id}/images")
    Call<List<ImageResponse>> getServiceImages(@Path("id") Long id);

    @POST("services")
    Call<ServiceSummary> createService(@Body CreateService dto);

    @Multipart
    @POST("services/{id}/images")
    Call<ResponseBody> uploadImages(@Path("id") Long id, @Part List<MultipartBody.Part> images);

    @HTTP(method = "DELETE", path = "services/{id}/images", hasBody = true)
    Call<ResponseBody> deleteImages(@Path("id") Long id, @Body List<RemoveImageRequest> images);

    @PUT("services/{id}")
    Call<Service> updateService(@Path("id") Long id, @Body UpdateService dto);

    @DELETE("services/{id}")
    Call<ResponseBody> deleteService(@Path("id") Long id);

    @GET("services/search")
    Call<PagedResponse<ServiceSummary>> searchServices(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("provider-reservations")
    Call<List<CalendarReservation>> getReservations();

    @GET("services/filter")
    Call<PagedResponse<ServiceSummary>> filterServices(@QueryMap Map<String, String> params);
}
