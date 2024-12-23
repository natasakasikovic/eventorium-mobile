package com.eventorium.presentation.solution.fragments.product;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.databinding.FragmentProductDetailsBinding;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.presentation.util.adapters.ImageAdapter;

import java.time.format.DateTimeFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private ProductViewModel productViewModel;

    public static final String ARG_ID = "ARG_PRODUCT_ID";


    public ProductDetailsFragment() {
    }

    public static ProductDetailsFragment newInstance(Long id) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        if(!productViewModel.isLoggedIn()) {
            binding.favButton.setVisibility(View.GONE);
        }
        assert getArguments() != null;
        // There is no list of products anywhere
        productViewModel.getProduct(1L).observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                binding.productName.setText(product.getName());
                binding.productPrice.setText(product.getPrice().toString());
                binding.productDescription.setText(product.getDescription());
                binding.productCategory.setText("Category: " + product.getCategory().getName());
                binding.productSpecialties.setText(product.getSpecialties());
                binding.rating.setText(product.getRating().toString());

                productViewModel.getServiceImages(product.getId()).observe(getViewLifecycleOwner(), images -> {
                    binding.images.setAdapter(new ImageAdapter(images));
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}