package com.eventorium.presentation.review.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

            }

            @Override
            public void onCommentClick(SolutionReview review) {

            }

            @Override
            public void onRateClick(SolutionReview review) {

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

    private void loadImage(SolutionReview solution, List<SolutionReview> solutions) {
        if(solution.getType() == ReviewType.PRODUCT) {
            productViewModel.getProductImage(solution.getId()).observe(getViewLifecycleOwner(), image -> {
                if (image != null) {
                    setImage(image, solution, solutions.indexOf(solution));
                }
            });
        } else {
            serviceViewModel.getServiceImage(solution.getId()).observe(getViewLifecycleOwner(), image -> {
                if (image != null) {
                    setImage(image, solution, solutions.indexOf(solution));
                }
            });
        }
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
        Toast.makeText(
                requireContext(),
                error,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}