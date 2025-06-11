package com.eventorium.presentation.solution.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.solution.adapters.ManageableServiceAdapter;
import com.eventorium.presentation.solution.viewmodels.ManageableServiceViewModel;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.solution.listeners.OnManageListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageServiceFragment extends Fragment {

    private FragmentServiceOverviewBinding binding;
    private ManageableServiceViewModel manageableServiceViewModel;
    private ServiceViewModel serviceViewModel;
    private CategoryViewModel categoryViewModel;
    private EventTypeViewModel eventTypeViewModel;
    private ManageableServiceAdapter adapter;
    private CircularProgressIndicator loadingIndicator;
    private RecyclerView recyclerView;
    private TextView noServicesText;

    public ManageServiceFragment() {
    }

    public static ManageServiceFragment newInstance() {
        return new ManageServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        manageableServiceViewModel = provider.get(ManageableServiceViewModel.class);
        serviceViewModel = provider.get(ServiceViewModel.class);
        categoryViewModel = provider.get(CategoryViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoadingIndicator();

        adapter = new ManageableServiceAdapter(new ArrayList<>(), new OnManageListener<>() {
            @Override
            public void onDeleteClick(ServiceSummary item) {
                showDeleteDialog(item);
            }

            @Override
            public void onSeeMoreClick(ServiceSummary serviceSummary) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                NavGraph currentGraph = navController.getGraph();
                Log.d("Navigation", "Current Graph: " + currentGraph);
                navController.navigate(R.id.action_manageServices_to_serviceDetails,
                        ServiceDetailsFragment.newInstance(serviceSummary.getId()).getArguments());
            }

            @Override
            public void onEditClick(ServiceSummary serviceSummary) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.action_manageService_to_editService,
                        EditServiceFragment.newInstance(serviceSummary).getArguments());
            }
        });
        binding.filterButton.setOnClickListener(v -> createBottomSheetDialog());
        recyclerView = binding.servicesRecycleView;
        recyclerView.setAdapter(adapter);
        loadingIndicator = binding.loadingIndicator;
        noServicesText = binding.noServicesText;

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                manageableServiceViewModel.searchServices(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    manageableServiceViewModel.searchServices(null);
                }
                return true;
            }
        });

        loadServices();
        setupSearch();
        setupFilter();
    }

    private void showDeleteDialog(ServiceSummary service) {
        new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setTitle("Delete Service")
                .setMessage("Are you sure you want to delete " + service.getName() + "?" )
                .setPositiveButton("Delete", (dialog, which) -> {
                    manageableServiceViewModel.deleteService(service.getId())
                            .observe(getViewLifecycleOwner(), result -> {
                                if(result.getError() == null) {
                                    Toast.makeText(
                                            requireContext(),
                                            R.string.service_deleted_successfully,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    manageableServiceViewModel.removeService(service.getId());
                                } else {
                                    Toast.makeText(
                                            requireContext(),
                                            result.getError(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void createBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);
        loadCategories(dialogView.findViewById(R.id.spinnerCategory));
        loadEventTypes(dialogView.findViewById(R.id.spinnerEventType));
        bottomSheetDialog.setContentView(dialogView);

        bottomSheetDialog.setOnDismissListener(dialog
                -> onBottomSheetDismiss((BottomSheetDialog) dialog));

        bottomSheetDialog.show();
    }

    private void onBottomSheetDismiss(BottomSheetDialog dialogView) {
        boolean availability
                = ((CheckBox) Objects.requireNonNull(dialogView.findViewById(R.id.availabilityBox)))
                .isChecked();;
        TextInputEditText minTextField = dialogView.findViewById(R.id.minPriceEditText);
        TextInputEditText maxTextField = dialogView.findViewById(R.id.maxPriceEditText);
        Double minPrice = parsePrice(minTextField);
        Double maxPrice = parsePrice(maxTextField);
        Category category = getFromSpinner(Objects.requireNonNull(dialogView.findViewById(R.id.spinnerCategory)));
        EventType eventType = getFromSpinner(Objects.requireNonNull(dialogView.findViewById(R.id.spinnerEventType)));

        ServiceFilter filter = ServiceFilter.builder()
                .availability(availability)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .category(category == null ? null : category.getName())
                .type(eventType == null ? null : eventType.getName())
                .build();

        manageableServiceViewModel.filterServices(filter);
    }

    private Double parsePrice(TextInputEditText textInput) {
        Double price = null;
        try {
            price = Double
                    .parseDouble(Objects.requireNonNull(textInput.getText()).toString());
        } catch (Exception ignored) {
        }
        return price;
    }

    private void loadEventTypes(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(List.of(""))
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        eventTypeViewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            adapter.addAll(eventTypes.stream().map(EventType::getName).toArray(String[]::new));
            adapter.notifyDataSetChanged();
            spinner.setAdapter(adapter);
            spinner.setTag(eventTypes);
        });
    }
    private<T> T getFromSpinner(Spinner spinner) {
        String name = spinner.getSelectedItem().toString();
        T value;
        if (!name.isEmpty()) {
            value = ((List<T>) spinner.getTag())
                .stream()
                .filter(c -> {
                    try {
                        return Objects
                                .equals(c.getClass().getMethod("getName").invoke(c), name);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst().get();
            return value;
        }
        return null;
    }

    private void getServiceImages(List<ServiceSummary> services) {
        services.forEach(service ->
            serviceViewModel.getServiceImage(service.getId())
                    .observe(getViewLifecycleOwner(), image -> {
                        if(image != null) {
                            service.setImage(image);
                            int position = services.indexOf(service);
                            adapter.notifyItemChanged(position);
                        }
                    }));
    }

    private void loadCategories(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(List.of(""))
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.addAll(categories.stream().map(Category::getName).toArray(String[]::new));
            adapter.notifyDataSetChanged();
            spinner.setAdapter(adapter);
            spinner.setTag(categories);
        });
    }

    private void updateServiceAdapter(List<ServiceSummary> services) {
        if(services != null && !services.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            noServicesText.setVisibility(View.GONE);
            getServiceImages(services);
            adapter.setServices(services);
        } else {
            adapter.setServices(Collections.EMPTY_LIST);
            recyclerView.setVisibility(View.GONE);
            noServicesText.setVisibility(View.VISIBLE);
        }
    }

    private void loadServices() {
        manageableServiceViewModel.getManageableServices()
                .observe(getViewLifecycleOwner(), this::updateServiceAdapter);
    }

    private void setupFilter() {
        manageableServiceViewModel.getFilterResults()
                .observe(getViewLifecycleOwner(), this::updateServiceAdapter);
    }

    private void setupSearch() {
        manageableServiceViewModel.getSearchResults()
                .observe(getViewLifecycleOwner(), this::updateServiceAdapter);
    }

    private void showLoadingIndicator() {
        manageableServiceViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}