package com.eventorium.presentation.event.fragments;

import android.content.Intent;
import android.net.Uri;
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
import com.eventorium.data.event.models.PastEvent;
import com.eventorium.databinding.FragmentPastEventsOverviewBinding;
import com.eventorium.presentation.event.adapters.PastEventAdapter;
import com.eventorium.presentation.event.listeners.OnPastEventActionListener;
import com.eventorium.presentation.event.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PastEventsOverviewFragment extends Fragment {
    private FragmentPastEventsOverviewBinding binding;
    private EventViewModel viewModel;
    private List<PastEvent> events;
    private RecyclerView recyclerView;
    private PastEventAdapter adapter;

    public PastEventsOverviewFragment() { }

    public static PastEventsOverviewFragment newInstance() {
        return new PastEventsOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPastEventsOverviewBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        setUpAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void setUpAdapter() {
        recyclerView = binding.eventsRecycleView;
        adapter = new PastEventAdapter(new ArrayList<>(), new OnPastEventActionListener() {
            @Override
            public void onStatisticsClicked(PastEvent event) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                // TODO: navigate to event statistics fragment
            }

            @Override
            public void onPdfExportClicked(PastEvent event) {
                viewModel.exportEventStatisticsToPdf(event.getId(), requireContext()).observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null) openPdf(result.getData());
                    else Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                });
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        this.viewModel.getPassedEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                this.events = result.getData();
                adapter.setEvents(events);
            } else
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openPdf(Uri pdfUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}