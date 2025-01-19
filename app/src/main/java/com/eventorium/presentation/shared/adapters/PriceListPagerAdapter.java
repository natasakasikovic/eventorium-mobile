package com.eventorium.presentation.shared.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eventorium.presentation.solution.fragments.pricelist.ProductPriceListFragment;
import com.eventorium.presentation.solution.fragments.pricelist.ServicePriceListFragment;

public class PriceListPagerAdapter extends FragmentStateAdapter {

    public PriceListPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return ProductPriceListFragment.newInstance();
        }
        return ServicePriceListFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
