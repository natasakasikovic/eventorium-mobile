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
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.databinding.FragmentProductDetailsBinding;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.presentation.util.adapters.ImageAdapter;
import com.google.android.material.button.MaterialButton;

import java.time.format.DateTimeFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private ProductViewModel productViewModel;

    public static final String ARG_ID = "ARG_PRODUCT_ID";
    private MaterialButton favouriteButton;
    private boolean isFavourite;


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
        assert getArguments() != null;
        favouriteButton = binding.favButton;
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

        productViewModel.isFavourite(getArguments().getLong(ARG_ID)).observe(getViewLifecycleOwner(), result -> {
            isFavourite = result;
            favouriteButton.setIconResource(
                    result
                            ? R.drawable.ic_favourite
                            : R.drawable.ic_not_favourite
            );
        });

        favouriteButton.setOnClickListener(v -> {
            Long id = getArguments().getLong(ARG_ID);
            if(isFavourite) {
                productViewModel.removeFavouriteProduct(id).observe(getViewLifecycleOwner(), result -> {
                    if(result) {
                        isFavourite = false;
                        favouriteButton.setIconResource(R.drawable.ic_not_favourite);
                        Toast.makeText(
                                requireContext(),
                                R.string.removed_service_from_favourites,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            } else {
                productViewModel.addFavouriteProduct(id).observe(getViewLifecycleOwner(), name -> {
                    if(name != null) {
                        isFavourite = true;
                        favouriteButton.setIconResource(R.drawable.ic_favourite);
                        Toast.makeText(
                                requireContext(),
                                getString(R.string.added_service)
                                        + name
                                        + getString(R.string.to_favourites),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
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