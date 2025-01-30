package com.eventorium.presentation.review.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentManageReviewBinding;
import com.eventorium.presentation.review.adapters.ReviewAdapter;
import com.eventorium.presentation.review.viewmodels.ReviewViewModel;

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
        adapter = new ReviewAdapter(new ArrayList<>());
        binding.reviewsRecycleView.setAdapter(adapter);

        loadReviews();

        return binding.getRoot();
    }

    private void loadReviews() {
        reviewViewModel.getPendingReviews().observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                adapter.setData(result.getData());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}