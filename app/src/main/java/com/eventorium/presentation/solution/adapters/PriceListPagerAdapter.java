package com.eventorium.presentation.solution.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eventorium.presentation.event.fragments.FavouriteEventsFragment;

public class PriceListPagerAdapter extends FragmentStateAdapter {

    public PriceListPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            default -> new FavouriteEventsFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
