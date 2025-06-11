package com.eventorium.presentation.favourites.fragments;

import static com.eventorium.presentation.event.fragments.EventDetailsFragment.ARG_EVENT_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.databinding.FragmentFavouriteEventsBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.favourites.viewmodels.FavouritesViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteEventsFragment extends Fragment {

    private FragmentFavouriteEventsBinding binding;
    private FavouritesViewModel viewModel;
    private List<EventSummary> events;

    public FavouriteEventsFragment() { }

    public static FavouriteEventsFragment newInstance() {
        return new FavouriteEventsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
    }

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavouriteEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFavouriteEvents();
    }

    public void loadFavouriteEvents() {
        viewModel.getFavouriteEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                events = result.getData();
                setupAdapter();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter() {
        binding.eventsRecycleView.setAdapter(new EventsAdapter(events, event -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ARG_EVENT_ID, event.getId());
            navController.navigate(R.id.action_fav_to_event_details, args);
        }));
    }
}