package com.eventorium.presentation.solution.fragments.pricelist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.databinding.FragmentPriceListBinding;
import com.eventorium.presentation.solution.viewmodels.PriceListViewModel;
import com.eventorium.presentation.shared.adapters.PriceListPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PriceListFragment extends Fragment {

    private FragmentPriceListBinding binding;
    private PriceListPagerAdapter adapter;
    private PriceListViewModel priceListViewModel;

    public PriceListFragment() {
    }

    public static PriceListFragment newInstance() {
        return new PriceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        priceListViewModel = new ViewModelProvider(this).get(PriceListViewModel.class);
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

        binding.downloadPdf.setOnClickListener(v ->
            priceListViewModel.downloadPdf(getContext()).observe(getViewLifecycleOwner(), pdfFile -> {
                if (pdfFile.getError() == null) {
                    Toast.makeText(requireContext(), "PDF downloaded to " + pdfFile.getData().getPath(), Toast.LENGTH_LONG).show();
                    openPdf(pdfFile.getData());
                } else {
                    Toast.makeText(requireContext(), pdfFile.getError(), Toast.LENGTH_SHORT).show();
                }
            })
        );

        return binding.getRoot();
    }

    private void openPdf(Uri pdfUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}