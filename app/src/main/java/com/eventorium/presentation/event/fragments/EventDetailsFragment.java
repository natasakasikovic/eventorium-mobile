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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.Activity;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.databinding.FragmentEventDetailsBinding;
import com.eventorium.presentation.auth.viewmodels.AuthViewModel;
import com.eventorium.presentation.interaction.fragments.chat.ChatFragment;
import com.eventorium.presentation.event.adapters.ActivitiesAdapter;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.user.fragments.UserProfileFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;
    public static String ARG_EVENT_ID = "event_id";
    private EventViewModel viewModel;
    private AuthViewModel authViewModel;
    private Long id;
    private EventDetails event;
    private UserDetails organizer;
    private RecyclerView agenda;
    private ActivitiesAdapter adapter;
    private boolean isFavourite;
    private MaterialButton favButton;
    private Button addToCalendarBtn;
    private Button exportDetailsBtn;
    private Button exportGuestListBtn;

    public EventDetailsFragment() { }

    public static EventDetailsFragment newInstance(Long id) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, id);
        fragment.setArguments(args);
        return fragment;
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
        setUpViewModels();
        setUpButtons();
        adjustUIVisibility();
        loadAgenda();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadEventDetails();
    }

    private void setUpViewModels() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(EventViewModel.class);
        authViewModel = provider.get(AuthViewModel.class);
    }

    private void setUpButtons() {
        binding.chatButton.setOnClickListener(v -> navigateToChat());
        favButton = binding.favButton;
        addToCalendarBtn = binding.btnAddToCalendar;
        exportDetailsBtn = binding.btnExportDetails;
        exportGuestListBtn = binding.btnExportGuests;
        binding.organizerBtn.setOnClickListener(v -> navigateToOrganizer());
    }

    private void adjustUIVisibility() {
        if (authViewModel.getUserId() == null) {
            hideViews(binding.actions, binding.question, binding.chatButton, binding.exportGuests);
            return;
        }

        if (authViewModel.getUserId().equals(id))
            hideViews(binding.chatButton, binding.organizerBtn, binding.organizerName);
        else
            hideViews(binding.exportGuests);
    }

    private void hideViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    private void navigateToChat() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putParcelable(ChatFragment.ARG_RECIPIENT, organizer);
        navController.navigate(R.id.chatFragment, args);
    }

    private void navigateToOrganizer() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(UserProfileFragment.ARG_ID, organizer.getId());
        navController.navigate(R.id.action_event_details_to_organizer, args);
    }

    private void loadEventDetails() {
        viewModel.getEventDetails(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                event = result.getData();
                setOrganizer();
                fillTextViews();
                setupFavIcon();
                setupFavButton();
                setupAddToCalendarButton();
                setupExportBtns();
            }
            else showError(result.getError());
        });
    }

    @SuppressLint("SetTextI18n")
    private void fillTextViews() {
        binding.eventName.setText(event.getName());
        binding.eventType.setText("Event type: " + event.getEventType());
        binding.privacyType.setText("Privacy type: " + event.getPrivacy());
        binding.description.setText(event.getDescription());
        binding.maxParticipants.setText(event.getMaxParticipants());
        binding.address.setText(event.getAddress());
        binding.city.setText(event.getCity());
        binding.date.setText(event.getDate());
        binding.organizerName.setText(event.getOrganizer().getName() + " " + event.getOrganizer().getLastname());
    }

    private void setOrganizer() {
        organizer = event.getOrganizer();
    }

    private void setupFavIcon() {
        if (authViewModel.getUserId() == null) return;
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
        this.addToCalendarBtn.setOnClickListener(v ->
            viewModel.addToCalendar(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null)
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.activation_dialog_title)
                        .setMessage(R.string.added_to_calendar)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            else showError(result.getError());
        }));
    }

    private void addToFavourites() {
        viewModel.addToFavourites(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() != null)
                showError(result.getError());
            else {
                favButton.setIconResource(R.drawable.ic_favourite);
                isFavourite = true;
            }
        });
    }

    private void removeFromFavourites() {
        viewModel.removeFromFavourites(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() != null)
                showError(result.getError());
            else {
                favButton.setIconResource(R.drawable.ic_not_favourite);
                isFavourite = false;
            }
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
            } else showError(result.getError());
        });
        SnapHelper snapHelperEvents = new LinearSnapHelper();
        snapHelperEvents.attachToRecyclerView(binding.agenda);
    }

    private void setupExportBtns() {
        this.exportDetailsBtn.setOnClickListener(v ->
            viewModel.exportToPdf(id, getContext()).observe(getViewLifecycleOwner(), result -> {
                if (result.getData() != null) openPdf(result.getData());
                else showError(result.getError());
            })
        );
        this.exportGuestListBtn.setOnClickListener(v ->
            viewModel.exportGuestListToPdf(id, getContext()).observe(getViewLifecycleOwner(), result -> {
                if (result.getData() != null) openPdf(result.getData());
                else showError(result.getError());
            })
        );
    }

    private void openPdf(Uri pdfUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}