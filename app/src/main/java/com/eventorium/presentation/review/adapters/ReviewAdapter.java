package com.eventorium.presentation.review.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.interaction.models.rating.Rating;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.presentation.review.listeners.OnReviewListener;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.stream.IntStream;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<SolutionReview> reviews;
    private OnReviewListener listener;

    public ReviewAdapter(List<SolutionReview> reviews, OnReviewListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        SolutionReview review = reviews.get(position);
        if(review.getRating() == null) {
            configureStarHandlers(holder, position, review);
        }
        holder.bind(review);
    }

    private void configureStarHandlers(@NonNull ReviewViewHolder holder, int position, SolutionReview review) {
        IntStream.range(0, holder.stars.length).forEach(i ->
            holder.stars[i].setOnClickListener(v -> {
                review.setRating(new Rating(i + 1));
                notifyItemChanged(position);
            })
        );
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setData(List<SolutionReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView solutionImage;
        TextView solutionName;
        ImageView[] stars = new ImageView[5];
        Button seeMoreButton, rateButton, commentButton;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            solutionImage = itemView.findViewById(R.id.solutionImage);
            solutionName = itemView.findViewById(R.id.solutionName);
            seeMoreButton = itemView.findViewById(R.id.seeMoreButton);
            rateButton = itemView.findViewById(R.id.rateButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            loadStars();
        }

        private void updateStars(int rating) {
            IntStream.range(0, stars.length).forEach(i ->
                    stars[i].setImageResource(i < rating ? R.drawable.ic_star_filled : R.drawable.ic_star)
            );
        }

        private void loadStars() {
            stars[0] = itemView.findViewById(R.id.star1);
            stars[1] = itemView.findViewById(R.id.star2);
            stars[2] = itemView.findViewById(R.id.star3);
            stars[3] = itemView.findViewById(R.id.star4);
            stars[4] = itemView.findViewById(R.id.star5);
        }

        public void bind(SolutionReview review) {
            solutionName.setText(review.getName());
            if(review.getRating() != null) {
                rateButton.setVisibility(View.GONE);
                updateStars(review.getRating().getRating());
            }
            solutionImage.setImageBitmap(review.getImage());

            seeMoreButton.setOnClickListener(v -> listener.onSeeMoreClick(review));
            rateButton.setOnClickListener(v -> listener.onRateClick(review));
            commentButton.setOnClickListener(v -> listener.onCommentClick(review));
        }
    }
}

