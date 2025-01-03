package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment.ARG_ID;

import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.Budget;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.databinding.FragmentBudgetCategoryBinding;
import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.util.adapters.CategoryPagerAdapter;
import com.eventorium.presentation.util.listeners.OnPurchaseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetCategoryFragment extends Fragment {


    public interface OnRemoveCategoryListener {
        void onRemoveCategory(int position, Category category);
        void onPurchasedRemoveCategory(int position, Category category);
    }

    private FragmentBudgetCategoryBinding binding;
    private BudgetViewModel budgetViewModel;
    private OnRemoveCategoryListener onRemoveCategoryListener;
    private ProductsAdapter productsAdapter;

    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    public static final String ARG_ID = "ARG_EVENT_ID";
    private static final String ARG_POSITION = "position";
    private Category category;
    private int position;
    private Long eventId;

    public BudgetCategoryFragment() {
    }

    public static BudgetCategoryFragment newInstance(Category category, Long eventId, int position) {
        BudgetCategoryFragment fragment = new BudgetCategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
        args.putInt(ARG_POSITION, position);
        args.putLong(ARG_ID, eventId);
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
            eventId = getArguments().getLong(ARG_ID);
        }
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
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

        productsAdapter = new ProductsAdapter(new ArrayList<>(), configureListener());

        return binding.getRoot();
    }


    private BudgetItem getBudgetItem(Double plannedAmount, ProductSummary item) {
        return BudgetItem.builder()
                .category(category)
                .itemId(item.getId())
                .plannedAmount(plannedAmount)
                .build();
    }

    private OnPurchaseListener<ProductSummary> configureListener() {
        return new OnPurchaseListener<>() {
            @Override
            public void purchase(ProductSummary item) {
                Double plannedAmount = Double.parseDouble(String.valueOf(binding.plannedAmount.getText()));
                budgetViewModel.purchaseProduct(
                        eventId,
                        getBudgetItem(plannedAmount, item)
                ).observe(getViewLifecycleOwner(), product -> {
                    if(product.getError() == null) {
                        productsAdapter.remove(item);
                        if (onRemoveCategoryListener != null) {
                            onRemoveCategoryListener.onPurchasedRemoveCategory(position, category);
                        }
                    } else {
                        Toast.makeText(
                                requireContext(),
                                product.getError(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }

            @Override
            public void navigateToDetails(ProductSummary item) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(ARG_ID, item.getId());
                navController.navigate(R.id.action_budget_to_productDetails, args);
            }
        };
    }

    private void search() {
        if(Objects.requireNonNull(binding.plannedAmount.getText()).toString().isEmpty()) {
            return;
        }
        Double price = Double.parseDouble(String.valueOf(binding.plannedAmount.getText()));
        if(binding.productChecked.isChecked()) {
            binding.itemsRecycleView.setAdapter(productsAdapter);
            searchProducts(category.getId(), price);
        } else {
            searchServices(category.getId(), price);
        }
    }

    private void searchProducts(Long id, Double price) {
        budgetViewModel.getSuggestedProducts(id, price).observe(getViewLifecycleOwner(), products -> {
            requireActivity().runOnUiThread(() -> {
                productsAdapter.setData(products);
            });
        });
    }

    private void searchServices(Long id, Double price) {
        budgetViewModel.getSuggestedServices(id, price).observe(getViewLifecycleOwner(), services -> {
            ServicesAdapter adapter = new ServicesAdapter(services, service -> {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(ARG_ID, service.getId());
                navController.navigate(R.id.action_budget_to_serviceDetails, args);
            });
            binding.itemsRecycleView.setAdapter(adapter);
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}