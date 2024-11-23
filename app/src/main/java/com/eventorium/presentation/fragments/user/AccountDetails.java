package com.eventorium.presentation.fragments.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentAccountDetailsBinding;
import com.eventorium.databinding.FragmentLoginBinding;

public class AccountDetails extends Fragment {

    private FragmentAccountDetailsBinding binding;

    public AccountDetails() { }

    public static AccountDetails newInstance() {
        return new AccountDetails();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}