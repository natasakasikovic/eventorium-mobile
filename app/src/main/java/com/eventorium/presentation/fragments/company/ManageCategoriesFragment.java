package com.eventorium.presentation.fragments.company;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentManageCategoriesBinding;


public class ManageCategoriesFragment extends Fragment {

    private FragmentManageCategoriesBinding binding;

    public ManageCategoriesFragment() { }

    public static ManageCategoriesFragment newInstance() {
        return new ManageCategoriesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}