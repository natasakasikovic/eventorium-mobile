package com.eventorium.presentation.event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.Invitation;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventInvitationAdapter extends RecyclerView.Adapter<EventInvitationAdapter.EmailViewHolder >{

    private final List<Invitation> invitations;

    public EventInvitationAdapter() {
        invitations = new ArrayList<>();
    }

    @NonNull
    @Override
    public EventInvitationAdapter.EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_chip, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder  holder, int position) {
        Invitation invitation = invitations.get(position);
        holder.chip.setText(invitation.getEmail());

        holder.chip.setOnCloseIconClickListener(v -> {
            invitations.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, invitations.size());
        });
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public boolean updateRecycleView(String email) {
        boolean exists = invitations.stream().anyMatch(invitation -> invitation.getEmail().equalsIgnoreCase(email.trim()));

        if (exists) return true;

        invitations.add(new Invitation(email));
        notifyItemInserted(invitations.size() - 1);
        return false;
    }

    public static class EmailViewHolder extends RecyclerView.ViewHolder {
        private final Chip chip;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.input_chip);
        }
    }
}
