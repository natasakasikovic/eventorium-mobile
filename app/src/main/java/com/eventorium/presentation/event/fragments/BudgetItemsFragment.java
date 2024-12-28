package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.event.fragments.BudgetPlanningFragment.ARG_EVENT_ID;
import static com.eventorium.presentation.event.fragments.BudgetPlanningFragment.ARG_EVENT_TYPE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.event.models.EventType;
import com.eventorium.databinding.FragmentBudgetItemsBinding;
import com.eventorium.databinding.FragmentBudgetPlanningBinding;


public class BudgetItemsFragment extends Fragment {

    private FragmentBudgetItemsBinding binding;
    private EventType eventType;
    private Long eventId;

    public BudgetItemsFragment() {
    }

    public static BudgetItemsFragment newInstance(EventType eventType, Long eventId) {
        BudgetItemsFragment fragment = new BudgetItemsFragment();
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
        binding = FragmentBudgetItemsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}