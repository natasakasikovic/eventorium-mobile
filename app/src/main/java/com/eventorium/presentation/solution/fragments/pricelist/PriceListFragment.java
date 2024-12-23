package com.eventorium.presentation.solution.fragments.pricelist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentPriceListBinding;
import com.eventorium.presentation.solution.adapters.PriceListPagerAdapter;
import com.eventorium.presentation.util.adapters.FavouritesPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PriceListFragment extends Fragment {

    private FragmentPriceListBinding binding;
    private PriceListPagerAdapter adapter;

    public PriceListFragment() {
    }

    public static PriceListFragment newInstance() {
        return new PriceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPriceListBinding.inflate(inflater, container, false);
        adapter = new PriceListPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Products");
                    break;
                case 1:
                    tab.setText("Services");
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