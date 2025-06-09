package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.databinding.FragmentManageableEventsBinding;
import com.eventorium.presentation.event.adapters.ManageableEventAdapter;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.event.viewmodels.ManageableEventViewModel;
import com.eventorium.presentation.solution.listeners.OnManageListener;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageableEventsFragment extends Fragment {

    private FragmentManageableEventsBinding binding;
    private List<EventSummary> events;
    private ManageableEventAdapter adapter;
    private ManageableEventViewModel viewModel;
    private EventTypeViewModel eventTypeViewModel;
    private RecyclerView recyclerView;

    public ManageableEventsFragment() {}

    public static ManageableEventsFragment newInstance() {
        return new ManageableEventsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(ManageableEventViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageableEventsBinding.inflate(inflater, container, false);
        setUpAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadEvents();
        setUpSearchListener();
    }

    private void setUpAdapter() {
        recyclerView = binding.eventsRecycleView;
        adapter = new ManageableEventAdapter(new ArrayList<>(), new OnManageListener<>() {

            @Override
            public void onSeeMoreClick(EventSummary item) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.action_manage_events_to_event_details,
                        EventDetailsFragment.newInstance(item.getId()).getArguments());
            }

            @Override
            public void onEditClick(EventSummary item) {

            }

            @Override
            public void onDeleteClick(EventSummary item) {
                // NOTE: Add cancellation logic here if needed in the future
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void setUpSearchListener() {

    }

    private void loadEvents() {
        viewModel.getEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                events = result.getData();
                adapter.setEvents(events);
                loadEventImage(events);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEventImage(List<EventSummary> events) {
        events.forEach( event -> eventTypeViewModel.getImage(event.getImageId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null) {
                        event.setImage(image);
                        int position = events.indexOf(event);
                        if (position != -1) {
                            adapter.notifyItemChanged(position);
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}