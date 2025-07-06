package com.eventorium.presentation.solution.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.service.Reservation;
import com.eventorium.presentation.category.listeners.OnManualReservationListener;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManualReservationAdapter extends RecyclerView.Adapter<ManualReservationAdapter.ManualReservationViewHolder>{

    private  List<Reservation> reservations;
    private final OnManualReservationListener listener;

    public ManualReservationAdapter(List<Reservation> reservations, OnManualReservationListener listener) {
        this.reservations = reservations;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ManualReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manual_reservation_card, parent, false);
        return new ManualReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManualReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public void setData(List<Reservation> data) {
        this.reservations = data;
        notifyDataSetChanged();
    }

    public void removeReservation(Long id) {
        int position = -1;
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId().equals(id)) {
                position = i;
                reservations.remove(i);
                break;
            }
        }
        if (position != -1)
            notifyItemRemoved(position);
    }

    public class ManualReservationViewHolder extends RecyclerView.ViewHolder {

        Button acceptButton, declineButton, serviceButton, eventButton;
        TextView eventTextView, serviceTextView, timeTextView, dateTextView;

        public ManualReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            serviceButton = itemView.findViewById(R.id.serviceButton);
            serviceTextView = itemView.findViewById(R.id.serviceText);
            eventButton = itemView.findViewById(R.id.eventButton);
            eventTextView = itemView.findViewById(R.id.eventText);
            timeTextView = itemView.findViewById(R.id.timeText);
            dateTextView = itemView.findViewById(R.id.dateText);
        }

        public void bind(Reservation reservation) {
            bindText(reservation);
            initializeListeners(reservation);
        }

        private void bindText(Reservation reservation) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/yy");

            serviceTextView.setText(reservation.getServiceName());
            eventTextView.setText(reservation.getEventName());
            timeTextView.setText(reservation.getStartingTime().equals(reservation.getEndingTime())
                    ? reservation.getStartingTime()
                    : reservation.getStartingTime() + " - " + reservation.getEndingTime());
            dateTextView.setText(reservation.getDate().format(dateFormatter));
        }
        private void initializeListeners(Reservation reservation) {
            acceptButton.setOnClickListener(v -> listener.acceptReservation(reservation.getId()));
            declineButton.setOnClickListener(v -> listener.declineReservation(reservation.getId()));
            eventButton.setOnClickListener(v -> listener.navigateToEvent(reservation.getEventId()));
            serviceButton.setOnClickListener(v -> listener.navigateToService(reservation.getServiceId()));
        }
    }
}
