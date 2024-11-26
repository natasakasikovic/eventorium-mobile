package com.eventorium.presentation.fragments.company;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
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