package com.eventorium.presentation.homepage;

import static com.eventorium.presentation.event.fragments.EventDetailsFragment.ARG_EVENT_ID;
import static com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment.ARG_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.eventorium.R;
import com.eventorium.data.shared.models.Result;
import com.eventorium.databinding.FragmentHomeBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.shared.utils.PagedListUtils;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;

import java.util.ArrayList;
import java.util.function.Consumer;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomepageViewModel viewModel;
    private ServicesAdapter serviceAdapter;
    private ProductsAdapter productsAdapter;
    private EventsAdapter eventsAdapter;

    public HomeFragment() { }

    public static HomeFragment newInstance() { return new HomeFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomepageViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        configureServiceAdapter();
        configureProductAdapter();
        configureEventAdapter();

        binding.servicesRecycleView.setAdapter(serviceAdapter);
        binding.productsRecycleView.setAdapter(productsAdapter);
        binding.eventsRecycleView.setAdapter(eventsAdapter);

        return binding.getRoot();
    }

    private void configureProductAdapter() {
        ImageLoader loader = new ImageLoader(requireContext());
        productsAdapter = new ProductsAdapter(
                loader,
                product -> () -> viewModel.getProductImage(product.getId()),
                product -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ProductDetailsFragment.ARG_ID, product.getId());
            navController.navigate(R.id.action_home_to_product_details, args);
        });
    }

    private void configureServiceAdapter() {
        ImageLoader imageLoader = new ImageLoader(requireContext());
        serviceAdapter = new ServicesAdapter(
                imageLoader,
                service -> () -> viewModel.getServiceImage(service.getId()),
                service -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    Bundle args = new Bundle();
                    args.putLong(ARG_ID, service.getId());
                    navController.navigate(R.id.action_home_to_service_details, args);
                });
    }

    private void configureEventAdapter() {
        ImageLoader loader = new ImageLoader(requireContext());
        eventsAdapter = new EventsAdapter(
                loader,
                event -> () -> viewModel.getEventImage(event.getImageId()),
                event -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    Bundle args = new Bundle();
                    args.putLong(ARG_EVENT_ID, event.getId());
                    navController.navigate(R.id.action_home_to_event_details, args);
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
                data -> eventsAdapter.submitList(PagedListUtils.fromList(data))
        ));
    }

    private void observeTopProducts() {
        viewModel.getTopProducts().observe(getViewLifecycleOwner(), result -> handleResult(
                result,
                data -> productsAdapter.submitList(PagedListUtils.fromList(data))
        ));
    }

    private void observeTopServices() {
        viewModel.getTopServices().observe(getViewLifecycleOwner(), result -> handleResult(
                result,
                data -> serviceAdapter.submitList(PagedListUtils.fromList(data))
        ));
    }

    private <T> void handleResult(Result<T> result, Consumer<T> onSuccess) {
        if (result.getError() == null && result.getData() != null) {
            onSuccess.accept(result.getData());
        } else {
            String message = result.getError() != null ? result.getError() : "Loading..";
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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