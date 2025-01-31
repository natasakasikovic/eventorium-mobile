package com.eventorium.presentation.notifications.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.databinding.FragmentNotificationsBinding;
import com.eventorium.presentation.notifications.adapters.NotificationAdapter;
import com.eventorium.presentation.notifications.viewmodels.NotificationViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationViewModel viewModel;
    private NotificationAdapter adapter;


    public NotificationsFragment() { }

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        adapter = new NotificationAdapter(new ArrayList<>());
        binding.notificationsRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeNotifications();
        markNotificationsAsSeen();
    }

    private void observeNotifications() {
        viewModel.getNotifications().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null)
                adapter.setData(result.getData());
            else
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
        });
    }

    private void markNotificationsAsSeen() {
        viewModel.markNotificationsAsSeen().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() != null)
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
        } );
    }
}