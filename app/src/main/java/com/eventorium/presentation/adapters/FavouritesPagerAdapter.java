package com.eventorium.presentation.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eventorium.presentation.fragments.event.FavouriteEventsFragment;
import com.eventorium.presentation.fragments.service.FavouriteServicesFragment;

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
//            case 2:
//                return new FavouriteProductsFragment.newInstance();
            default -> new FavouriteEventsFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

