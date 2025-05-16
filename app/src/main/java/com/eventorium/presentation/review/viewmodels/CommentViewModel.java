package com.eventorium.presentation.review.viewmodels;

import static java.util.stream.Collectors.toList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.interaction.models.comment.CreateComment;
import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.comment.UpdateComment;
import com.eventorium.data.interaction.repositories.CommentRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.models.Status;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@Getter
@HiltViewModel
public class CommentViewModel extends ViewModel {

    private final CommentRepository commentRepository;
    private final MutableLiveData<List<Comment>> comments = new MutableLiveData<>();

    @Inject
    public CommentViewModel(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void getPendingComments() {
        commentRepository.getPendingComments().observeForever(this.comments::postValue);
    }

    public void removeComment(Long id) {
        comments.setValue(Objects.requireNonNull(comments.getValue()).stream()
                .filter(review -> !review.getId().equals(id))
                .collect(toList()));
    }


    public LiveData<Result<Comment>> createProductComment(Long id, String comment) {
        return commentRepository.createProductComment(id, new CreateComment(comment));
    }

    public LiveData<Result<Comment>> createServiceComment(Long id, String comment) {
        return commentRepository.createServiceComment(id, new CreateComment(comment));
    }

    public LiveData<Result<Comment>> createEventComment(Long id, String comment) {
        return commentRepository.createEventComment(id, new CreateComment(comment));
    }

    public LiveData<Result<Void>> updateComment(Long id, Status status) {
        return commentRepository.updateComment(id, new UpdateComment(status));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        commentRepository.getPendingComments().removeObserver(comments::postValue);
    }
}
