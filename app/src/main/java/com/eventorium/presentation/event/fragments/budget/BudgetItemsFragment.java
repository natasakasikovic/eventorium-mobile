package com.eventorium.presentation.event.fragments.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.databinding.FragmentBudgetItemsBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.shared.adapters.CategoryPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetItemsFragment extends Fragment implements BudgetCategoryFragment.CategoryChangeListener {

    private FragmentBudgetItemsBinding binding;

    private CategoryViewModel categoryViewModel;
    private BudgetViewModel budgetViewModel;
    private CategoryPagerAdapter pagerAdapter;
    private ArrayAdapter<Category> categoryAdapter;

    private Event event;

    private List<Category> activeCategories = new ArrayList<>();
    private final List<Category> allCategories = new ArrayList<>();

    public static String ARG_EVENT = "ARG_EVENT";

    public BudgetItemsFragment() {
    }

    public static BudgetItemsFragment newInstance(Event event) {
        BudgetItemsFragment fragment = new BudgetItemsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            event = getArguments().getParcelable(ARG_EVENT);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        categoryViewModel = provider.get(CategoryViewModel.class);
        budgetViewModel = provider.get(BudgetViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetItemsBinding.inflate(inflater, container, false);
        configureCategoryAdapter();
        loadSuggestedCategories();
        loadAllCategories();
        restoreBudget();

        binding.btnAddCategory.setOnClickListener(v -> {
            Category category = (Category) binding.categorySelector.getSelectedItem();
            addCategory(category);
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) ->
                tab.setText(pagerAdapter.getFragmentTitle(position))
        ).attach();

        return binding.getRoot();
    }

    private void configureCategoryAdapter() {
        categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySelector.setAdapter(categoryAdapter);
    }

    private void loadAllCategories() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.addAll(categories);
        });
    }

    private void restoreBudget() {
        budgetViewModel.getBudget(event.getId()).observe(getViewLifecycleOwner(), result -> {
            List<Category> categories = result.getData().getActiveCategories();
            if(result.getError() == null && !categories.isEmpty()) {
                for(Category category : categories)
                    pagerAdapter.addFragment(
                            BudgetCategoryFragment.newInstance(event, category , activeCategories.size()),
                            category.getName());
            }
        });

    }

    private void addCategory(Category category) {
        if(activeCategories.contains(category))
            return;

        pagerAdapter.addFragment(BudgetCategoryFragment.newInstance(event, category, activeCategories.size()), category.getName());
        activeCategories.add(category);
    }

    private void loadSuggestedCategories() {
        pagerAdapter = new CategoryPagerAdapter(this);
        if(event.getType() != null) {
            event.getType().getSuggestedCategories()
                    .forEach(category -> {
                        pagerAdapter.addFragment(
                                BudgetCategoryFragment.newInstance(event, category , activeCategories.size()),
                                category.getName());
                        activeCategories.add(category);
                    });
        }
        binding.viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onRemoveCategory(int position, Category category) {
        pagerAdapter.removeFragment(position);
        activeCategories.remove(category);
    }

    @Override
    public void saveCategories() {
        budgetViewModel
                .updateActiveCategories(event.getId(), activeCategories)
                .observe(getViewLifecycleOwner(), _ignored -> {});
    }
}