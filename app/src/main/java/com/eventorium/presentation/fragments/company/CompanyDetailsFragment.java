package com.eventorium.presentation.fragments.company;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentCompanyDetailsBinding;

public class CompanyDetailsFragment extends Fragment {
    private FragmentCompanyDetailsBinding binding;

    public CompanyDetailsFragment() { }

    public static CompanyDetailsFragment newInstance() {
        return new CompanyDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompanyDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}