package com.eventorium.presentation.event.fragments.budget;

import static java.util.stream.Collectors.toList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.Budget;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.models.Privacy;
import com.eventorium.databinding.FragmentBudgetItemsBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.shared.adapters.CategoryPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetItemsFragment extends Fragment implements BudgetCategoryFragment.OnRemoveCategoryListener {

    private FragmentBudgetItemsBinding binding;

    private CategoryViewModel categoryViewModel;
    private BudgetViewModel budgetViewModel;
    private CategoryPagerAdapter adapter;
    private ArrayAdapter<Category> categoryAdapter;
    private EventType eventType;
    private Long eventId;

    private final List<Category> plannedCategories = new ArrayList<>();
    private List<Category> otherCategories = new ArrayList<>();
    private List<Category> purchasedCategories;

    public static String ARG_EVENT_ID = "ARG_EVENT_ID";
    public static String ARG_EVENT_TYPE = "ARG_EVENT_TYPE";

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
        restoreBudget();
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

    private void configureCategoryAdapter() {
        categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySelector.setAdapter(categoryAdapter);
    }

    private void loadOtherCategories() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.addAll(categories);
        });
    }


    private void restoreBudget() {
        budgetViewModel.getBudget(eventId).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null && !result.getData().getItems().isEmpty()) {
                Budget budget = result.getData();
                purchasedCategories =  budget.getItems().stream()
                        .map(BudgetItem::getCategory)
                        .collect(toList());
            }
        });

    }

    private void addCategory(Category category) {
        if(purchasedCategories != null && purchasedCategories.contains(category)) {
            Toast.makeText(
                getContext(),
                R.string.already_bought_for_that_category,
                Toast.LENGTH_SHORT
            ).show();
            return;
        }
//        adapter.addFragment(BudgetCategoryFragment.newInstance(), category.getName());
        plannedCategories.add(category);
        otherCategories.remove(category);
    }

    private void loadSuggestedCategories() {
        adapter = new CategoryPagerAdapter(this);
        if(eventType != null) {
            eventType.getSuggestedCategories()
                    .forEach(category -> {
//                        adapter.addFragment(
//                                BudgetCategoryFragment.newInstance( category, eventId, plannedCategories.size()),
//                                category.getName());
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