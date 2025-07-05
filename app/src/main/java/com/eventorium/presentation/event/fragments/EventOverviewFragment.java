package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.event.fragments.EventDetailsFragment.ARG_EVENT_ID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.event.EventFilter;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentEventOverviewBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventOverviewFragment extends Fragment {

    private FragmentEventOverviewBinding binding;
    private EventViewModel viewModel;
    private EventTypeViewModel eventTypeViewModel;
    private CityViewModel cityViewModel;
    private EventsAdapter adapter;
    private View dialogView;
    private BottomSheetDialog bottomSheetDialog;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public EventOverviewFragment() { }

    public static EventOverviewFragment newInstance() {
        return new EventOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(EventViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
        cityViewModel = provider.get(CityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEventOverviewBinding.inflate(inflater, container, false);
        ImageLoader imageLoader = new ImageLoader();
        adapter = new EventsAdapter(
                imageLoader,
                event -> () -> eventTypeViewModel.getImage(event.getImageId()),
                event -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    Bundle args = new Bundle();
                    args.putLong(ARG_EVENT_ID, event.getId());
                    navController.navigate(R.id.action_overview_to_event_details, args);
                });
        binding.eventsRecycleView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObserver();
        setUpListeners();
    }

    private void setUpObserver(){
        viewModel.getItems().observe(getViewLifecycleOwner(), events -> {
            adapter.submitList(events);
        });
    }

    private void createDatePickers() {
        TextInputEditText fromDate = dialogView.findViewById(R.id.fromDateEditText);
        TextInputEditText toDate = dialogView.findViewById(R.id.toDateEditText);

        setupDatePicker(fromDate, "Select start date");
        setupDatePicker(toDate, "Select end date");
    }

    private void setupDatePicker(TextInputEditText editText, String title) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(title).build();

        editText.setOnClickListener(v -> datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER_" + title));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            String formattedDate = formatDate(selection);
            editText.setText(formattedDate);
        });
    }

    private String formatDate(Long millis) {
        LocalDate selectedDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
    }

    private void setUpListeners(){
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        dialogView = getLayoutInflater().inflate(R.layout.events_filter, null);
        bottomSheetDialog = new BottomSheetDialog(requireActivity());

        loadCities(dialogView.findViewById(R.id.spinnerCity));
        loadEventTypes(dialogView.findViewById(R.id.spinnerEventType));
        createDatePickers();

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setOnDismissListener(dialog -> onBottomSheetDismiss());

        bottomSheetDialog.show();
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

    private void onBottomSheetDismiss() {
        if (dialogView == null) return;

        TextInputEditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        String name = nameEditText.getText().toString().trim();

        TextInputEditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        String description = descriptionEditText.getText().toString().trim();

        EventType type = getFromSpinner(dialogView.findViewById(R.id.spinnerEventType));
        String eventTypeName = type != null ? type.getName() : null;

        City city = getFromSpinner(dialogView.findViewById(R.id.spinnerCity));
        String cityName = city != null ? city.getName() : null;

        Integer maxParticipants = parseInteger(dialogView.findViewById(R.id.maxParticipantsEditText));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

        TextInputEditText toDateEditText = dialogView.findViewById(R.id.toDateEditText);
        LocalDate to = parseDate(toDateEditText, formatter);

        TextInputEditText fromDateEditText = dialogView.findViewById(R.id.fromDateEditText);
        LocalDate from = parseDate(fromDateEditText, formatter);

        EventFilter filter = new EventFilter(name, description, eventTypeName, maxParticipants, cityName, from, to);

        viewModel.filter(filter);
    }

    private Integer parseInteger(TextInputEditText textInput) {
        String priceText = textInput.getText() != null ? textInput.getText().toString().trim() : "";

        if (priceText.isEmpty()) return null;

        try {
            return Integer.parseInt(priceText);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate parseDate(TextInputEditText editText, DateTimeFormatter formatter) {

        if (editText == null || editText.getText() == null)
            return null;

        String dateText = editText.getText().toString().trim();
        if (dateText.isEmpty())
            return null;

        try {
            return LocalDate.parse(dateText, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private <T> String getItemName(T item) {
        try {
            Method method = item.getClass().getMethod("getName");
            return (String) method.invoke(item);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return "";
        }
    }

    private void loadCities(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(List.of("")));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cityViewModel.getCities().observe(getViewLifecycleOwner(), cities -> {
            adapter.addAll(cities.stream().map(City::getName).toArray(String[]::new));
            adapter.notifyDataSetChanged();
            spinner.setAdapter(adapter);
            spinner.setTag(cities);
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        dialogView = null;
        bottomSheetDialog = null;
    }
}