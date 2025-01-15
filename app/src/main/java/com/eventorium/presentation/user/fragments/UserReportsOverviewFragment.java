package com.eventorium.presentation.user.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentUserReportsOverviewBinding;
import com.eventorium.presentation.user.UserReportAdapter;
import com.eventorium.presentation.user.viewmodels.UserReportViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserReportsOverviewFragment extends Fragment {

    private FragmentUserReportsOverviewBinding binding;
    private UserReportAdapter adapter;
    private UserReportViewModel viewModel;
    private static final String ARG = "report";


    public UserReportsOverviewFragment() { }

    public static UserReportsOverviewFragment newInstance() {
        return new UserReportsOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserReportsOverviewBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(UserReportViewModel.class);
        configureAdapter();
        binding.recycleView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getReports().observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null)
                adapter.setData(result.getData());
        });
    }

    private void configureAdapter(){
        adapter = new UserReportAdapter(new ArrayList<>(), report -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putParcelable(ARG, report);
            navController.navigate(R.id.action_reports_overview_to_details, args);
        });
    }
}