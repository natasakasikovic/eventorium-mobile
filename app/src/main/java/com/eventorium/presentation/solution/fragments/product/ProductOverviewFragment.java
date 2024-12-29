package com.eventorium.presentation.solution.fragments.product;

import static com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment.ARG_ID;

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
import com.eventorium.databinding.FragmentProductOverviewBinding;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductOverviewFragment extends Fragment {

    private FragmentProductOverviewBinding binding;
    private ProductViewModel viewModel;
    private ProductsAdapter adapter;

    public ProductOverviewFragment() { }

    public static ProductOverviewFragment newInstance() {
        return new ProductOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductOverviewBinding.inflate(inflater, container, false);
        configureAdapter();
        return binding.getRoot();
    }

    private void configureAdapter() {
        adapter = new ProductsAdapter(new ArrayList<>(), product  -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ARG_ID, product.getId());
            navController.navigate(R.id.action_productOverview_to_productDetails, args);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if(products.getError() == null) {
                adapter.setData(products.getData());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}