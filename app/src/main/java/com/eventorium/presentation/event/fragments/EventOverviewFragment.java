package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.event.fragments.EventDetailsFragment.ARG_EVENT_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.databinding.FragmentEventOverviewBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.event.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventOverviewFragment extends Fragment {

    private FragmentEventOverviewBinding binding;
    private EventViewModel viewModel;
    private EventTypeViewModel eventTypeViewModel;
    private EventsAdapter adapter;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventOverviewBinding.inflate(inflater, container, false);

        adapter = new EventsAdapter(new ArrayList<>(), event -> {
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
        setUpListener();
    }

    private void setUpObserver(){
        viewModel.getEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null){
                adapter.setData(result.getData());
                loadEventImage(result.getData());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
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

    private void setUpListener(){
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String keyword) {
                viewModel.searchEvents(keyword).observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null) {
                        adapter.setData(result.getData());
                        loadEventImage(result.getData());
                    } else
                         Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
                });
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}