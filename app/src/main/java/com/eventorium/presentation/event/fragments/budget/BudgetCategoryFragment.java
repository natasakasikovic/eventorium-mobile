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
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.models.Privacy;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.databinding.FragmentBudgetCategoryBinding;
import com.eventorium.presentation.chat.fragments.ChatFragment;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.util.listeners.OnPurchaseListener;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetCategoryFragment extends Fragment {

    public interface OnRemoveCategoryListener {
        void onRemoveCategory(int position, Category category);
    }

    private FragmentBudgetCategoryBinding binding;
    private BudgetViewModel budgetViewModel;
    private OnRemoveCategoryListener onRemoveCategoryListener;
    private ProductsAdapter productsAdapter;
    private ServicesAdapter servicesAdapter;

    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    public static final String ARG_ID = "ARG_EVENT_ID";
    public static final String ARG_EVENT_TYPE = "ARG_EVENT_TYPE";
    private static final String ARG_POSITION = "position";
    private static final String ARG_PRIVACY = "ARG_PRIVACY";
    private Category category;
    private int position;
    private Long eventId;
    private EventType eventType;

    private Privacy privacy;

    public BudgetCategoryFragment() {
    }

    public static BudgetCategoryFragment newInstance(
            Privacy privacy,
            EventType eventType,
            Category category,
            Long eventId,
            int position
    ) {
        BudgetCategoryFragment fragment = new BudgetCategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
        args.putParcelable(ARG_EVENT_TYPE, eventType);
        args.putInt(ARG_POSITION, position);
        args.putLong(ARG_ID, eventId);
        args.putParcelable(ARG_PRIVACY, privacy);
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
            eventType = getArguments().getParcelable(ARG_EVENT_TYPE);
            privacy = getArguments().getParcelable(ARG_PRIVACY);
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

        productsAdapter = new ProductsAdapter(new ArrayList<>(), productSummary -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ARG_ID, productSummary.getId());
            args.putLong(ProductDetailsFragment.ARG_EVENT_ID, eventId);
            args.putDouble(ProductDetailsFragment.ARG_PLANNED_AMOUNT, Double.parseDouble(String.valueOf(binding.plannedAmount.getText())));
            args.putParcelable(ProductDetailsFragment.ARG_EVENT_TYPE, eventType);
            args.putParcelable(ARG_PRIVACY, privacy);
            navController.navigate(R.id.action_budget_to_productDetails, args);
        });
//        servicesAdapter = new ServicesAdapter(new ArrayList<>(), configureServiceListener());


        return binding.getRoot();
    }


    private BudgetItem getBudgetItem(Double plannedAmount, ProductSummary item) {
        return BudgetItem.builder()
                .category(category)
                .itemId(item.getId())
                .plannedAmount(plannedAmount)
                .build();
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
//            productsAdapter.setData(products);
        });
    }

    private void searchServices(Long id, Double price) {
        budgetViewModel.getSuggestedServices(id, price).observe(getViewLifecycleOwner(), services -> {
            binding.itemsRecycleView.setAdapter(servicesAdapter);
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}