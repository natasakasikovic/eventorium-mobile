package com.eventorium.presentation.solution.fragments.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.databinding.FragmentProductOverviewBinding;
import com.eventorium.presentation.solution.adapters.ManageableProductAdapter;
import com.eventorium.presentation.solution.listeners.OnManageListener;
import com.eventorium.presentation.solution.viewmodels.ManageableProductViewModel;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageProductFragment extends Fragment {

    private FragmentProductOverviewBinding binding;
    private ManageableProductViewModel viewModel;
    private ProductViewModel productViewModel;
    private ManageableProductAdapter adapter;
    private List<ProductSummary> products;
    private RecyclerView recyclerView;

    public ManageProductFragment() { }

    public static ManageProductFragment newInstance() {
        ManageProductFragment fragment = new ManageProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModels();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductOverviewBinding.inflate(inflater, container, false);
        setUpAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProducts();
        setUpListeners();
    }

    private void initializeViewModels() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(ManageableProductViewModel.class);
        productViewModel = provider.get(ProductViewModel.class);
    }

    private void setUpAdapter() {
        recyclerView = binding.productsRecycleView;
        adapter = new ManageableProductAdapter(new ArrayList<>(), new OnManageListener<ProductSummary>() {
            @Override
            public void onDeleteClick(ProductSummary item) {}

            @Override
            public void onSeeMoreClick(ProductSummary item) {
                navigateToProductDetails(item);
            }

            @Override
            public void onEditClick(ProductSummary item) {}
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadProducts() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                products = result.getData();
                adapter.setProducts(products);
                loadImages();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImages() {
        products.forEach(product -> productViewModel
            .getProductImage(product.getId()).observe(getViewLifecycleOwner(), img -> {
            if (img != null) {
                product.setImage(img);
                int position = products.indexOf(product);
                adapter.notifyItemChanged(position);
            }
        }));
    }

    private void navigateToProductDetails(ProductSummary product) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        navController.navigate(R.id.action_manage_product_to_product_details,
                ProductDetailsFragment.newInstance(product.getId()).getArguments());
    }

    private void setUpListeners() {
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // search listener
            @Override
            public boolean onQueryTextChange(String keyword) {
                viewModel.searchProducts(keyword).observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null) {
                        products = result.getData();
                        adapter.setData(products);
                        loadImages();
                    }
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
}