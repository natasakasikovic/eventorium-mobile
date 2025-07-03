package com.eventorium.presentation.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.auth.models.UserReportRequest;
import com.eventorium.databinding.FragmentUserReportBinding;
import com.eventorium.presentation.user.viewmodels.UserReportViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class UserReportFragment extends Fragment {

    private static final String ARG_ID = "ARG_USER_ID";
    private FragmentUserReportBinding binding;
    private UserReportViewModel viewModel;
    private  Long offenderId;

    public UserReportFragment() { }

    public static UserReportFragment newInstance() {
        return new UserReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            offenderId = getArguments().getLong(ARG_ID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserReportBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UserReportViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpListener();
    }

    public void setUpListener() {
        binding.reportUserButton.setOnClickListener(v -> {
            String reportReason = binding.reportReasonInput.getText().toString();

            if (isReportReasonValid(reportReason))
                submitReport(reportReason);
            else
                Toast.makeText(requireContext(), R.string.error_empty_report_reason, Toast.LENGTH_SHORT).show();
        });
    }

    private void submitReport(String reportReason) {
        UserReportRequest report = new UserReportRequest(reportReason);
        viewModel.reportUser(offenderId, report).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() != null) {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), R.string.report_successfully_submitted, Toast.LENGTH_SHORT).show();
                navigateBack();
            }
        });
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    private boolean isReportReasonValid(String reportReason) {
        return !reportReason.trim().isEmpty();
    }
}