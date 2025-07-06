package com.eventorium.presentation.solution.fragments.service;

import static com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment.ARG_ID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.helpers.SortBottomSheetDialogHelper;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServiceOverviewFragment extends Fragment {

    private FragmentServiceOverviewBinding binding;
    private ServiceViewModel viewModel;
    private EventTypeViewModel eventTypeViewModel;
    private CategoryViewModel categoryViewModel;
    private ServicesAdapter adapter;
    private SortBottomSheetDialogHelper helper;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;


    public ServiceOverviewFragment() {}

    public static ServiceOverviewFragment newInstance() {
        return new ServiceOverviewFragment();
    }

    private void configureServiceAdapter() {
        ImageLoader loader = new ImageLoader();
        adapter = new ServicesAdapter(
                getViewLifecycleOwner(),
                loader,
                service -> viewModel.getServiceImage(service.getId()),
                service -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    Bundle args = new Bundle();
                    args.putLong(ARG_ID, service.getId());
                    navController.navigate(R.id.action_serviceOverview_to_service_details, args);
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceOverviewBinding.inflate(inflater, container, false);

        initializeViewModels();
        configureServiceAdapter();
        helper = new SortBottomSheetDialogHelper();

        binding.servicesRecycleView.setAdapter(adapter);
        return binding.getRoot();
    }

    private void initializeViewModels() {
        viewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        eventTypeViewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeServices();
        setUpListeners();
    }

    private void setUpListeners() {
        binding.filterButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String keyword) { // search listener
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

        binding.filterButton.setOnClickListener(v -> createFilterBottomSheetDialog());
        binding.sortButton.setOnClickListener(v -> createSortBottomSheetDialog());
    }

    private void createFilterBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);

        loadCategories(dialogView.findViewById(R.id.spinnerCategory));
        loadEventTypes(dialogView.findViewById(R.id.spinnerEventType));

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setOnDismissListener(dialog -> onFilterBottomSheetDismiss((BottomSheetDialog) dialog));

        bottomSheetDialog.show();
    }

    private void createSortBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.solutions_sort, null);

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setOnDismissListener(dialog -> onSortBottomSheetDismiss((BottomSheetDialog) dialog));

        bottomSheetDialog.show();
    }

    private void onSortBottomSheetDismiss(BottomSheetDialog dialogView) {
        String sortCriteria = helper.extractSortCriteria(dialogView);
        if (sortCriteria != null)
            viewModel.sort(sortCriteria);
    }

    private void onFilterBottomSheetDismiss(BottomSheetDialog dialogView) {

        TextInputEditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        String name = nameEditText.getText().toString().trim();

        TextInputEditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        String description = descriptionEditText.getText().toString().trim();

        boolean availability = ((CheckBox) dialogView.findViewById(R.id.availabilityBox)).isChecked();

        Double minPrice = parsePrice(dialogView.findViewById(R.id.minPriceEditText));
        Double maxPrice = parsePrice(dialogView.findViewById(R.id.maxPriceEditText));

        Category category = getFromSpinner(dialogView.findViewById(R.id.spinnerCategory));
        EventType eventType = getFromSpinner(dialogView.findViewById(R.id.spinnerEventType));

        ServiceFilter filter = ServiceFilter.builder()
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

    private void observeServices() {
        viewModel.getItems().observe(getViewLifecycleOwner(), services -> {
            adapter.submitList(services);
            if(binding.loadingIndicator.getVisibility() == View.VISIBLE) {
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.servicesRecycleView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}