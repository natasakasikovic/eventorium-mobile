package com.eventorium.presentation.solution.fragments.product;

import static com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment.ARG_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.databinding.FragmentProductOverviewBinding;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductOverviewBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        configureAdapter();
        binding.productsRecycleView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeProducts();
        setUpListener();
    }

    private void configureAdapter(){
        adapter = new ProductsAdapter(new ArrayList<>(), product  -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ARG_ID, product.getId());
            navController.navigate(R.id.action_productOverview_to_productDetails, args);
        });
    }

    private void observeProducts(){
        viewModel.getProducts().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                adapter.setData(result.getData());
                loadProductImages(result.getData());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpListener(){
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String keyword) {
                viewModel.searchProducts(keyword).observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null)
                        adapter.setData(result.getData());
                    else
                        Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
                });
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void loadProductImages(List<ProductSummary> products) {
        products.forEach( product -> viewModel.getProductImage(product.getId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null){
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