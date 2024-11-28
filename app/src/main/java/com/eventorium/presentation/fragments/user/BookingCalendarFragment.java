package com.eventorium.presentation.fragments.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentBookingCalendarBinding;

public class BookingCalendarFragment extends Fragment {

    private FragmentBookingCalendarBinding binding;

    public BookingCalendarFragment() { }

    public static BookingCalendarFragment newInstance() {
        return new BookingCalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}