package com.eventorium.presentation.company.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.data.company.models.CompanyDetails;
import com.eventorium.databinding.FragmentCompanyDetailsBinding;
import com.eventorium.presentation.company.viewmodels.CompanyViewModel;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.shared.models.ImageItem;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CompanyDetailsFragment extends Fragment {

    public static String ARG_COMPANY_ID = "company_id";
    private FragmentCompanyDetailsBinding binding;
    private CompanyViewModel viewModel;
    private Long id;

    public CompanyDetailsFragment() { }

    public static CompanyDetailsFragment newInstance() {
        return new CompanyDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
        id = getArguments().getLong(ARG_COMPANY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompanyDetailsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        loadCompany();
        return binding.getRoot();
    }

    private void loadCompany() {
        viewModel.getCompany(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                CompanyDetails company = result.getData();
                binding.name.setText(company.getName());
                binding.email.setText(company.getEmail());
                String workingHours = company.getOpeningHours() + " - " + company.getClosingHours();
                binding.workingHours.setText(workingHours);
                binding.description.post(() -> binding.description.setText(company.getDescription()));
                binding.description.requestLayout();
                binding.description.invalidate();
                String address = company.getAddress() + " " + company.getCity();
                binding.address.setText(address);
                binding.phoneNumber.setText(company.getPhoneNumber());
                loadImages(company.getId());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImages(Long id) {
        viewModel.getImages(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                List<ImageItem> images = result.getData();
                if (images.isEmpty()) binding.noImages.setVisibility(View.VISIBLE);
                else binding.images.setAdapter(new ImageAdapter(images));
                binding.loader.setVisibility(View.GONE);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}