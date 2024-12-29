package com.eventorium.presentation.event.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.databinding.FragmentBudgetCategoryBinding;
import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.util.adapters.CategoryPagerAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetCategoryFragment extends Fragment {


    public interface OnRemoveCategoryListener {
        void onRemoveCategory(int position, Category category);
    }

    private FragmentBudgetCategoryBinding binding;
    private BudgetViewModel budgetViewModel;
    private OnRemoveCategoryListener onRemoveCategoryListener;

    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    private static final String ARG_POSITION = "position";
    private Category category;
    private int position;

    public BudgetCategoryFragment() {
    }

    public static BudgetCategoryFragment newInstance(Category category, int position) {
        BudgetCategoryFragment fragment = new BudgetCategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
        args.putInt(ARG_POSITION, position);
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
        return binding.getRoot();
    }

    private void search() {
        Double price = Double.parseDouble(String.valueOf(binding.plannedAmount.getText()));
        if(binding.productChecked.isChecked()) {
            searchProducts(category.getId(), price);
        } else {
            searchServices(category.getId(), price);
        }
    }

    private void searchProducts(Long id, Double price) {
        budgetViewModel.getSuggestedProducts(id, price).observe(getViewLifecycleOwner(), products -> {
            ProductsAdapter adapter = new ProductsAdapter(products);
            binding.itemsRecycleView.setAdapter(adapter);
        });
    }

    private void searchServices(Long id, Double price) {
        budgetViewModel.getSuggestedServices(id, price).observe(getViewLifecycleOwner(), services -> {
            ServicesAdapter adapter = new ServicesAdapter(services);
            binding.itemsRecycleView.setAdapter(adapter);
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}