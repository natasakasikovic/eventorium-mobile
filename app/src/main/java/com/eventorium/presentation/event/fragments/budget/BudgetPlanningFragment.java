package com.eventorium.presentation.event.fragments.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.databinding.FragmentBudgetPlanningBinding;
import com.eventorium.presentation.event.fragments.agenda.AgendaFragment;
import com.eventorium.presentation.shared.adapters.BudgetPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

public class BudgetPlanningFragment extends Fragment {

    private FragmentBudgetPlanningBinding binding;
    public static final String ARG_EVENT = "ARG_EVENT";
    public static final String ARG_CAN_ADVANCE = "ARG_CAN_ADVANCE";

    private Event event;
    private boolean canAdvance;

    public BudgetPlanningFragment() {
    }

    public static BudgetPlanningFragment newInstance(Event event, boolean canAdvance) {
        BudgetPlanningFragment fragment = new BudgetPlanningFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        args.putBoolean(ARG_CAN_ADVANCE, canAdvance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable(ARG_EVENT);
            canAdvance = getArguments().getBoolean(ARG_CAN_ADVANCE, true);
        }
        setUpOnBackPressedHandler();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetPlanningBinding.inflate(inflater, container, false);
        BudgetPagerAdapter adapter = new BudgetPagerAdapter(this, event);
        binding.viewPager.setAdapter(adapter);
        if(canAdvance)
            setUpListener();

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Planning");
                    break;
                case 1:
                    tab.setText("Purchased & Reserved");
                    break;
            }
        }).attach();

        return binding.getRoot();
    }

    private void setUpListener() {
        binding.continueButton.setVisibility(View.VISIBLE);
        binding.continueButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(AgendaFragment.ARG_EVENT_ID, event.getId());
            args.putParcelable(AgendaFragment.ARG_EVENT_PRIVACY, event.getPrivacy());
            navController.navigate(R.id.action_budget_to_agenda, args);
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}