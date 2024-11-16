package com.eventorium.presentation.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentEditServiceBinding;

public class EditServiceFragment extends Fragment {

    private FragmentEditServiceBinding binding;

    public EditServiceFragment() {
    }

    public static EditServiceFragment newInstance() {
        return new EditServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditServiceBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_edit_service, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}