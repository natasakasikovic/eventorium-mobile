package com.eventorium.data.auth.services;

import com.eventorium.data.auth.models.UpdateReportStatusRequest;
import com.eventorium.data.auth.models.UserReportRequest;
import com.eventorium.data.auth.models.UserReportResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserReportService {
    @GET("user-reports")
    Call<List<UserReportResponse>> getReports();

    @POST("user-reports/{offender-id}")
    Call<Void> reportUser(@Body UserReportRequest report, @Path("offender-id") Long id);

    @PATCH("user-reports/{offender-id}")
    Call<Void> updateReport(@Path("offender-id") Long id, @Body UpdateReportStatusRequest request);
}
