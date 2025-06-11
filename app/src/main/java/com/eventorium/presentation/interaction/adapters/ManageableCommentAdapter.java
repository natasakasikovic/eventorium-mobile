package com.eventorium.presentation.interaction.adapters;

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
import com.eventorium.presentation.interaction.listeners.OnManageCommentListener;

import java.util.List;

public class ManageableCommentAdapter extends RecyclerView.Adapter<ManageableCommentAdapter.ReviewViewHolder> {

    private List<Comment> comments;
    private final OnManageCommentListener listener;

    public ManageableCommentAdapter(List<Comment> comments, OnManageCommentListener listener) {
        this.comments = comments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manageable_comment_card, parent, false);
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
        TextView objectTextView;
        TextView commentTextView;
        Button acceptButton;
        Button declineButton;
        Button detailsButton;



        public ReviewViewHolder(View itemView) {
            super(itemView);
            userButton = itemView.findViewById(R.id.userButton);
            userTextView = itemView.findViewById(R.id.userText);
            detailsButton = itemView.findViewById(R.id.commentableButton);
            objectTextView = itemView.findViewById(R.id.commentableText);
            commentTextView = itemView.findViewById(R.id.commentText);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Comment comment) {
            userTextView.setText(comment.getUser().getName() + " " + comment.getUser().getLastname());
            objectTextView.setText(comment.getDisplayName());
            commentTextView.setText(comment.getComment());

            userButton.setOnClickListener(v -> listener.navigateToProvider(comment.getUser()));
            acceptButton.setOnClickListener(v -> listener.acceptComment(comment.getId()));
            declineButton.setOnClickListener(v -> listener.declineComment(comment.getId()));
            detailsButton.setOnClickListener(v -> listener.navigateToDetails(comment.getType(), comment.getObjectId()));
        }
    }
}

