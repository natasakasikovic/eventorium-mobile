package com.eventorium.presentation.event.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.data.event.models.EventRatingsStatistics;
import com.eventorium.databinding.FragmentEventRatingStatisticsBinding;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventRatingStatisticsFragment extends Fragment {

    private FragmentEventRatingStatisticsBinding binding;
    private EventViewModel viewModel;
    public static String ARG_EVENT_ID = "event_id";
    private Long eventId;
    BarChart barChart;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;

    public EventRatingStatisticsFragment() { }

    public static EventRatingStatisticsFragment newInstance(Long id) {
        EventRatingStatisticsFragment fragment = new EventRatingStatisticsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            eventId = getArguments().getLong(ARG_EVENT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventRatingStatisticsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadStatistics();
    }

    private void loadStatistics() {
        viewModel.getStatistics(eventId).observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                EventRatingsStatistics stats = result.getData();
                fillChart(stats);
                binding.textView.setText(stats.getEventName());
                binding.totalRatingsText.setText("Total ratings: " + stats.getTotalRatings());
                binding.totalVisitorsText.setText("Total visitors: " + stats.getTotalVisitors());
            }
            else Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
        });
    }

    private void fillChart(EventRatingsStatistics event) {
        barChart = binding.idBarChart;

        setupBarData(event.getRatingsCount());
        setupXAxis();
        setupYAxis();
        setupChartStyle();

        barChart.invalidate();
    }

    private void setupBarData(Map<Integer, Integer> ratingsCount) {
        BarDataSet barDataSet = new BarDataSet(getBarEntries(ratingsCount), "Rating");
        barDataSet.setColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA);
        barDataSet.setValueTextSize(11f);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.15f);

        barChart.setData(barData);
    }

    private ArrayList<BarEntry> getBarEntries(Map<Integer, Integer> data) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            entries.add(new BarEntry(i, data.getOrDefault(i, 0)));
        }
        return entries;
    }

    private void setupXAxis() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.LTGRAY);
        xAxis.setGridLineWidth(1f);
        barChart.getXAxis().setAxisMinimum(0);
    }

    private void setupYAxis() {
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setGridLineWidth(1f);
        leftAxis.setTextColor(Color.WHITE);

        barChart.getAxisRight().setEnabled(false);
    }

    private void setupChartStyle() {
        barChart.animateY(1000);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(6);
        barChart.getDescription().setEnabled(false);
    }

}