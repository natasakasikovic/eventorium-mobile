package com.eventorium.presentation.event.fragments.budget;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.BudgetSuggestion;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.solution.models.SolutionType;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.databinding.FragmentBudgetCategoryBinding;
import com.eventorium.presentation.event.adapters.BudgetSuggestionAdapter;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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

    private BudgetSuggestionAdapter adapter;
    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    private static final String ARG_POSITION = "ARG_POS";
    public static final String ARG_EVENT = "ARG_EVENT";
    private Category category;
    private double plannedAmount;
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
        binding.deleteButton.setOnClickListener(v -> {
            if (onRemoveCategoryListener != null) {
                onRemoveCategoryListener.onRemoveCategory(position, category);
            }
        });
        configureAdapter();
        binding.itemsRecycleView.setAdapter(adapter);
        binding.searchItems.setOnClickListener(v -> search());
        return binding.getRoot();
    }

    private void configureAdapter() {
        adapter = new BudgetSuggestionAdapter(new ArrayList<>(), suggestion -> {
            if(suggestion.getSolutionType() == SolutionType.PRODUCT)
                navigateToProductDetails(suggestion);
            else
                navigateToServiceDetails(suggestion);

        });
    }

    private void navigateToServiceDetails(BudgetSuggestion suggestion) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(ServiceDetailsFragment.ARG_ID, suggestion.getId());
        args.putDouble(ServiceDetailsFragment.ARG_PLANNED_AMOUNT, plannedAmount);
        args.putParcelable(ServiceDetailsFragment.ARG_EVENT, event);
        navController.navigate(R.id.action_budget_to_serviceDetails, args);
    }


    private void navigateToProductDetails(BudgetSuggestion suggestion) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(ProductDetailsFragment.ARG_ID, suggestion.getId());
        args.putDouble(ProductDetailsFragment.ARG_PLANNED_AMOUNT, plannedAmount);
        args.putParcelable(ProductDetailsFragment.ARG_EVENT, event);
        navController.navigate(R.id.action_budget_to_productDetails, args);
    }


    private void search() {
        if(Objects.requireNonNull(binding.plannedAmount.getText()).toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        double price = Double.parseDouble(String.valueOf(binding.plannedAmount.getText()));
        plannedAmount = price;
        budgetViewModel.getBudgetSuggestions(event.getId(), category.getId(), price).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null){
                adapter.setData(result.getData());
                loadImages(result.getData());
            } else Toast.makeText(getContext(), result.getError(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadImages(List<BudgetSuggestion> suggestions) {
        suggestions.forEach(suggestion -> {
            if(suggestion.getSolutionType() == SolutionType.PRODUCT)
                loadImage(suggestion, productViewModel::getProductImage);
            else
                loadImage(suggestion, serviceViewModel::getServiceImage);
        });
    }


    private void loadImage(BudgetSuggestion suggestion, Function<Long, LiveData<Bitmap>> imageLoader) {
        imageLoader.apply(suggestion.getId())
                .observe(getViewLifecycleOwner(), image -> {
                    if (image != null) {
                        suggestion.setImage(image);
                        int position = adapter.getPosition(suggestion);
                        if (position != -1) {
                            adapter.notifyItemChanged(position);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}