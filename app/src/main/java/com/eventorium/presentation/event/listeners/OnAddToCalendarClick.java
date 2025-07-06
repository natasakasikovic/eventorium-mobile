package com.eventorium.presentation.event.listeners;

import com.eventorium.data.event.models.invitation.InvitationDetails;

public interface OnAddToCalendarClick {
    void onAddToCalendar(InvitationDetails invitation);
}
