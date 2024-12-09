package com.eventorium.presentation.company.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentEditCompanyBinding;

public class EditCompanyFragment extends Fragment {

    private FragmentEditCompanyBinding binding;

    public EditCompanyFragment() { }

    public static EditCompanyFragment newInstance() {
        return new EditCompanyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditCompanyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}