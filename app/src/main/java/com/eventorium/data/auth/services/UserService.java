package com.eventorium.data.auth.services;

import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.auth.models.ChangePasswordRequest;
import com.eventorium.data.auth.models.Person;

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

public interface UserService {
    @GET("users/me")
    Call<AccountDetails> getCurrentUser();

    @GET("users/{id}")
    Call<AccountDetails> getUser(@Path("id") Long id);

    @GET("users/{id}/profile-photo")
    Call<ResponseBody> getProfilePhoto(@Path("id") Long id);

    @PUT("users")
    Call<ResponseBody> update(@Body Person updateRequest);

    @Multipart
    @PUT("users/profile-photo")
    Call<ResponseBody> uploadProfilePhoto(@Part MultipartBody.Part photo);

    @POST("users/password")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequest request);

    @POST("user-blocking/{user-id}")
    Call<ResponseBody> blockUser(@Path("user-id") Long id);

    @DELETE("users")
    Call<ResponseBody> deactivateAccount();
}