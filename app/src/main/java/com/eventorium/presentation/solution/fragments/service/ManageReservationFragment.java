package com.eventorium.presentation.solution.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentManageReservationBinding;
import com.eventorium.presentation.category.listeners.OnManualReservationListener;
import com.eventorium.presentation.solution.adapters.ManualReservationAdapter;
import com.eventorium.presentation.solution.viewmodels.ReservationViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageReservationFragment extends Fragment {

    private FragmentManageReservationBinding binding;
    private ReservationViewModel reservationViewModel;
    private ManualReservationAdapter adapter;

    public ManageReservationFragment() {
    }

    public static ManageReservationFragment newInstance() {
        return new ManageReservationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageReservationBinding.inflate(inflater, container, false);
        adapter = new ManualReservationAdapter(new ArrayList<>(), configureAdapter());
        binding.reservationsRecycleView.setAdapter(adapter);
        loadReservations();
        return binding.getRoot();
    }

    private void loadReservations() {
        reservationViewModel.observePendingReservations();
        reservationViewModel.getPendingReservations().observe(getViewLifecycleOwner(), reservations -> {
            adapter.setData(reservations);
        });
    }

    private OnManualReservationListener configureAdapter() {
        return new OnManualReservationListener() {
            @Override
            public void navigateToEvent(Long id) {

            }

            @Override
            public void navigateToService(Long id) {

            }

            @Override
            public void acceptReservation(Long id) {

            }

            @Override
            public void declineReservation(Long id) {

            }
        };
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}