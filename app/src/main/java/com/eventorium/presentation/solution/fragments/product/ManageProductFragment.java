package com.eventorium.presentation.solution.fragments.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.databinding.FragmentProductOverviewBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.solution.adapters.ManageableProductAdapter;
import com.eventorium.presentation.solution.listeners.OnManageListener;
import com.eventorium.presentation.solution.viewmodels.ManageableProductViewModel;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageProductFragment extends Fragment {

    private FragmentProductOverviewBinding binding;
    private ManageableProductViewModel viewModel;
    private ProductViewModel productViewModel;
    private EventTypeViewModel eventTypeViewModel;
    private CategoryViewModel categoryViewModel;
    private ManageableProductAdapter adapter;
    private List<ProductSummary> products;
    private RecyclerView recyclerView;

    public ManageProductFragment() { }

    public static ManageProductFragment newInstance() {
        return new ManageProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModels();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductOverviewBinding.inflate(inflater, container, false);
        setUpAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProducts();
        setUpListeners();
    }

    private void initializeViewModels() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(ManageableProductViewModel.class);
        productViewModel = provider.get(ProductViewModel.class);
        eventTypeViewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    private void setUpAdapter() {
        recyclerView = binding.productsRecycleView;
        adapter = new ManageableProductAdapter(new ArrayList<>(), new OnManageListener<>() {
            @Override
            public void onDeleteClick(ProductSummary item) {
                showDeleteDialog(item);
            }

            @Override
            public void onSeeMoreClick(ProductSummary item) {
                navigateToProductDetails(item);
            }

            @Override
            public void onEditClick(ProductSummary item) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.action_manage_products_to_edit_product,
                        EditProductFragment.newInstance(item).getArguments());
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                        viewModel.loadNextPage();
                        loadImages();
                    }
                }
            }
        });
    }

    private void loadProducts() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.addProducts(products);
        });
    }

    private void loadImages() {
        products.forEach(product -> productViewModel
            .getProductImage(product.getId()).observe(getViewLifecycleOwner(), img -> {
            if (img != null) {
                product.setImage(img);
                int position = products.indexOf(product);
                adapter.notifyItemChanged(position);
            }
        }));
    }

    private void navigateToProductDetails(ProductSummary product) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        navController.navigate(R.id.action_manage_product_to_product_details,
                ProductDetailsFragment.newInstance(product.getId()).getArguments());
    }

    private void setUpListeners() {
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // search listener
            @Override
            public boolean onQueryTextChange(String keyword) {
                viewModel.searchProducts(keyword).observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null) {
                        products = result.getData();
                        adapter.setData(products);
                        loadImages();
                    }
                    else
                        Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
                });
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

        TextInputEditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
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


        observeFilteringProducts(filter);
    }

    private void observeFilteringProducts(ProductFilter filter) {
        viewModel.filterProducts(filter).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                products = result.getData();
                adapter.setData(products);
                loadImages();
            } else
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
        });
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

    private void showDeleteDialog(ProductSummary product) {
        new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setTitle("Delete product")
                .setMessage("Are you sure you want to delete " + product.getName() + "?" )
                .setPositiveButton("Delete", (dialog, which) -> onDialogConfirmation(product.getId()))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void onDialogConfirmation(Long productId) {
        productViewModel.deleteProduct(productId)
                .observe(getViewLifecycleOwner(), result -> {
                    if(result.getError() == null) {
                        Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show();
                        adapter.removeProduct(productId);
                    } else {
                        Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


}