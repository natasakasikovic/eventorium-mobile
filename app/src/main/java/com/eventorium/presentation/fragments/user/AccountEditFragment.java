package com.eventorium.presentation.fragments.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;

public class AccountEditFragment extends Fragment {

    public AccountEditFragment() { }

    public static AccountEditFragment newInstance(String param1, String param2) { return new AccountEditFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_edit, container, false);
    }
}