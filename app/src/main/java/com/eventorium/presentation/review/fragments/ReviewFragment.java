package com.eventorium.presentation.review.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.interaction.models.review.ReviewType;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.databinding.FragmentReviewBinding;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.review.adapters.ReviewAdapter;
import com.eventorium.presentation.review.listeners.OnReviewListener;
import com.eventorium.presentation.review.viewmodels.RatingViewModel;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private BudgetViewModel budgetViewModel;
    private ProductViewModel productViewModel;
    private ServiceViewModel serviceViewModel;
    private RatingViewModel ratingViewModel;
    private ReviewAdapter reviewAdapter;

    public ReviewFragment() {
    }

    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        budgetViewModel = provider.get(BudgetViewModel.class);
        productViewModel = provider.get(ProductViewModel.class);
        serviceViewModel = provider.get(ServiceViewModel.class);
        ratingViewModel = provider.get(RatingViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        setupAdapter();
        loadItems();
        return binding.getRoot();
    }

    private void setupAdapter() {
        reviewAdapter = new ReviewAdapter(new ArrayList<>(), new OnReviewListener() {
            @Override
            public void onSeeMoreClick(SolutionReview review) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                if(review.getType().equals(ReviewType.SERVICE)) {
                    args.putLong(ServiceDetailsFragment.ARG_ID, review.getId());
                    navController.navigate(R.id.action_review_to_serviceDetails, args);
                } else {
                    args.putLong(ProductDetailsFragment.ARG_ID, review.getId());
                    navController.navigate(R.id.action_review_to_productDetails, args);
                }
            }

            @Override
            public void onCommentClick(SolutionReview review) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(CommentFragment.ARG_COMMENTABLE_ID, review.getId());
                args.putString(CommentFragment.ARG_NAME, review.getName());
                args.putParcelable(CommentFragment.ARG_TYPE, review.getType());
                navController.navigate(R.id.action_review_to_comment, args);
            }

            @Override
            public void onRateClick(SolutionReview review, Integer rating) {
                Long id = review.getId();
                if(review.getType().equals(ReviewType.SERVICE)) {
                    rateService(id, rating, review.getName());
                } else {
                    rateProduct(id, rating, review.getName());
                }
            }
        });
        binding.reviewsRecycleView.setAdapter(reviewAdapter);
    }

    private void loadItems() {
        budgetViewModel.getBudgetItems().observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                reviewAdapter.setData(result.getData());
                loadImages(result.getData());
            } else {
                showError(result.getError());
            }
        });
    }

    private void loadImages(List<SolutionReview> solutions) {
        solutions.forEach(solution -> loadImage(solution, solutions));
    }

    private void rateService(Long id, Integer rating, String name) {
        ratingViewModel.createServiceRating(id, rating).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                showSuccessRating(name);
            } else {
                showError(result.getError());
            }
        });
    }

    private void rateProduct(Long id, Integer rating, String name) {
        ratingViewModel.createProductRating(id, rating).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                showSuccessRating(name);
            } else {
                showError(result.getError());
            }
        });
    }

    private void loadImage(SolutionReview solution, List<SolutionReview> solutions) {
        if(solution.getType() == ReviewType.PRODUCT) {
            loadProductImage(solution, solutions);
        } else {
            loadServiceImage(solution, solutions);
        }
    }


    private void loadProductImage(SolutionReview solution, List<SolutionReview> solutions) {
        productViewModel.getProductImage(solution.getId()).observe(getViewLifecycleOwner(), image -> {
            if (image != null) {
                setImage(image, solution, solutions.indexOf(solution));
            }
        });
    }

    private void loadServiceImage(SolutionReview solution, List<SolutionReview> solutions) {
        serviceViewModel.getServiceImage(solution.getId()).observe(getViewLifecycleOwner(), image -> {
            if (image != null) {
                setImage(image, solution, solutions.indexOf(solution));
            }
        });
    }
    private void setImage(Bitmap image, SolutionReview solution, int position) {
        if (image != null) {
            solution.setImage(image);
            if (position != -1) {
                reviewAdapter.notifyItemChanged(position);
            }
        }
    }

    private void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void showSuccessRating(String name) {
        Toast.makeText(requireContext(), getString(R.string.successfully_rated) + " " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}