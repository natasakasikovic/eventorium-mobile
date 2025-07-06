package com.eventorium.presentation.event.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.presentation.event.listeners.OnManageEventListener;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.utils.ImageLoader;

import java.util.Objects;

public class ManageableEventAdapter extends PagedListAdapter<EventSummary, ManageableEventAdapter.ManageableEventViewHolder> {

    private final OnManageEventListener manageListener;
    private final ImageSourceProvider<EventSummary> imageSourceProvider;
    private final ImageLoader imageLoader;

    private final LifecycleOwner owner;

    public ManageableEventAdapter(
            LifecycleOwner owner,
            ImageLoader imageLoader,
            ImageSourceProvider<EventSummary> imageSourceProvider,
            OnManageEventListener listener
    ) {
        super(DIFF_CALLBACK);
        this.owner = owner;
        this.manageListener = listener;
        this.imageLoader = imageLoader;
        this.imageSourceProvider = imageSourceProvider;
    }

    @NonNull
    @Override
    public ManageableEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manageable_event_card, parent, false);
        return new ManageableEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageableEventViewHolder holder, int position) {
        EventSummary event = getItem(position);
        if (event != null) {
            holder.bind(event);
        }
    }

    public static final DiffUtil.ItemCallback<EventSummary> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull EventSummary oldItem, @NonNull EventSummary newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull EventSummary oldItem, @NonNull EventSummary newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public class ManageableEventViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView cityTextView;

        Button seeMoreButton;
        Button editButton;
        Button budgetButton;
        ImageView photoImageView;

        public ManageableEventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_name);
            cityTextView = itemView.findViewById(R.id.event_city);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            editButton = itemView.findViewById(R.id.edit_button);
            budgetButton = itemView.findViewById(R.id.budgetButton);
            photoImageView = itemView.findViewById(R.id.event_photo);
        }

        public void bind(EventSummary event) {
            nameTextView.setText(event.getName());
            cityTextView.setText(event.getCity());

            photoImageView.setTag(event.getId());
            LiveData<Bitmap> imageLiveData = imageSourceProvider.getImageSource(event);
            imageLoader.loadImage(event.getId(), imageLiveData, photoImageView, owner);

            seeMoreButton.setOnClickListener(v -> manageListener.onSeeMoreClick(event));
            editButton.setOnClickListener(v -> manageListener.onEditClick(event));
            budgetButton.setOnClickListener(v -> manageListener.navigateToBudget(event));
        }
    }
}
