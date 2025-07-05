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
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.solution.listeners.OnManageListener;

import java.util.Objects;

public class ManageableProductAdapter extends PagedListAdapter<ProductSummary, ManageableProductAdapter.ManageableProductViewHolder> {

    private final OnManageListener<ProductSummary> manageListener;
    private final ImageLoader imageLoader;
    private final ImageSourceProvider<ProductSummary> imageSourceProvider;

    public ManageableProductAdapter(
            ImageLoader imageLoader,
            ImageSourceProvider<ProductSummary> imageSourceProvider,
            OnManageListener<ProductSummary> listener
    ) {
        super(DIFF_CALLBACK);
        this.manageListener = listener;
        this.imageLoader = imageLoader;
        this.imageSourceProvider = imageSourceProvider;
    }

    @NonNull
    @Override
    public ManageableProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manageable_product_card, parent, false);
        return new ManageableProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageableProductViewHolder holder, int position) {
        ProductSummary product = getItem(position);
        if (product != null) {
            holder.bind(product);
        }
    }

    public class ManageableProductViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;

        Button seeMoreButton;
        Button editButton;
        Button deleteButton;
        LinearLayout layout;

        public ManageableProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            layout = itemView.findViewById(R.id.product_layout);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ProductSummary productSummary) {
            nameTextView.setText(productSummary.getName());
            priceTextView.setText(productSummary.getPrice().toString());

            imageLoader.loadImage(
                    ImageHolder.PRODUCT,
                    productSummary.getId(),
                    imageSourceProvider.getImageSource(productSummary),
                    imageView
            );

            seeMoreButton.setOnClickListener(v -> manageListener.onSeeMoreClick(productSummary));
            editButton.setOnClickListener(v -> manageListener.onEditClick(productSummary));
            deleteButton.setOnClickListener(v -> manageListener.onDeleteClick(productSummary));

            if (productSummary.getAvailable() != null) {
                float alpha = productSummary.getAvailable() ? 1f : 0.5f;
                layout.setAlpha(alpha);
            }
        }
    }

    private static final DiffUtil.ItemCallback<ProductSummary> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull ProductSummary oldItem, @NonNull ProductSummary newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull ProductSummary oldItem, @NonNull ProductSummary newItem) {
                    return oldItem.equals(newItem);
                }
            };
}

