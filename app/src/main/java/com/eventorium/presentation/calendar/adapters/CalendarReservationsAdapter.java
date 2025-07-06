package com.eventorium.presentation.calendar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.service.CalendarReservation;

import java.util.List;

public class CalendarReservationsAdapter extends RecyclerView.Adapter<CalendarReservationsAdapter.ViewHolder> {

    private List<CalendarReservation> calendarReservations;

    public CalendarReservationsAdapter(List<CalendarReservation> calendarReservations) {
        this.calendarReservations = calendarReservations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarReservation reservation = calendarReservations.get(position);
        holder.reservationTextView.setText(String.format("%s -> %s", reservation.getServiceName(), reservation.getEventName()));
    }

    @Override
    public int getItemCount() {
        return calendarReservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reservationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reservationTextView = itemView.findViewById(R.id.item_name);
        }
    }
}
