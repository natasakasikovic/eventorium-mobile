package com.eventorium.presentation.homepage;

import static com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment.ARG_ID;

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
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.models.Status;
import com.eventorium.databinding.FragmentHomeBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomepageViewModel viewModel;
    private ServicesAdapter serviceAdapter;
    private ProductsAdapter productsAdapter;

    public HomeFragment() { }

    public static HomeFragment newInstance() { return new HomeFragment(); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomepageViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        configureServiceAdapter();
        configureProductAdapter();

        binding.servicesRecycleView.setAdapter(serviceAdapter);
        binding.productsRecycleView.setAdapter(productsAdapter);

        return binding.getRoot();
    }

    private void configureProductAdapter() {
        productsAdapter = new ProductsAdapter( new ArrayList<>(), product -> {
            NavController navController = Navigation.findNavController( requireActivity(), R.id.fragment_nav_content_main );
            Bundle args = new Bundle();
            args.putLong(ProductDetailsFragment.ARG_ID, product.getId());
            navController.navigate(R.id.action_home_to_product_details, args);
        });
    }

    private void configureServiceAdapter() {
        serviceAdapter = new ServicesAdapter( new ArrayList<>(), service -> {
            NavController navController = Navigation.findNavController( requireActivity(), R.id.fragment_nav_content_main );
            Bundle args = new Bundle();
            args.putLong(ARG_ID, service.getId());
            navController.navigate(R.id.action_home_to_service_details, args);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeTopEvents();
        observeTopProducts();
        observeTopServices();

        attachSnapHelpers();
        setUpListeners();
    }

    private void observeTopEvents() {
        viewModel.getTopEvents().observe(getViewLifecycleOwner(), result -> handleResult(
                result,
                data -> binding.eventsRecycleView.setAdapter(new EventsAdapter(data)) // TODO: change
        ));
    }

    private void observeTopProducts() {
        viewModel.getTopProducts().observe(getViewLifecycleOwner(), result -> handleResult(
                result,
                data -> { productsAdapter.setData(data);
                          loadProductImages(data); }
        ));
    }

    private void loadProductImages(List<ProductSummary> products) {
        products.forEach( product -> viewModel.getProductImage(product.getId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null){
                        product.setImage(image);
                        int position = products.indexOf(product);
                        if (position != -1) {
                            productsAdapter.notifyItemChanged(position);
                        }
                    }
                }));
    }

    private void observeTopServices() {
        viewModel.getTopServices().observe(getViewLifecycleOwner(), result -> handleResult(
                result,
                data -> {serviceAdapter.setData(data);
                         loadServiceImages(data);
                }
        ));
    }

    private void loadServiceImages(List<ServiceSummary> services){
        services.forEach( service -> viewModel.getServiceImage(service.getId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null){
                        service.setImage(image);
                        int position = services.indexOf(service);
                        if (position != -1) {
                            serviceAdapter.notifyItemChanged(position);
                        }
                    }
                }));
    }

    private <T> void handleResult(Result<T> result, Consumer<T> onSuccess) {
        if (result.getError() == null) {
            onSuccess.accept(result.getData());
        } else {
            Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
        }
    }

    private void attachSnapHelpers() {
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

}