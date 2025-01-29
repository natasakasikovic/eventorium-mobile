package com.eventorium.presentation.calendar.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentCalendarBinding;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    MaterialCalendarView calendarView;

    public CalendarFragment() { }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        setupCalendarView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    private void setupCalendarView() {
        calendarView = binding.calendarView;
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                DateDetailsBottomSheetFragment bottomSheetFragment = DateDetailsBottomSheetFragment.newInstance(date);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

}