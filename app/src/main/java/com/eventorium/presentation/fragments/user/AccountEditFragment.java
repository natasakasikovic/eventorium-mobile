package com.eventorium.presentation.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.databinding.FragmentAccountEditBinding;
import com.eventorium.databinding.FragmentPasswordEditBinding;

public class AccountEditFragment extends Fragment {

    private FragmentAccountEditBinding binding;

    public AccountEditFragment () { }

    public static AccountEditFragment newInstance(String param1, String param2) { return new AccountEditFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.changePasswordButton.setOnClickListener(v -> {
            showChangePasswordDialog();
        });
    }

    public void showChangePasswordDialog() {
        PasswordEditFragment dialogFragment = PasswordEditFragment.newInstance();
        dialogFragment.show(getParentFragmentManager(), "PasswordEditDialog");
    }

}