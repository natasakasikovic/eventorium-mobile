package com.eventorium.presentation.event.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.ChatUserDetails;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.interaction.models.MessageSender;
import com.eventorium.databinding.FragmentEventDetailsBinding;
import com.eventorium.presentation.auth.viewmodels.LoginViewModel;
import com.eventorium.presentation.chat.fragments.ChatFragment;
import com.eventorium.presentation.event.viewmodels.EventViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;
    public static String ARG_EVENT_ID = "event_id";
    private EventViewModel viewModel;
    private LoginViewModel loginViewModel;
    private Long id;
    private EventDetails event;
    private MessageSender organizer;

    public EventDetailsFragment() { }

    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
        id = getArguments().getLong(ARG_EVENT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(EventViewModel.class);
        loginViewModel = provider.get(LoginViewModel.class);
        if (!loginViewModel.isLoggedIn()) {
            binding.actions.setVisibility(View.GONE);
            binding.chatButton.setVisibility(View.GONE);
        }
        binding.chatButton.setOnClickListener(v -> navigateToChat());
        return binding.getRoot();
    }

    private void navigateToChat() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putParcelable(ChatFragment.ARG_RECIPIENT, organizer);
        navController.navigate(R.id.chatFragment, args);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadEventDetails();
    }

    @SuppressLint("SetTextI18n")
    private void loadEventDetails() {
        viewModel.getEventDetails(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                event = result.getData();
                ChatUserDetails sender = event.getOrganizer();
                organizer = new MessageSender(sender.getId(), sender.getName(), sender.getLastname());
                binding.eventName.setText(event.getName());
                binding.eventType.setText("Event type: " + event.getEventType());
                binding.privacyType.setText("Privacy type: " + event.getPrivacy());
                binding.description.setText(event.getDescription());
                binding.maxParticipants.setText(event.getMaxParticipants());
                binding.address.setText(event.getAddress());
                binding.city.setText(event.getCity());
                binding.date.setText(event.getDate());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}