package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.models.Privacy;
import com.eventorium.databinding.FragmentBudgetPlanningBinding;
import com.eventorium.presentation.shared.adapters.BudgetPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

public class BudgetPlanningFragment extends Fragment {

    private FragmentBudgetPlanningBinding binding;
    public static final String ARG_EVENT_TYPE = "ARG_EVENT_TYPE";
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";
    public static final String ARG_EVENT_PRIVACY = "ARG_EVENT_PRIVACY";

    private Long eventId;
    private EventType eventType;
    private Privacy privacy;
    private BudgetPagerAdapter adapter;

    public BudgetPlanningFragment() {
    }

    public static BudgetPlanningFragment newInstance(EventType eventType, Long eventId, Privacy privacy) {
        BudgetPlanningFragment fragment = new BudgetPlanningFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT_TYPE, eventType);
        args.putLong(ARG_EVENT_ID, eventId);
        args.putParcelable(ARG_EVENT_PRIVACY, privacy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            eventId = getArguments().getLong(ARG_EVENT_ID);
            eventType = getArguments().getParcelable(ARG_EVENT_TYPE);
            privacy = getArguments().getParcelable(ARG_EVENT_PRIVACY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetPlanningBinding.inflate(inflater, container, false);
        adapter = new BudgetPagerAdapter(this, eventType, eventId);
        binding.viewPager.setAdapter(adapter);
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
        binding.continueButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ARG_EVENT_ID, eventId);
            args.putParcelable(ARG_EVENT_PRIVACY, privacy);
            navController.navigate(R.id.action_budget_to_agenda, args);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}