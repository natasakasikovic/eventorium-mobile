package com.eventorium.presentation.solution.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.solution.listeners.OnManageListener;


public class ManageableServiceAdapter extends PagedListAdapter<ServiceSummary, ManageableServiceAdapter.ManageableServiceViewHolder> {

    private final OnManageListener<ServiceSummary> manageListener;
    private final ImageSourceProvider<ServiceSummary> imageSourceProvider;
    private final ImageLoader imageLoader;

    public ManageableServiceAdapter(
            ImageLoader imageLoader,
            ImageSourceProvider<ServiceSummary> imageSourceProvider,
            OnManageListener<ServiceSummary> listener
    ) {
        super(DIFF_CALLBACK);
        this.manageListener = listener;
        this.imageLoader = imageLoader;
        this.imageSourceProvider = imageSourceProvider;
    }

    @NonNull
    @Override
    public ManageableServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manageable_service_card, parent, false);
        return new ManageableServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageableServiceViewHolder holder, int position) {
        ServiceSummary service = getItem(position);
        if (service != null) {
            holder.bind(service);
        }
    }

    public class ManageableServiceViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;

        Button seeMoreButton;
        Button editButton;
        Button deleteButton;

        public ManageableServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.service_name);
            priceTextView = itemView.findViewById(R.id.service_price);
            imageView = itemView.findViewById(R.id.service_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(ServiceSummary serviceSummary) {
            nameTextView.setText(serviceSummary.getName());
            double price = serviceSummary.getPrice() * (1 - serviceSummary.getDiscount() / 100.0);
            priceTextView.setText(String.format("%.2f", price));

            imageLoader.loadImage(
                    imageSourceProvider.getImageSource(serviceSummary),
                    imageView
            );

            seeMoreButton.setOnClickListener(v -> manageListener.onSeeMoreClick(serviceSummary));
            editButton.setOnClickListener(v -> manageListener.onEditClick(serviceSummary));
            deleteButton.setOnClickListener(v -> manageListener.onDeleteClick(serviceSummary));
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
                    return oldItem.getId().equals(newItem.getId());
                }
            };
}

