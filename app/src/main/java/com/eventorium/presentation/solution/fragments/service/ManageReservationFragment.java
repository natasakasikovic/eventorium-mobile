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
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.models.Status;
import com.eventorium.data.solution.models.service.Reservation;
import com.eventorium.databinding.FragmentManageReservationBinding;
import com.eventorium.presentation.category.listeners.OnManualReservationListener;
import com.eventorium.presentation.event.fragments.EventDetailsFragment;
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
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(EventDetailsFragment.ARG_EVENT_ID, id);
                navController.navigate(R.id.action_manageReservation_to_eventDetails, args);
            }

            @Override
            public void navigateToService(Long id) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(ServiceDetailsFragment.ARG_ID, id);
                navController.navigate(R.id.action_manageReservation_to_serviceDetails, args);
            }

            @Override
            public void acceptReservation(Long id) {
                reservationViewModel.updateReservation(id, Status.ACCEPTED)
                        .observe(getViewLifecycleOwner(), result -> handleUpdateResult(id, result));
            }

            @Override
            public void declineReservation(Long id) {
                reservationViewModel.updateReservation(id, Status.DECLINED)
                        .observe(getViewLifecycleOwner(), result -> handleUpdateResult(id, result));
            }
        };
    }

    private void handleUpdateResult(Long id, Result<Reservation> result) {
        if(result.getError() == null) {
            Toast.makeText(
                    requireContext(),
                    getString(R.string.successfully_updated_reservation),
                    Toast.LENGTH_SHORT
            ).show();
            reservationViewModel.removeReservation(id);
        } else {
            Toast.makeText(
                    requireContext(),
                    result.getError(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}