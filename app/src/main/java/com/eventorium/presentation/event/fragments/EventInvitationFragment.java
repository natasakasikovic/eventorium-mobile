package com.eventorium.presentation.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.databinding.FragmentEventInvitationBinding;
import com.eventorium.presentation.event.adapters.EventInvitationAdapter;
import com.eventorium.presentation.event.viewmodels.InvitationViewModel;

import java.util.regex.Pattern;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventInvitationFragment extends Fragment {

    private FragmentEventInvitationBinding binding;
    private EventInvitationAdapter adapter;
    private InvitationViewModel viewModel;
    private Long eventId;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    public EventInvitationFragment() { }

    public static EventInvitationFragment newInstance() {
        return new EventInvitationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            eventId = getArguments().getLong(ARG_EVENT_ID);

        setUpOnBackPressedHandler();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventInvitationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(InvitationViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new EventInvitationAdapter();
        binding.recyclerView.setAdapter(adapter);

        setUpListeners();
    }

    private void setUpListeners() {
        binding.addEmailButton.setOnClickListener(v ->
        {
            String email = binding.emailEditText.getText().toString();
            if (!isEmailValid(email)) return;

            boolean emailExists = adapter.updateRecycleView(email);
            if (emailExists) {
                binding.emailInputLayout.setError("This email is already added.");
            } else {
                binding.emailInputLayout.setError(null);
                binding.emailEditText.setText("");
            }
        });

        binding.sendInvitationsButton.setOnClickListener( v -> {
            viewModel.sendInvitations(this.eventId, adapter.getInvitations());
        });
    }

    private boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            binding.emailInputLayout.setError("Email cannot be empty");
            return false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            binding.emailInputLayout.setError("Email format is not good");
            return false;
        }
        return true;
    }

    private void setUpOnBackPressedHandler() {
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void showExitConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.exit_event_creation)
                .setMessage(R.string.exit_event_creation_confirmation)
                .setPositiveButton(R.string.exit, (dialog, which) -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    navController.popBackStack(R.id.homepageFragment, false);
                })
                .setNegativeButton(R.string.stay, (dialog, which) -> dialog.dismiss())
                .show();
    }

}