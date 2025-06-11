package com.eventorium.presentation.event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.invitation.InvitationDetails;
import com.eventorium.presentation.event.listeners.OnAddToCalendarClick;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class UserInvitationsAdapter extends RecyclerView.Adapter<UserInvitationsAdapter.InvitationViewHolder> {

    private List<InvitationDetails> invitations;
    private final OnSeeMoreClick<InvitationDetails> seeMoreListener;
    private final OnAddToCalendarClick addToCalendarListener;

    public UserInvitationsAdapter(List<InvitationDetails> invitations,
                                  OnSeeMoreClick<InvitationDetails> seeMoreListener,
                                  OnAddToCalendarClick addToCalendarListener) {
        this.invitations = invitations;
        this.seeMoreListener = seeMoreListener;
        this.addToCalendarListener = addToCalendarListener;
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_card, parent, false);
        return new InvitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        InvitationDetails invitation = invitations.get(position);
        holder.eventName.setText(invitation.getEventName());
        holder.eventDate.setText(invitation.getEventDate());
        holder.btnIWillCome.setOnClickListener(v -> {
            addToCalendarListener.onAddToCalendar(invitation);
        });
        holder.btnViewEvent.setOnClickListener(v -> {
            seeMoreListener.navigateToDetails(invitation);
        });
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public void setData(List<InvitationDetails> data) {
        invitations = data;
        notifyDataSetChanged();
    }

    public static class InvitationViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventDate;

        MaterialButton btnIWillCome;
        MaterialButton btnViewEvent;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            btnIWillCome = itemView.findViewById(R.id.btnIWillCome);
            btnViewEvent = itemView.findViewById(R.id.btnViewEvent);
        }
    }
}
