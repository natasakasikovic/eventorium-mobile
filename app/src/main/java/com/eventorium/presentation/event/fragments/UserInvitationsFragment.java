package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.event.fragments.EventDetailsFragment.ARG_EVENT_ID;

import android.app.AlertDialog;
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
import com.eventorium.databinding.FragmentUserInvitationsBinding;
import com.eventorium.presentation.event.adapters.UserInvitationsAdapter;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.event.viewmodels.InvitationViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserInvitationsFragment extends Fragment {

    private FragmentUserInvitationsBinding binding;
    private InvitationViewModel viewModel;
    private EventViewModel eventViewModel;
    private UserInvitationsAdapter adapter;

    public UserInvitationsFragment() { }

    public static UserInvitationsFragment newInstance() {
        return new UserInvitationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserInvitationsBinding.inflate(inflater, container, false);
        setUpViewModels();
        setUpAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadInvitations();
    }

    private void setUpViewModels() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(InvitationViewModel.class);
        eventViewModel = provider.get(EventViewModel.class);
    }

    private void setUpAdapter() {
        adapter = new UserInvitationsAdapter(new ArrayList<>(),
                invitation -> navigateToEventDetails(invitation.getEventId()),
                invitation -> addToCalendar(invitation.getEventId()));
        binding.rvInvitations.setAdapter(adapter);
    }

    private void loadInvitations() {
        viewModel.getInvitations().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null)
                adapter.setData(result.getData());
            else
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToEventDetails(Long eventId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, eventId);
        navController.navigate(R.id.action_invitations_to_event_details, args);
    }

    private void addToCalendar(Long eventId) {
        eventViewModel.addToCalendar(eventId).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null)
                showSuccessMessage();
            else
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
        });
    }

    private void showSuccessMessage() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.activation_dialog_title)
                .setMessage(R.string.added_to_calendar)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}