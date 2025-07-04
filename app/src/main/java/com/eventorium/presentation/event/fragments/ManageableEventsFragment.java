package com.eventorium.presentation.event.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.databinding.FragmentManageableEventsBinding;
import com.eventorium.presentation.event.adapters.ManageableEventAdapter;
import com.eventorium.presentation.event.fragments.budget.BudgetItemsListFragment;
import com.eventorium.presentation.event.listeners.OnManageEventListener;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.event.viewmodels.ManageableEventViewModel;
import com.eventorium.presentation.shared.utils.ImageLoader;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageableEventsFragment extends Fragment {

    private FragmentManageableEventsBinding binding;
    private ManageableEventAdapter adapter;
    private ManageableEventViewModel viewModel;
    private EventTypeViewModel eventTypeViewModel;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageableEventsBinding.inflate(inflater, container, false);
        viewModel.refresh();
        setUpAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeEvents();
        setUpSearchListener();
    }

    private void setUpAdapter() {
        RecyclerView recyclerView = binding.eventsRecycleView;
        ImageLoader imageLoader = new ImageLoader(requireContext());
        adapter = new ManageableEventAdapter(
                imageLoader,
                event -> () -> eventTypeViewModel.getImage(event.getImageId()),
                new OnManageEventListener() {

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
    }

    private void observeEvents() {
        viewModel.getItems().observe(getViewLifecycleOwner(), events -> {
            adapter.submitList(events);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}