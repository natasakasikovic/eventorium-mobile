package com.eventorium.presentation.user.fragments;

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
import com.eventorium.data.auth.models.UpdateReportStatusRequest;
import com.eventorium.data.auth.models.UserReportResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.models.Status;
import com.eventorium.databinding.FragmentUserReportDetailsBinding;
import com.eventorium.presentation.user.viewmodels.UserReportViewModel;

import java.text.MessageFormat;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserReportDetailsFragment extends Fragment {

    private FragmentUserReportDetailsBinding binding;
    private UserReportViewModel viewModel;
    private UserReportResponse report;
    private static final String ARG = "report";


    public UserReportDetailsFragment() { }

    public static UserReportDetailsFragment newInstance() {
        return new UserReportDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            report = getArguments().getParcelable(ARG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserReportDetailsBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(UserReportViewModel.class);
        bindData();
        setUpListeners();

        return binding.getRoot();
    }


    private void bindData(){
        binding.reporter.setText(MessageFormat.format("Reporter: {0}", report.getReporter()));
        binding.reason.setText(MessageFormat.format("Reason: {0}", report.getReason()));
        binding.offender.setText(MessageFormat.format("Offender: {0}", report.getOffender()));
    }

    private void setUpListeners() {
        binding.acceptButton.setOnClickListener(v -> viewModel.updateStatus(report.getId(), new UpdateReportStatusRequest(Status.ACCEPTED))
                .observe(getViewLifecycleOwner(), this::handleResult));
        binding.declineButton.setOnClickListener(v -> viewModel.updateStatus(report.getId(), new UpdateReportStatusRequest(Status.DECLINED))
                .observe(getViewLifecycleOwner(), this::handleResult));
    }

    private void handleResult(Result<Void> result) {
        if (result.getError() != null)
            Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show();
            navigateBack();
        }
    }

    private void navigateBack(){
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }
}