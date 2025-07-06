package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.event.fragments.EventDetailsFragment.ARG_EVENT_ID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
                getViewLifecycleOwner(),
                imageLoader,
                event -> eventTypeViewModel.getImage(event.getImageId()),
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

        binding.filterButton.setOnClickListener(v -> createFilterBottomSheetDialog());
        binding.sortButton.setOnClickListener(v -> createSortBottomSheetDialog());
    }

    private void createFilterBottomSheetDialog() {
        dialogView = getLayoutInflater().inflate(R.layout.events_filter, null);
        bottomSheetDialog = new BottomSheetDialog(requireActivity());

        loadCities(dialogView.findViewById(R.id.spinnerCity));
        loadEventTypes(dialogView.findViewById(R.id.spinnerEventType));
        createDatePickers();

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setOnDismissListener(dialog -> onFilterBottomSheetDismiss());

        bottomSheetDialog.show();
    }

    private void createSortBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.events_sort, null);

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setOnDismissListener(dialog -> onSortBottomSheetDismiss((BottomSheetDialog) dialog));

        bottomSheetDialog.show();
    }

    private void onSortBottomSheetDismiss(BottomSheetDialog dialogView) {
        String sortCriteria = extractSortCriteria(dialogView);
        if (sortCriteria != null)
            viewModel.sort(sortCriteria);
    }

    private String extractSortCriteria(BottomSheetDialog dialogView) {
        RadioGroup radioGroupSortField = dialogView.findViewById(R.id.radioGroupSortField);
        RadioGroup radioGroupOrder = dialogView.findViewById(R.id.radioGroupOrder);

        int selectedSortFieldId = radioGroupSortField.getCheckedRadioButtonId();
        int selectedOrderId = radioGroupOrder.getCheckedRadioButtonId();

        if (selectedSortFieldId == -1 || selectedOrderId == -1)
            return null;

        RadioButton selectedSortField = dialogView.findViewById(selectedSortFieldId);
        RadioButton selectedOrder = dialogView.findViewById(selectedOrderId);

        String sortField = getSortField(selectedSortField.getId());
        String sortDirection = getSortDirection(selectedOrder.getText().toString());

        return String.format("%s,%s", sortField, sortDirection);
    }

    private String getSortField(int radioButtonId) {
        if (radioButtonId == R.id.radioName)
            return "name";
        else if (radioButtonId == R.id.radioDescription)
            return "description";
        else if (radioButtonId == R.id.radioDate)
            return "date";
        else if (radioButtonId == R.id.radioMaxParticipants)
            return "maxParticipants";
        else
            return "name"; // default fallback
    }

    private String getSortDirection(String text) {
        text = text.toLowerCase();
        if (text.contains("asc"))
            return "asc";
        else if (text.contains("desc"))
            return "desc";
        else
            return "asc"; // default fallback
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

    private void onFilterBottomSheetDismiss() {
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