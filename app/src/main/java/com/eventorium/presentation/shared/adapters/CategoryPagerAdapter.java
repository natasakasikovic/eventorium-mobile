package com.eventorium.presentation.shared.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public CategoryPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        if(fragmentTitleList.contains(title)) {
            return;
        }
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
        notifyItemInserted(fragmentList.size() - 1);
    }

    public void removeFragment(int position) {
        if (position >= 0 && position < fragmentList.size()) {
            fragmentList.remove(position);
            fragmentTitleList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateFragment(int position, Fragment fragment) {
        if (position >= 0 && position < fragmentList.size()) {
            fragmentList.set(position, fragment);
            notifyItemChanged(position);
        }
    }

    public String getFragmentTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
