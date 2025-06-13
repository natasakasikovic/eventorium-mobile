package com.eventorium.presentation.event.fragments.budget;

import android.content.Context;
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
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.databinding.FragmentBudgetCategoryBinding;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetCategoryFragment extends Fragment {

    public interface OnRemoveCategoryListener {
        void onRemoveCategory(int position, Category category);
    }

    private FragmentBudgetCategoryBinding binding;
    private BudgetViewModel budgetViewModel;
    private ProductViewModel productViewModel;
    private ServiceViewModel serviceViewModel;
    private OnRemoveCategoryListener onRemoveCategoryListener;
    private ProductsAdapter productsAdapter;
    private ServicesAdapter servicesAdapter;

    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    private static final String ARG_POSITION = "ARG_POS";
    public static final String ARG_EVENT = "ARG_EVENT";
    private Category category;
    private Event event;
    private int position;


    public BudgetCategoryFragment() {
    }

    public static BudgetCategoryFragment newInstance(Event event, Category category, int position) {
        BudgetCategoryFragment fragment = new BudgetCategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
        args.putInt(ARG_POSITION, position);
        args.putParcelable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnRemoveCategoryListener) {
            onRemoveCategoryListener = (OnRemoveCategoryListener) getParentFragment();
        } else {
            assert getParentFragment() != null;
            throw new ClassCastException(getParentFragment()+ " must implement OnRemoveCategoryListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            category = getArguments().getParcelable(ARG_CATEGORY);
            position = getArguments().getInt(ARG_POSITION);
            event = getArguments().getParcelable(ARG_EVENT);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        budgetViewModel = provider.get(BudgetViewModel.class);
        productViewModel = provider.get(ProductViewModel.class);
        serviceViewModel = provider.get(ServiceViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetCategoryBinding.inflate(inflater, container, false);
        binding.productChecked.setChecked(true);
        binding.deleteButton.setOnClickListener(v -> {
            if (onRemoveCategoryListener != null) {
                onRemoveCategoryListener.onRemoveCategory(position, category);
            }
        });
        binding.searchItems.setOnClickListener(v -> search());

        productsAdapter = new ProductsAdapter(new ArrayList<>(), this::navigateToProductDetails);
        servicesAdapter = new ServicesAdapter(new ArrayList<>(), this::navigateToServiceDetails);

        return binding.getRoot();
    }

    private void navigateToServiceDetails(ServiceSummary serviceSummary) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(ProductDetailsFragment.ARG_ID, serviceSummary.getId());
        args.putDouble(ProductDetailsFragment.ARG_PLANNED_AMOUNT, Double.parseDouble(String.valueOf(binding.plannedAmount.getText())));
        args.putParcelable(ProductDetailsFragment.ARG_EVENT, event);
        navController.navigate(R.id.action_budget_to_serviceDetails, args);
    }

    private void navigateToProductDetails(ProductSummary productSummary) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(ProductDetailsFragment.ARG_ID, productSummary.getId());
        args.putDouble(ProductDetailsFragment.ARG_PLANNED_AMOUNT, Double.parseDouble(String.valueOf(binding.plannedAmount.getText())));
        args.putParcelable(ProductDetailsFragment.ARG_EVENT, event);
        navController.navigate(R.id.action_budget_to_productDetails, args);
    }


    private void search() {
        if(Objects.requireNonNull(binding.plannedAmount.getText()).toString().isEmpty()) {
            return;
        }
        Double price = Double.parseDouble(String.valueOf(binding.plannedAmount.getText()));
    }

    private void loadProductImages(List<ProductSummary> products) {
        products.forEach( product -> productViewModel.getProductImage(product.getId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null){
                        product.setImage(image);
                        int position = products.indexOf(product);
                        if (position != -1) {
                            productsAdapter.notifyItemChanged(position);
                        }
                    }
                })
        );
    }

    private void loadServiceImages(List<ServiceSummary> services) {
        services.forEach(service -> serviceViewModel.getServiceImage(service.getId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null){
                        service.setImage(image);
                        int position = services.indexOf(service);
                        if (position != -1) {
                            servicesAdapter.notifyItemChanged(position);
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