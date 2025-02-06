package com.eventorium.presentation.review.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.comment.Commentable;
import com.eventorium.presentation.review.listeners.OnReviewListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ReviewViewHolder> {

    private List<Comment> comments;
    private final OnReviewListener listener;

    public CommentAdapter(List<Comment> comments, OnReviewListener listener) {
        this.comments = comments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setData(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        Button userButton;
        TextView userTextView;
        TextView commentableTextView;
        TextView commentTextView;
        Button acceptButton;
        Button declineButton;
        Button commentableButton;



        public ReviewViewHolder(View itemView) {
            super(itemView);
            userButton = itemView.findViewById(R.id.userButton);
            userTextView = itemView.findViewById(R.id.userText);
            commentableButton = itemView.findViewById(R.id.commentableButton);
            commentableTextView = itemView.findViewById(R.id.commentableText);
            commentTextView = itemView.findViewById(R.id.commentText);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Comment comment) {
            userTextView.setText(comment.getUser().getName() + " " + comment.getUser().getLastname());
            commentableTextView.setText(comment.getCommentable().getDisplayName());
            commentTextView.setText(comment.getComment());

            Commentable commentable = comment.getCommentable();

            userButton.setOnClickListener(v -> listener.navigateToProvider(comment.getUser()));
            acceptButton.setOnClickListener(v -> listener.acceptReview(comment.getId()));
            declineButton.setOnClickListener(v -> listener.declineReview(comment.getId()));
            commentableButton.setOnClickListener(v -> listener.navigateToCommentable(comment.getType(), commentable));
        }
    }
}

