package com.eventorium.presentation.favourites.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eventorium.presentation.favourites.fragments.FavouriteEventsFragment;
import com.eventorium.presentation.favourites.fragments.FavouriteProductsFragment;
import com.eventorium.presentation.favourites.fragments.FavouriteServicesFragment;

public class FavouritesPagerAdapter extends FragmentStateAdapter {

    public FavouritesPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 0 -> FavouriteEventsFragment.newInstance();
            case 1 -> FavouriteServicesFragment.newInstance();
            case 2 -> FavouriteProductsFragment.newInstance();
            default -> new FavouriteEventsFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

