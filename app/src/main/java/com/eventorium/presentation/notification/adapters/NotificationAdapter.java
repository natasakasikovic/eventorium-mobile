package com.eventorium.presentation.notification.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.data.notification.models.NotificationResponse;
import com.eventorium.R;
import com.eventorium.data.shared.utils.DateTimeUtils;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationResponse> notifications;

    public NotificationAdapter(List<NotificationResponse> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationResponse notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setData(List<NotificationResponse> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        TextView timestampTextView;
        LinearLayout layout;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message);
            timestampTextView = itemView.findViewById(R.id.timestamp);
            layout = itemView.findViewById(R.id.layout);
        }

        public void bind(NotificationResponse notification) {
            messageTextView.setText(notification.getMessage());
            timestampTextView.setText(DateTimeUtils.formatLocalDateTime(notification.getTimestamp()));

            float alpha = notification.getSeen() ? 0.5f : 1f;
            layout.setAlpha(alpha);
        }
    }
}