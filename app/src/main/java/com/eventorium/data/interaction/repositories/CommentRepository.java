package com.eventorium.data.interaction.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.comment.CreateComment;
import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.comment.UpdateComment;
import com.eventorium.data.interaction.services.CommentService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {

    private final CommentService commentService;

    @Inject
    public CommentRepository(CommentService commentService) {
        this.commentService = commentService;
    }

    public LiveData<Result<Comment>> createProductComment(Long id, CreateComment request) {
        MutableLiveData<Result<Comment>> result = new MutableLiveData<>();
        commentService.createProductComment(id, request).enqueue(handleRequest(result));
        return result;
    }

    public LiveData<Result<Comment>> createServiceComment(Long id, CreateComment request) {
        MutableLiveData<Result<Comment>> result = new MutableLiveData<>();
        commentService.createServiceComment(id, request).enqueue(handleRequest(result));
        return result;
    }

    public LiveData<List<Comment>> getPendingComments() {
        MutableLiveData<List<Comment>> liveData = new MutableLiveData<>();
        commentService.getPendingComments().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Comment>> call,
                    @NonNull Response<List<Comment>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                liveData.postValue(new ArrayList<>());
            }
        });
        return liveData;
    }

    public LiveData<Result<Void>> updateComment(Long id, UpdateComment request) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        commentService.updateComment(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Comment> call,
                    @NonNull Response<Comment> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(null));
                } else {
                    result.postValue(Result.error("Failed to update review"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        });
        return result;
    }

    private Callback<Comment> handleRequest(MutableLiveData<Result<Comment>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Comment> call,
                    @NonNull Response<Comment> response
            ) {
                if (response.isSuccessful()) {
                    result.postValue(Result.success(null));
                } else {
                    try {
                        String error = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        };
    }
}

