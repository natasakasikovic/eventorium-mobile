package com.eventorium.presentation.review.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.interaction.models.review.ManageReview;
import com.eventorium.data.interaction.models.review.Review;
import com.eventorium.presentation.review.listeners.OnReviewListener;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ManageReview> reviews;
    private final OnReviewListener listener;

    public ReviewAdapter(List<ManageReview> reviews, OnReviewListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ManageReview review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setData(List<ManageReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        Button userButton;
        TextView userTextView;
        Button solutionButton;
        TextView solutionTextView;
        TextView feedbackTextView;
        TextView ratingTextView;
        Button acceptButton;
        Button declineButton;


        public ReviewViewHolder(View itemView) {
            super(itemView);
            userButton = itemView.findViewById(R.id.userButton);
            userTextView = itemView.findViewById(R.id.userText);
            solutionButton = itemView.findViewById(R.id.solutionButton);
            solutionTextView = itemView.findViewById(R.id.solutionText);
            feedbackTextView = itemView.findViewById(R.id.feedbackText);
            ratingTextView = itemView.findViewById(R.id.ratingText);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ManageReview review) {
            userTextView.setText(review.getUser().getName() + " " + review.getUser().getLastname());
            solutionTextView.setText(review.getSolution().getName());
            feedbackTextView.setText(review.getFeedback());
            ratingTextView.setText(review.getRating().toString() + "/5");

            userButton.setOnClickListener(v -> listener.navigateToProvider(review.getUser()));
            solutionButton.setOnClickListener(v -> listener.navigateToSolution(review.getSolution()));
            acceptButton.setOnClickListener(v -> listener.acceptReview(review.getId()));
            declineButton.setOnClickListener(v -> listener.declineReview(review.getId()));
        }
    }
}

