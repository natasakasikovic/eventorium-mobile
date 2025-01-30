package com.eventorium.presentation.review.fragments;

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
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.interaction.models.review.SolutionType;
import com.eventorium.data.interaction.models.review.UpdateReview;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.models.Status;
import com.eventorium.databinding.FragmentManageReviewBinding;
import com.eventorium.presentation.company.fragments.CompanyDetailsFragment;
import com.eventorium.presentation.review.adapters.ReviewAdapter;
import com.eventorium.presentation.review.listeners.OnReviewListener;
import com.eventorium.presentation.review.viewmodels.ReviewViewModel;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;
import com.eventorium.presentation.user.fragments.UserProfileFragment;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageReviewFragment extends Fragment {

    private FragmentManageReviewBinding binding;
    private ReviewViewModel reviewViewModel;
    private ReviewAdapter adapter;

    public ManageReviewFragment() {
    }
    public static ManageReviewFragment newInstance() {
        return new ManageReviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding  = FragmentManageReviewBinding.inflate(inflater, container, false);
        adapter = new ReviewAdapter(new ArrayList<>(), configureAdapter());
        binding.reviewsRecycleView.setAdapter(adapter);

        loadReviews();
        return binding.getRoot();
    }


    private OnReviewListener configureAdapter() {
        return new OnReviewListener() {
            @Override
            public void navigateToProvider(UserDetails provider) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(UserProfileFragment.ARG_ID, provider.getId());
                navController.navigate(R.id.action_manageReviews_to_userProfile, args);
            }

            @Override
            public void navigateToSolution(SolutionReview solution) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                if(solution.getSolutionType() == SolutionType.PRODUCT) {
                    args.putLong(ProductDetailsFragment.ARG_ID, solution.getId());
                    navController.navigate(R.id.action_manageReviews_to_productDetails, args);
                } else {
                    args.putLong(ServiceDetailsFragment.ARG_ID, solution.getId());
                    navController.navigate(R.id.action_manageReviews_to_serviceDetails, args);
                }
            }

            @Override
            public void acceptReview(Long id) {
                reviewViewModel.updateReview(id, Status.ACCEPTED)
                        .observe(getViewLifecycleOwner(), result -> handleUpdateResult(id, result));
            }

            @Override
            public void declineReview(Long id) {
                reviewViewModel.updateReview(id, Status.DECLINED)
                        .observe(getViewLifecycleOwner(), result -> handleUpdateResult(id, result));
            }

            private void handleUpdateResult(Long id, Result<Void> result) {
                if(result.getError() == null) {
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.successfully_updated_review),
                            Toast.LENGTH_SHORT
                    ).show();
                    reviewViewModel.removeReview(id);
                } else {
                    Toast.makeText(
                            requireContext(),
                            result.getError(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        };
    }

    private void loadReviews() {
        reviewViewModel.getPendingReviews();
        reviewViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            adapter.setData(reviews);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}