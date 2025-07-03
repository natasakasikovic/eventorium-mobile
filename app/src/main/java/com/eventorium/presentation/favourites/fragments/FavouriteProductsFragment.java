package com.eventorium.presentation.favourites.fragments;

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
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.databinding.FragmentFavouriteProductsBinding;
import com.eventorium.presentation.favourites.viewmodels.FavouritesViewModel;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteProductsFragment extends Fragment {

    private FragmentFavouriteProductsBinding binding;
    private FavouritesViewModel viewModel;
    private List<ProductSummary> products;
    private ProductsAdapter adapter;

    public FavouriteProductsFragment() { }

    public static FavouriteProductsFragment newInstance() {
        return new FavouriteProductsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFavouriteProducts();
    }

    private void loadFavouriteProducts() {
        viewModel.getFavouriteProducts().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                products = result.getData();
                setupAdapter();
                loadProductImages();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter() {
        ImageLoader loader = new ImageLoader(requireContext());
        adapter = new ProductsAdapter(
                loader,
                product -> () -> viewModel.getProductImage(product.getId()),
                product -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    Bundle args = new Bundle();
                    args.putLong(ARG_ID, product.getId());
                    navController.navigate(R.id.action_fav_to_product_details, args);
                });
        binding.productsRecycleView.setAdapter(adapter);
    }

    private void loadProductImages() {
        products.forEach(product -> viewModel.getProductImage(product.getId())
                .observe(getViewLifecycleOwner(), image -> {
                    if (image != null) {
                        product.setImage(image);
                        int position = products.indexOf(product);
                        if (position != -1) {
                            adapter.notifyItemChanged(position);
                        }
                    }
                })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}