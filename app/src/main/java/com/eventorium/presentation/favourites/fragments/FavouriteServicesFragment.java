package com.eventorium.presentation.favourites.fragments;

import static com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment.ARG_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.databinding.FragmentFavouriteServicesBinding;
import com.eventorium.presentation.favourites.viewmodels.FavouritesViewModel;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.shared.utils.PagedListUtils;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteServicesFragment extends Fragment {

    private FragmentFavouriteServicesBinding binding;
    private FavouritesViewModel viewModel;
    private List<ServiceSummary> services;
    private ServicesAdapter adapter;

    public FavouriteServicesFragment() { }

    public static FavouriteServicesFragment newInstance() {
        return new FavouriteServicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteServicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFavouriteServices();
    }

    private void loadFavouriteServices() {
        viewModel.getFavouriteServices().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                services = result.getData();
                setupAdapter();

                PagedList<ServiceSummary> pagedList = PagedListUtils.fromList(services);
                adapter.submitList(pagedList);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter() {
        ImageLoader loader = new ImageLoader();
        adapter = new ServicesAdapter(
                loader,
                service -> () -> viewModel.getServiceImage(service.getId()),
                service -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    Bundle args = new Bundle();
                    args.putLong(ARG_ID, service.getId());
                    navController.navigate(R.id.action_fav_to_service_details, args);
                });
        binding.servicesRecycleView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}