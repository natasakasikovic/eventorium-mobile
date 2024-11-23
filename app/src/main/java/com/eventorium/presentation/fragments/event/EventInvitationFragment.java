package com.eventorium.presentation.fragments.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.models.Event;
import com.eventorium.databinding.FragmentEventInvitationBinding;
import com.eventorium.presentation.adapters.EventInvitationAdapter;

import java.util.regex.Pattern;

public class EventInvitationFragment extends Fragment {

    private FragmentEventInvitationBinding binding;
    private EventInvitationAdapter invitationAdapter;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    public EventInvitationFragment() { }

    public static EventInvitationFragment newInstance() {
        return new EventInvitationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventInvitationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
        setUpListeners();
    }

    private void setUpAdapter(){
        RecyclerView recyclerView = binding.recyclerView;
        invitationAdapter = new EventInvitationAdapter();
        recyclerView.setAdapter(invitationAdapter);
    }

    private void setUpListeners(){
        binding.addEmailButton.setOnClickListener(v ->
        {
            String email = binding.emailEditText.getText().toString();
            if (!isEmailValid(email)) return;

            boolean emailExists = invitationAdapter.updateRecycleView(email);
            if (emailExists) {
                binding.emailInputLayout.setError("This email is already added.");
            } else {
                binding.emailInputLayout.setError(null);
                binding.emailEditText.setText("");
            }
        });

        binding.sendInvitationsButton.setOnClickListener( v -> {
            Toast.makeText(getActivity().getApplicationContext(), "NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isEmailValid(String email){
        if (email.isEmpty()){
            binding.emailInputLayout.setError("Email cannot be empty");
            return false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()){
            binding.emailInputLayout.setError("Email format is not good");
            return false;
        }
        return true;
    }

}