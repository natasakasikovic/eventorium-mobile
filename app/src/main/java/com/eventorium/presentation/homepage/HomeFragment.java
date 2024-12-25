package com.eventorium.presentation.homepage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.util.models.Status;
import com.eventorium.databinding.FragmentHomeBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomepageViewModel viewModel;

    private static List<ProductSummary> productSummaries = new ArrayList<>();
    private static List<ServiceSummary> serviceSummaries = new ArrayList<>();

    public HomeFragment() { }

    public static HomeFragment newInstance() { return new HomeFragment(); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomepageViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareProductData();

        attachSnapHelpers();

        viewModel.getTopEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null){
                binding.eventsRecycleView.setAdapter(new EventsAdapter(result.getData()));
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
            }
        });

        binding.productsRecycleView.setAdapter(new ProductsAdapter(productSummaries));
        binding.servicesRecycleView.setAdapter(new ServicesAdapter(serviceSummaries));


        setUpListeners();
    }

    public void attachSnapHelpers() {
        SnapHelper snapHelperEvents = new LinearSnapHelper();
        snapHelperEvents.attachToRecyclerView(binding.eventsRecycleView);

        SnapHelper snapHelperProducts = new LinearSnapHelper();
        snapHelperProducts.attachToRecyclerView(binding.productsRecycleView);

        SnapHelper snapHelperServices = new LinearSnapHelper();
        snapHelperServices.attachToRecyclerView(binding.servicesRecycleView);
    }

    private void setUpListeners(){
        binding.arrowButtonEvents.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homepage_to_events_overview);
        });
        binding.arrowButtonServices.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homepage_to_services_overview);
        });
        binding.arrowButtonProducts.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homepage_to_products_overview);
        });
    }

    private void prepareProductData() {
        productSummaries.clear();
        productSummaries.add(new ProductSummary(1L, "Smartphone X", 799.99, 15.0, true, true, 4.5, Status.ACCEPTED));
        productSummaries.add(new ProductSummary(2L, "Laptop Pro 15", 1499.99, 10.0, false, true, 4.7, Status.ACCEPTED));
        productSummaries.add(new ProductSummary(3L, "Bluetooth Headphones", 120.00, 5.0, true, true, 4.2, Status.ACCEPTED));
        productSummaries.add(new ProductSummary(4L, "Smartwatch 2024", 250.00, 20.0, true, false, 4.8, Status.ACCEPTED));
        productSummaries.add(new ProductSummary(5L, "Electric Scooter", 499.99, 25.0, true, true, 3.9, Status.ACCEPTED));
    }
}