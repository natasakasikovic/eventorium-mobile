package com.eventorium.presentation.solution.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eventorium.R;
import com.eventorium.presentation.util.listeners.OnSeeMoreClick;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;

import java.util.List;

public class ServicesAdapter extends BaseServiceAdapter<ServicesAdapter.ServiceViewHolder> {

    private final OnSeeMoreClick<ServiceSummary> listener;

    public ServicesAdapter(List<ServiceSummary> serviceSummaries, OnSeeMoreClick<ServiceSummary> listener) {
        super(serviceSummaries);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceSummary service = serviceSummaries.get(position);
        holder.bind(service);
    }

    public class ServiceViewHolder extends BaseServiceViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView discountTextView;
        ImageView photoImageview;
        Button seeMoreButton;
        LinearLayout layout;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.service_name);
            priceTextView = itemView.findViewById(R.id.service_price);
            discountTextView = itemView.findViewById(R.id.service_discount);
            photoImageview = itemView.findViewById(R.id.service_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            layout = itemView.findViewById(R.id.service_layout);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ServiceSummary service) {

            float alpha = service.getAvailable() ? 1f : 0.5f;
            layout.setAlpha(alpha);

            setDiscount(service);

            nameTextView.setText(service.getName());
            priceTextView.setText(service.getPrice().toString());
            photoImageview.setImageBitmap(service.getImage());
            seeMoreButton.setOnClickListener(v -> listener.navigateToDetails(service));

        }

        @SuppressLint("SetTextI18n")
        private void setDiscount(ServiceSummary service){
            if (hasDiscount(service)) {
                discountTextView.setVisibility(View.VISIBLE);
                discountTextView.setText(service.getDiscount().toString() + "% OFF");
            } else {
                discountTextView.setVisibility(View.GONE);
            }
        }

        private boolean hasDiscount(ServiceSummary service) {
            return service.getDiscount() != null && service.getDiscount() > 0;
        }

    }
}
