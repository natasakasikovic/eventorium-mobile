package com.eventorium.presentation.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eventorium.presentation.fragments.event.FavouriteEventsFragment;

public class FavouritesPagerAdapter extends FragmentStateAdapter {

    public FavouritesPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FavouriteEventsFragment(); // Fragment za događaje
//            case 1:
//                return new ServicesFragment(); // Fragment za usluge
//            case 2:
//                return new ProductsFragment(); // Fragment za proizvode
            default:
                return new FavouriteEventsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

