package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.event.fragments.BudgetPlanningFragment.ARG_EVENT_ID;
import static com.eventorium.presentation.event.fragments.BudgetPlanningFragment.ARG_EVENT_TYPE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.EventType;
import com.eventorium.databinding.FragmentBudgetItemsBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.shared.adapters.CategoryPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetItemsFragment extends Fragment implements BudgetCategoryFragment.OnRemoveCategoryListener {

    private FragmentBudgetItemsBinding binding;

    private CategoryViewModel categoryViewModel;
    private CategoryPagerAdapter adapter;
    private EventType eventType;
    private Long eventId;

    private final List<Category> plannedCategories = new ArrayList<>();
    private List<Category> otherCategories = new ArrayList<>();


    public BudgetItemsFragment() {
    }

    public static BudgetItemsFragment newInstance(EventType eventType, Long eventId) {
        BudgetItemsFragment fragment = new BudgetItemsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT_TYPE, eventType);
        args.putLong(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            eventId = getArguments().getLong(ARG_EVENT_ID);
            eventType = getArguments().getParcelable(ARG_EVENT_TYPE);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        categoryViewModel = provider.get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetItemsBinding.inflate(inflater, container, false);
        loadSuggestedCategories();
        loadOtherCategories();

        binding.btnAddCategory.setOnClickListener(v -> {
            Category category = (Category) binding.categorySelector.getSelectedItem();
            addCategory(category);
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) ->
                tab.setText(adapter.getFragmentTitle(position))
        ).attach();

        return binding.getRoot();
    }

    private void loadOtherCategories() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            //TODO: Add custom adapter, this adapter is useless like the whole android API
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categories.stream()
                            .filter(category -> !plannedCategories.contains(category))
                            .toArray(Category[]::new)
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySelector.setAdapter(adapter);
        });
    }

    private void addCategory(Category category) {
        adapter.addFragment(BudgetCategoryFragment.newInstance(category, plannedCategories.size()), category.getName());
        plannedCategories.add(category);
        otherCategories.remove(category);
    }

    private void loadSuggestedCategories() {
        adapter = new CategoryPagerAdapter(this);
        if(eventType != null) {
            eventType.getSuggestedCategories()
                    .forEach(category -> {
                        adapter.addFragment(
                                BudgetCategoryFragment.newInstance(category, plannedCategories.size()),
                                category.getName());
                        plannedCategories.add(category);
                    });
        }
        binding.viewPager.setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onRemoveCategory(int position, Category category) {
        adapter.removeFragment(position);

        plannedCategories.remove(category);
        otherCategories.add(category);
    }
}