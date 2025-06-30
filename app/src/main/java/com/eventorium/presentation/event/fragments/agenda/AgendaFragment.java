package com.eventorium.presentation.event.fragments.agenda;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.event.Activity;
import com.eventorium.data.event.models.Privacy;
import com.eventorium.databinding.FragmentAgendaBinding;
import com.eventorium.presentation.event.adapters.ActivitiesAdapter;
import com.eventorium.presentation.event.listeners.OnActivityCreatedListener;
import com.eventorium.presentation.event.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AgendaFragment extends Fragment implements OnActivityCreatedListener  {
    private FragmentAgendaBinding binding;
    private OnBackPressedCallback onBackPressedCallback;

    private List<Activity> activities = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;
    private EventViewModel eventViewModel;
    private Long id;
    private Privacy privacy;

    public static String ARG_EVENT_ID = "ARG_EVENT_ID";
    public static String ARG_EVENT_PRIVACY = "ARG_EVENT_PRIVACY";

    public AgendaFragment() { }

    public static AgendaFragment newInstance(Long eventId, Privacy privacy) {
        AgendaFragment fragment = new AgendaFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, eventId);
        args.putParcelable(ARG_EVENT_PRIVACY, privacy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_EVENT_ID);
            privacy = getArguments().getParcelable(ARG_EVENT_PRIVACY);
        }
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        setUpOnBackPressedHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAgendaBinding.inflate(inflater, container, false);
        binding.add.setOnClickListener(v->openAddActivityDialog());
        recyclerView = binding.recyclerView;
        adapter = new ActivitiesAdapter(activities, activity -> {
            activities.remove(activity);
        });
        recyclerView.setAdapter(adapter);
        binding.continueButton.setOnClickListener(v -> nextStep());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Activity activity) {
        activities.add(activity);
        adapter.notifyItemInserted(activities.size() - 1);
    }

    private void openAddActivityDialog() {
        AddActivityDialogFragment dialog = new AddActivityDialogFragment();
        dialog.show(getChildFragmentManager(), "AddActivityDialog");
    }

    private void nextStep() {
        eventViewModel.createAgenda(id, activities).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) navigate();
            else new AlertDialog.Builder(requireContext())
                        .setMessage(result.getError())
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();

        });
    }

    private void navigate() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);

        if (onBackPressedCallback != null) {
            onBackPressedCallback.setEnabled(false);
        }

        if (privacy.equals(Privacy.CLOSED)) {
            Bundle args = new Bundle();
            args.putLong(ARG_EVENT_ID, id);
            navController.navigate(R.id.action_agenda_to_invitations, args);
        } else {
            Toast.makeText(requireContext(), "Event created successfully", Toast.LENGTH_SHORT).show();
            navController.popBackStack(R.id.homepageFragment, false);
        }
    }

    private void setUpOnBackPressedHandler() {
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void showExitConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.exit_event_creation)
                .setMessage(R.string.exit_event_creation_confirmation)
                .setPositiveButton("Exit", (dialog, which) -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    navController.popBackStack(navController.getGraph().getStartDestinationId(), false);
                })
                .setNegativeButton("Stay", (dialog, which) -> dialog.dismiss())
                .show();
    }

}