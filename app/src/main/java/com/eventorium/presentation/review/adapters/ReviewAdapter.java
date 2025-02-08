package com.eventorium.presentation.review.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.interaction.models.rating.Rating;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.presentation.review.listeners.OnReviewListener;

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
        holder.bind(review);
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
        private int selectedRating = 0;
        private boolean ratingLocked = false;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            solutionImage = itemView.findViewById(R.id.solutionImage);
            solutionName = itemView.findViewById(R.id.solutionName);
            seeMoreButton = itemView.findViewById(R.id.seeMoreButton);
            rateButton = itemView.findViewById(R.id.rateButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            loadStars();
        }

        private void loadStars() {
            stars[0] = itemView.findViewById(R.id.star1);
            stars[1] = itemView.findViewById(R.id.star2);
            stars[2] = itemView.findViewById(R.id.star3);
            stars[3] = itemView.findViewById(R.id.star4);
            stars[4] = itemView.findViewById(R.id.star5);
        }

        private void updateStars(int rating) {
            IntStream.range(0, stars.length).forEach(i ->
                    stars[i].setImageResource(i < rating ? R.drawable.ic_star_filled : R.drawable.ic_star)
            );
        }

        private void enableStarSelection() {
            IntStream.range(0, stars.length).forEach(i ->
                    stars[i].setOnClickListener(v -> {
                        if (!ratingLocked) {
                            selectedRating = i + 1;
                            updateStars(selectedRating);
                        }
                    })
            );
        }

        public void bind(SolutionReview review) {
            solutionName.setText(review.getName());
            solutionImage.setImageBitmap(review.getImage());

            if (review.getRating() != null) {
                selectedRating = review.getRating().getRating();
                updateStars(selectedRating);
                disableRating();
            } else {
                enableStarSelection();
                rateButton.setOnClickListener(v -> {
                    if (selectedRating > 0) {
                        review.setRating(new Rating(selectedRating));
                        listener.onRateClick(review, selectedRating);
                        ratingLocked = true;
                        disableRating();
                    }
                });
            }

            seeMoreButton.setOnClickListener(v -> listener.onSeeMoreClick(review));
            commentButton.setOnClickListener(v -> listener.onCommentClick(review));
        }

        private void disableRating() {
            ratingLocked = true;
            rateButton.setVisibility(View.GONE);
            for (ImageView star : stars) {
                star.setOnClickListener(null);
            }
        }
    }
}
