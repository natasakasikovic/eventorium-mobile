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

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ReviewViewHolder>{

    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ReviewViewHolder holder, int position) {
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

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        Button userButton;
        TextView userTextView;
        TextView commentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            userButton = itemView.findViewById(R.id.userButton);
            userTextView = itemView.findViewById(R.id.userText);
            commentTextView = itemView.findViewById(R.id.commentText);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Comment comment) {
            userTextView.setText(comment.getUser().getName() + " " + comment.getUser().getLastname());
            commentTextView.setText(comment.getComment());
        }
    }
}