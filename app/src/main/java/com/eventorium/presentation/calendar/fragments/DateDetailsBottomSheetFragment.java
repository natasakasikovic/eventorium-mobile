package com.eventorium.presentation.calendar.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentDateDetailsBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class DateDetailsBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String ARG_DATE = "date";
    private FragmentDateDetailsBottomSheetBinding binding;

    public DateDetailsBottomSheetFragment() { }

    public static DateDetailsBottomSheetFragment newInstance(CalendarDay date) {
        DateDetailsBottomSheetFragment fragment = new DateDetailsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putInt("year", date.getYear());
        args.putInt("month", date.getMonth());
        args.putInt("day", date.getDay());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDateDetailsBottomSheetBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            int year = getArguments().getInt("year");
            int month = getArguments().getInt("month");
            int day = getArguments().getInt("day");
        }
        return binding.getRoot();
    }
}