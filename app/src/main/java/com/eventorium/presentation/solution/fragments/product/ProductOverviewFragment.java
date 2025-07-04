package com.eventorium.presentation.solution.fragments.product;

import static com.eventorium.R.*;
import static com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment.ARG_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.databinding.FragmentProductOverviewBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductOverviewFragment extends Fragment {

    private FragmentProductOverviewBinding binding;
    private ProductViewModel viewModel;
    private EventTypeViewModel eventTypeViewModel;
    private CategoryViewModel categoryViewModel;
    private ProductsAdapter adapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public ProductOverviewFragment() { }

    public static ProductOverviewFragment newInstance() {
        return new ProductOverviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductOverviewBinding.inflate(inflater, container, false);

        initializeViewModels();

        configureAdapter();
        binding.productsRecycleView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void initializeViewModels() {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        eventTypeViewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeProducts();
        setUpListeners();
    }

    private void configureAdapter(){
        ImageLoader loader = new ImageLoader(requireContext());
        adapter = new ProductsAdapter(
                loader,
                product -> () -> viewModel.getProductImage(product.getId()),
                product -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    Bundle args = new Bundle();
                    args.putLong(ARG_ID, product.getId());
                    navController.navigate(R.id.action_productOverview_to_productDetails, args);
                });
    }

    private void observeProducts() {
        viewModel.getItems().observe(getViewLifecycleOwner(), products -> {
            adapter.submitList(products);
        });
    }

    private void setUpListeners() {
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // search listener
            @Override
            public boolean onQueryTextChange(String keyword) {
                if (searchRunnable != null)
                    handler.removeCallbacks(searchRunnable);

                searchRunnable = () -> viewModel.search(keyword);
                handler.postDelayed(searchRunnable, 300);
                return true;

            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        binding.filterButton.setOnClickListener(v -> createBottomSheetDialog()); // filter listener
    }

    private void createBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.products_filter, null);

        loadCategories(dialogView.findViewById(R.id.spinnerCategory));
        loadEventTypes(dialogView.findViewById(R.id.spinnerEventType));

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setOnDismissListener(dialog -> onBottomSheetDismiss((BottomSheetDialog) dialog));

        bottomSheetDialog.show();
    }

    private void onBottomSheetDismiss(BottomSheetDialog dialogView) {

        TextInputEditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        String name = nameEditText.getText().toString().trim();

        TextInputEditText descriptionEditText = dialogView.findViewById(id.descriptionEditText);
        String description = descriptionEditText.getText().toString().trim();

        boolean availability = ((CheckBox) dialogView.findViewById(R.id.availabilityBox)).isChecked();

        Double minPrice = parsePrice(dialogView.findViewById(R.id.minPriceEditText));
        Double maxPrice = parsePrice(dialogView.findViewById(R.id.maxPriceEditText));

        Category category = getFromSpinner(dialogView.findViewById(R.id.spinnerCategory));
        EventType eventType = getFromSpinner(dialogView.findViewById(R.id.spinnerEventType));

        ProductFilter filter = ProductFilter.builder()
                .name(name)
                .description(description)
                .availability(availability)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .category(category != null ? category.getName() : null)
                .type(eventType != null ? eventType.getName() : null)
                .build();


        viewModel.filter(filter);
    }

    private Double parsePrice(TextInputEditText textInput) {
        String priceText = textInput.getText() != null ? textInput.getText().toString().trim() : "";

        if (priceText.isEmpty()) return null;

        try {
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private <T> T getFromSpinner(Spinner spinner) {
        String name = spinner.getSelectedItem() != null ? spinner.getSelectedItem().toString().trim() : "";

        if (name.isEmpty())
            return null;

        List<T> items = (List<T>) spinner.getTag();
        if (items == null)
            return null;

        return items.stream().filter(item -> name.equals(getItemName(item))).findFirst().orElse(null);
    }

    private <T> String getItemName(T item) {
        try {
            Method method = item.getClass().getMethod("getName");
            return (String) method.invoke(item);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return "";
        }
    }

    private void loadEventTypes(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(List.of("")));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventTypeViewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            adapter.addAll(eventTypes.stream().map(EventType::getName).toArray(String[]::new));
            adapter.notifyDataSetChanged();
            spinner.setAdapter(adapter);
            spinner.setTag(eventTypes);
        });
    }

    private void loadCategories(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(List.of("")));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.addAll(categories.stream().map(Category::getName).toArray(String[]::new));
            adapter.notifyDataSetChanged();
            spinner.setAdapter(adapter);
            spinner.setTag(categories);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}