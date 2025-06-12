package com.eventorium.presentation.solution.fragments.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.databinding.FragmentPurchaseProductBinding;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.event.viewmodels.EventViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PurchaseProductFragment extends Fragment {

    private FragmentPurchaseProductBinding binding;
    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    public static final String ARG_PRODUCT_ID = "ARG_PRODUCT_ID";
    private BudgetViewModel budgetViewModel;
    private EventViewModel eventViewModel;
    private Long productId;
    private Category category;

    public PurchaseProductFragment() {
    }

    public static PurchaseProductFragment newInstance(Long productId, Category category) {
        PurchaseProductFragment fragment = new PurchaseProductFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PRODUCT_ID, productId);
        args.putParcelable(ARG_CATEGORY, category);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            productId = getArguments().getLong(ARG_PRODUCT_ID);
            category = getArguments().getParcelable(ARG_CATEGORY);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        budgetViewModel = provider.get(BudgetViewModel.class);
        eventViewModel = provider.get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentPurchaseProductBinding.inflate(inflater, container, false);
        loadEvents();
        binding.purchaseProductButton.setOnClickListener(v -> onPurchase());
        return binding.getRoot();
    }

    private void onPurchase() {
        Event event = (Event) binding.eventSelector.getSelectedItem();
        BudgetItemRequest item = buildItem();
        if(item == null) return;
        budgetViewModel.purchaseProduct(event.getId(), item).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                Toast.makeText(
                        requireContext(),
                        R.string.successfully_purchased_product,
                        Toast.LENGTH_SHORT
                ).show();
                NavController navController = Navigation.findNavController(requireView());
                navController.popBackStack();
            } else {
                Toast.makeText(
                        requireContext(),
                        result.getError(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private BudgetItemRequest buildItem() {
        String plannedAmount = String.valueOf(binding.plannedAmountText.getText());
        if(plannedAmount.trim().isEmpty()) {
            Toast.makeText(
                    requireContext(),
                    R.string.please_fill_in_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return null;
        }

        return BudgetItemRequest.builder()
                .itemId(productId)
                .category(category)
                .plannedAmount(Double.parseDouble(plannedAmount))
                .build();
    }

    private void loadEvents() {
        ArrayAdapter<Event> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventViewModel.getFutureEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                adapter.addAll(result.getData());
                adapter.notifyDataSetChanged();
                binding.eventSelector.setAdapter(adapter);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}