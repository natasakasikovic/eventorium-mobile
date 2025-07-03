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
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.shared.models.ImageHolder;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.presentation.shared.utils.ImageLoader;

import java.util.List;

public class ServicesAdapter extends PagedListAdapter<ServiceSummary, ServicesAdapter.ServiceViewHolder> {

    private final OnSeeMoreClick<ServiceSummary> listener;
    private final ImageLoader imageLoader;
    private final ImageSourceProvider<ServiceSummary> imageSourceProvider;

    public ServicesAdapter(
            ImageLoader imageLoader,
            ImageSourceProvider<ServiceSummary> imageSourceProvider,
            OnSeeMoreClick<ServiceSummary> listener
    ) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.imageLoader = imageLoader;
        this.imageSourceProvider = imageSourceProvider;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceSummary service = getItem(position);
        if (service != null) {
            holder.bind(service);
        }
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

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

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(ServiceSummary service) {
            float alpha = service.getAvailable() ? 1f : 0.5f;
            layout.setAlpha(alpha);

            setDiscount(service);

            nameTextView.setText(service.getName());
            double price = service.getPrice() * (1 - service.getDiscount() / 100.0);
            priceTextView.setText(String.format("%.2f", price));

            imageLoader.loadImage(
                    ImageHolder.SERVICE,
                    service.getId(),
                    imageSourceProvider.getImageSource(service),
                    photoImageview
            );

            seeMoreButton.setOnClickListener(v -> listener.navigateToDetails(service));
        }

        @SuppressLint("SetTextI18n")
        private void setDiscount(ServiceSummary service) {
            if (service.getDiscount() != null && service.getDiscount() > 0) {
                discountTextView.setVisibility(View.VISIBLE);
                discountTextView.setText(service.getDiscount() + "% OFF");
            } else {
                discountTextView.setVisibility(View.GONE);
            }
        }
    }

    private static final DiffUtil.ItemCallback<ServiceSummary> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull ServiceSummary oldItem, @NonNull ServiceSummary newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull ServiceSummary oldItem, @NonNull ServiceSummary newItem) {
                    return oldItem.equals(newItem);
                }
            };
}

