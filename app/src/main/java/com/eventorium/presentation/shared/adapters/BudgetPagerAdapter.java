package com.eventorium.presentation.shared.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eventorium.data.event.models.EventType;
import com.eventorium.presentation.event.fragments.BudgetItemsFragment;

public class BudgetPagerAdapter extends FragmentStateAdapter {

    private final EventType eventType;
    private final Long eventId;
    public BudgetPagerAdapter(@NonNull Fragment fragment, EventType eventType, Long eventId) {
        super(fragment);
        this.eventType = eventType;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // TODO: change when purchase or reservation is added
        return BudgetItemsFragment.newInstance(eventType, eventId);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
