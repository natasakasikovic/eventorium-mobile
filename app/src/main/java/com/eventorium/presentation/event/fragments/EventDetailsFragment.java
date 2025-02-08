package com.eventorium.presentation.event.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.ChatUserDetails;
import com.eventorium.data.event.models.Activity;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.databinding.FragmentEventDetailsBinding;
import com.eventorium.presentation.auth.viewmodels.LoginViewModel;
import com.eventorium.presentation.chat.fragments.ChatFragment;
import com.eventorium.presentation.event.adapters.ActivitiesAdapter;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;
    public static String ARG_EVENT_ID = "event_id";
    private EventViewModel viewModel;
    private LoginViewModel loginViewModel;
    private Long id;
    private EventDetails event;
    private UserDetails organizer;
    private RecyclerView agenda;
    private ActivitiesAdapter adapter;
    private boolean isFavourite;
    private MaterialButton favButton;
    private Button addToCalendarBtn;
    private Button exportBtn;

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
            binding.question.setVisibility(View.GONE);
        }
        binding.chatButton.setOnClickListener(v -> navigateToChat());
        favButton = binding.favButton;
        addToCalendarBtn = binding.btnAddToCalendar;
        exportBtn = binding.btnExport;
        loadAgenda();
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
                organizer = new UserDetails(sender.getId(), sender.getName(), sender.getLastname());
                binding.eventName.setText(event.getName());
                binding.eventType.setText("Event type: " + event.getEventType());
                binding.privacyType.setText("Privacy type: " + event.getPrivacy());
                binding.description.setText(event.getDescription());
                binding.maxParticipants.setText(event.getMaxParticipants());
                binding.address.setText(event.getAddress());
                binding.city.setText(event.getCity());
                binding.date.setText(event.getDate());
                setupFavIcon();
                setupFavButton();
                setupAddToCalendarButton();
                setupExportBtn();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFavIcon() {
        if (!loginViewModel.isLoggedIn()) return;
        viewModel.isFavourite(id).observe(getViewLifecycleOwner(), isFav -> {
            isFavourite = isFav;
            favButton.setIconResource(isFavourite ? R.drawable.ic_favourite : R.drawable.ic_not_favourite);
        });
    }

    private void setupFavButton() {
        favButton.setOnClickListener(v -> {
            if (isFavourite) removeFromFavourites();
            else addToFavourites();
        });
    }

    private void setupAddToCalendarButton() {
        this.addToCalendarBtn.setOnClickListener(v -> {
            viewModel.addToCalendar(id).observe(getViewLifecycleOwner(), result -> {
                if (result.getError() == null) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle(R.string.activation_dialog_title)
                            .setMessage(R.string.added_to_calendar)
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupExportBtn() {
        this.exportBtn.setOnClickListener(v -> {
            viewModel.exportToPdf(id, getContext()).observe(getViewLifecycleOwner(), result -> {
                if (result.getData() != null) {
                    openPdf(result.getData());
                } else {
                    Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void openPdf(Uri pdfUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void addToFavourites() {
        viewModel.addToFavourites(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() != null) {
                isFavourite = true;
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            } else
                favButton.setIconResource(R.drawable.ic_favourite);

        });
    }

    private void removeFromFavourites() {
        viewModel.removeFromFavourites(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() != null) {
                isFavourite = false;
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            } else
                favButton.setIconResource(R.drawable.ic_not_favourite);
        });
    }

    private void loadAgenda() {
        agenda = binding.agenda;
        viewModel.getAgenda(id).observe(getViewLifecycleOwner(), result -> {
            List<Activity> activities = result.getData();
            if (activities != null) {
                if (activities.isEmpty()) {
                    binding.agendaTitle.setVisibility(View.GONE);
                    binding.agenda.setVisibility(View.GONE);
                } else {
                    adapter = new ActivitiesAdapter(activities, null);
                    adapter.setDeleteButtonVisibility(false);
                    agenda.setAdapter(adapter);
                }
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}