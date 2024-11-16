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

public class ServiceDetailsFragment extends Fragment {

    private FragmentManageServiceBinding binding;

    public static final String ARG_SERVICE = "service";
    private Service service;

    public ServiceDetailsFragment() {
    }

    public static ServiceDetailsFragment newInstance(Service service) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SERVICE, service);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            service = getArguments().getParcelable(ARG_SERVICE);
        }
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