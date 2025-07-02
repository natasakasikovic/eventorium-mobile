package com.eventorium.presentation.event.fragments;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.databinding.FragmentManageableEventsBinding;
import com.eventorium.presentation.event.adapters.ManageableEventAdapter;
import com.eventorium.presentation.event.fragments.budget.BudgetItemsListFragment;
import com.eventorium.presentation.event.listeners.OnManageEventListener;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.event.viewmodels.ManageableEventViewModel;
import com.eventorium.presentation.shared.listeners.PaginationScrollListener;
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
    NavController navController;

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
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
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
        observeEvents();
        viewModel.refresh();
        setUpSearchListener();
        setupScrollListener(binding.eventsRecycleView);
    }

    private void setUpAdapter() {
        recyclerView = binding.eventsRecycleView;
        adapter = new ManageableEventAdapter(new ArrayList<>(), new OnManageEventListener() {

            @Override
            public void onSeeMoreClick(EventSummary item) {
                navController.navigate(R.id.action_manage_events_to_event_details,
                        EventDetailsFragment.newInstance(item.getId()).getArguments());
            }

            @Override
            public void onEditClick(EventSummary item) {
                navController.navigate(R.id.action_manage_events_to_edit_event,
                        EditEventFragment.newInstance(item.getId()).getArguments());
            }

            @Override
            public void navigateToBudget(EventSummary item) {
                Event event = Event.builder().id(item.getId()).build();
                navController.navigate(R.id.action_manage_events_to_budget,
                        BudgetItemsListFragment.newInstance(event, false).getArguments());
            }

            @Override
            public void onDeleteClick(EventSummary item) {
                throw new UnsupportedOperationException("Not supported in this context.");
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void setUpSearchListener() {
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String keyword) {
                viewModel.search(keyword);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void observeEvents() {
        viewModel.getItems().observe(getViewLifecycleOwner(), events -> {
            adapter.setData(events);
            loadEventImage(events);
        });
    }

    private void setupScrollListener(RecyclerView recyclerView) {
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);
        recyclerView.addOnScrollListener(new PaginationScrollListener(layout) {
            @Override
            protected void loadMoreItems() {
                viewModel.loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return viewModel.isLoading;
            }

            @Override
            public boolean isLastPage() {
                return viewModel.isLastPage;
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