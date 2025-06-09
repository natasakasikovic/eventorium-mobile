package com.eventorium.data.interaction.services;

import com.eventorium.data.interaction.models.comment.CreateComment;
import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.comment.UpdateComment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentService {

    @GET("comments/pending")
    Call<List<Comment>> getPendingComments();

    @POST("comments")
    Call<Comment> createProductComment(@Body CreateComment request);

    @PATCH("comments/{id}")
    Call<Comment> updateComment(@Path("id") Long id, @Body UpdateComment request);

}
