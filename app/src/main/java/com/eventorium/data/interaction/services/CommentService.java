package com.eventorium.data.interaction.services;

import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.comment.CreateComment;
import com.eventorium.data.interaction.models.comment.UpdateComment;
import com.eventorium.data.interaction.models.review.ReviewType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentService {

    @GET("comments/pending")
    Call<List<Comment>> getPendingComments();

    @POST("comments")
    Call<Comment> createComment(@Body CreateComment request);

    @PATCH("comments/{id}")
    Call<Comment> updateComment(@Path("id") Long id, @Body UpdateComment request);

    @GET("comments")
    Call<List<Comment>> getAcceptedCommentsForTarget(@Query("type") ReviewType type, @Query("id") long objectId);
}