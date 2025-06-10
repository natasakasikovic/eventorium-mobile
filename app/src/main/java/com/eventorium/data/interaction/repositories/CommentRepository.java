package com.eventorium.data.interaction.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.comment.CreateComment;
import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.comment.UpdateComment;
import com.eventorium.data.interaction.services.CommentService;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

public class CommentRepository {

    private final CommentService commentService;

    @Inject
    public CommentRepository(CommentService commentService) {
        this.commentService = commentService;
    }

    public LiveData<Result<Comment>> createComment(CreateComment request) {
        MutableLiveData<Result<Comment>> result = new MutableLiveData<>();
        commentService.createComment(request).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<List<Comment>>> getPendingComments() {
        MutableLiveData<Result<List<Comment>>> liveData = new MutableLiveData<>();
        commentService.getPendingComments().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Comment>> updateComment(Long id, UpdateComment request) {
        MutableLiveData<Result<Comment>> result = new MutableLiveData<>();
        commentService.updateComment(id, request).enqueue(handleValidationResponse(result));
        return result;
    }
}

