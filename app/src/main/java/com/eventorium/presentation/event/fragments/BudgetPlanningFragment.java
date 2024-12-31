package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventType;
import com.eventorium.databinding.FragmentBudgetPlanningBinding;
import com.eventorium.presentation.util.adapters.BudgetPagerAdapter;
import com.eventorium.presentation.util.adapters.FavouritesPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

public class BudgetPlanningFragment extends Fragment {

    private FragmentBudgetPlanningBinding binding;
    public static final String ARG_EVENT_TYPE = "ARG_EVENT_TYPE";
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private Long eventId;
    private EventType eventType;
    private BudgetPagerAdapter adapter;

    public BudgetPlanningFragment() {
    }

    public static BudgetPlanningFragment newInstance(EventType eventType, Long eventId) {
        BudgetPlanningFragment fragment = new BudgetPlanningFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT_TYPE, eventType);
        args.putLong(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            eventId = getArguments().getLong(ARG_EVENT_ID);
            eventType = getArguments().getParcelable(ARG_EVENT_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetPlanningBinding.inflate(inflater, container, false);
        adapter = new BudgetPagerAdapter(this, eventType, eventId);
        binding.viewPager.setAdapter(adapter);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}