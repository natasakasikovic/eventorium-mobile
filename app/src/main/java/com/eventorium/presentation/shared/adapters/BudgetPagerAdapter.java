package com.eventorium.presentation.shared.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eventorium.data.event.models.event.Event;
import com.eventorium.presentation.event.fragments.budget.BudgetItemsFragment;
import com.eventorium.presentation.event.fragments.budget.BudgetItemsListFragment;

public class BudgetPagerAdapter extends FragmentStateAdapter {

    private final Event event;
    public BudgetPagerAdapter(@NonNull Fragment fragment, Event event) {
        super(fragment);
        this.event = event;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return BudgetItemsFragment.newInstance(event);
        }
        return BudgetItemsListFragment.newInstance(event, true);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
