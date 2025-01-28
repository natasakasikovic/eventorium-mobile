package com.eventorium.data.interaction.services;

import com.eventorium.data.interaction.models.review.CreateReview;
import com.eventorium.data.interaction.models.review.Review;
import com.eventorium.data.interaction.models.review.UpdateReview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {

    @GET("/reviews/pending/all")
    Call<List<Review>> getPendingReviews();

    @POST("/products/{product-id}/reviews")
    Call<Review> createProductReview(@Path("product-id") Long id, @Body CreateReview request);

    @POST("/services/{service-id}/reviews")
    Call<Review> createServiceReview(@Path("service-id") Long id, @Body CreateReview request);

    @PATCH("/reviews/{id}")
    Call<Review> updateReview(@Path("id") Long id, @Body UpdateReview request);
}
