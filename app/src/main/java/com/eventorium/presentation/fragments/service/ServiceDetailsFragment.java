package com.eventorium.presentation.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentManageServiceBinding;
import com.eventorium.databinding.FragmentServiceDetailsBinding;

public class ServiceDetailsFragment extends Fragment {

    private FragmentManageServiceBinding binding;

    public ServiceDetailsFragment() {
    }

    public static ServiceDetailsFragment newInstance(String param1, String param2) {
        return new ServiceDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageServiceBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_service_details, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}