package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.databinding.FragmentEventOverviewBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.event.viewmodels.EventViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventOverviewFragment extends Fragment {

    private FragmentEventOverviewBinding binding;
    private EventViewModel viewModel;

    public EventOverviewFragment() { }

    public static EventOverviewFragment newInstance() {
        return new EventOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventOverviewBinding.inflate(inflater, container, false);
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
                binding.eventsRecycleView.setAdapter(new EventsAdapter(result.getData()));
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpListener(){
        binding.searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String keyword) {
                viewModel.searchEvents(keyword).observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null)
                        binding.eventsRecycleView.setAdapter(new EventsAdapter(result.getData()));
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}