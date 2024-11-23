package com.eventorium.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class EventInvitationAdapter extends RecyclerView.Adapter<EventInvitationAdapter.EmailViewHolder >{

    private final List<String> emails;
    public EventInvitationAdapter () { emails = new ArrayList<>(); }

    @NonNull
    @Override
    public EventInvitationAdapter.EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_chip, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder  holder, int position) {
        holder.chip.setText(emails.get(position));

        holder.chip.setOnCloseIconClickListener(v -> {
            emails.remove(emails.get(position));
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, emails.size());
        });
    }

    @Override
    public int getItemCount() { return emails.size(); }

    public boolean updateRecycleView(String email){
        boolean exists = emails.contains(email.trim());

        if (!exists) {
            emails.add(email);
            notifyItemInserted(emails.size() - 1);
        }

        return exists;
    }

    public static class EmailViewHolder extends RecyclerView.ViewHolder {
        private final Chip chip;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.input_chip);
        }
    }

}
