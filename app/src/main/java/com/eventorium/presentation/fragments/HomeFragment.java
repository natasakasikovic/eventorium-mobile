package com.eventorium.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.models.Event;
import com.eventorium.data.models.Product;
import com.eventorium.data.models.Service;
import com.eventorium.databinding.FragmentHomeBinding;
import com.eventorium.presentation.adapters.EventsAdapter;
import com.eventorium.presentation.adapters.ProductsAdapter;
import com.eventorium.presentation.adapters.service.ServiceAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static List<Event> events = new ArrayList<>();
    private static List<Product> products = new ArrayList<>();
    private static List<Service> services = new ArrayList<>();


    public HomeFragment() { }

    public static HomeFragment newInstance() { return new HomeFragment(); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareEventData();
        prepareProductData();
        prepareServiceData();

        attachSnapHelpers();

        binding.eventsRecycleView.setAdapter(new EventsAdapter(events));
        binding.productsRecycleView.setAdapter(new ProductsAdapter(products));
        binding.servicesRecycleView.setAdapter(new ServiceAdapter(services));

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

        binding.arrowButtonProducts.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homepage_to_products_overview);
        });

    }

    public void prepareEventData() {
        events.clear();
        events.add(new Event("Concert",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Conference",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Workshop",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Festival",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Webinar", "Novi Sad", R.drawable.conference));
    }

    private void prepareProductData() {
        products.clear();
        products.add(new Product("Balloon Bouquet", 500.0, R.drawable.baloons));
        products.add(new Product("Confetti Cannon", 200.0, R.drawable.conference));
        products.add(new Product("LED String Lights", 100.0, R.drawable.conference));
        products.add(new Product("Photo Booth Props", 20.0, R.drawable.baloons));
        products.add(new Product("Custom Banner", 25.0, R.drawable.conference));
    }

    private void prepareServiceData(){
        services.clear();
        services.add(new Service("Catering", 500.0, R.drawable.catering));
        services.add(new Service("Photography", 300.0,  R.drawable.catering));
        services.add(new Service("Venue Setup", 200.0,  R.drawable.catering));
        services.add(new Service("DJ Service", 250.0,  R.drawable.catering));
        services.add(new Service("Florist", 150.0,  R.drawable.catering));
    }
}