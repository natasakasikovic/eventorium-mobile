package com.eventorium.presentation.solution.fragments.pricelist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.solution.dtos.UpdatePriceListRequestDto;
import com.eventorium.databinding.FragmentServicePriceListBinding;
import com.eventorium.presentation.solution.adapters.PriceListItemAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.viewmodels.PriceListViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServicePriceListFragment extends Fragment {

    private FragmentServicePriceListBinding binding;
    private PriceListViewModel priceListViewModel;

    public ServicePriceListFragment() {
    }

    public static ServicePriceListFragment newInstance() {
        return new ServicePriceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        priceListViewModel = new ViewModelProvider(this).get(PriceListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServicePriceListBinding.inflate(inflater, container, false);
        priceListViewModel.getServices().observe(getViewLifecycleOwner(), services -> {
            binding.servicesRecycleView.setAdapter(new PriceListItemAdapter(services, service -> {

                if(service.getDiscount() > 100 || service.getDiscount() < 0) {
                    Toast.makeText(
                            getContext(),
                            "Discount should be between 0 and 100",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                priceListViewModel.updateService(
                        service.getId(),
                        new UpdatePriceListRequestDto(service.getPrice(), service.getDiscount())
                ).observe(getViewLifecycleOwner(), priceListItem -> {
                    if(priceListItem != null) {
                        Toast.makeText(
                                getContext(),
                                "Item has been updated successfully!",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        Toast.makeText(
                                getContext(),
                                "Failed to update item!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }));
        });
        return binding.getRoot();
    }
}