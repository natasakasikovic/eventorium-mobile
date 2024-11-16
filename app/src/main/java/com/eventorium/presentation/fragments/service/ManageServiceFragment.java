package com.eventorium.presentation.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.models.Service;
import com.eventorium.databinding.FragmentManageServiceBinding;

import java.util.ArrayList;
import java.util.List;


public class ManageServiceFragment extends Fragment {

    private FragmentManageServiceBinding binding;
    private static List<Service> services = new ArrayList<>();

    public ManageServiceFragment() {
    }
    public static ManageServiceFragment newInstance() {
        return new ManageServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageServiceBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_manage_service, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}