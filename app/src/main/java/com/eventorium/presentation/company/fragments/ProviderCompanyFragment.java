package com.eventorium.presentation.company.fragments;

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
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.company.models.Company;
import com.eventorium.databinding.FragmentProviderCompanyBinding;
import com.eventorium.presentation.company.viewmodels.CompanyViewModel;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.adapters.ImageAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProviderCompanyFragment extends Fragment {
    private FragmentProviderCompanyBinding binding;
    private CompanyViewModel viewModel;

    public ProviderCompanyFragment() { }

    public static ProviderCompanyFragment newInstance() {
        return new ProviderCompanyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProviderCompanyBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        loadCompany();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editCompanyButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_companyDetails_to_editCompanyFragment);
        });

    }

    private void loadCompany() {
        viewModel.getCompany().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                Company company = result.getData();
                binding.name.setText(company.getName());
                binding.email.setText(company.getEmail());
                String workingHours = company.getOpeningHours() + " - " + company.getClosingHours();
                binding.workingHours.setText(workingHours);
                binding.description.post(() -> binding.description.setText(company.getDescription()));
                binding.description.requestLayout();
                binding.description.invalidate();
                String address = company.getAddress() + " " + company.getCity().getName();
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