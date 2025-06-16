package com.eventorium.presentation.solution.adapters;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eventorium.R;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.presentation.solution.listeners.OnManageListener;

import java.util.List;
import java.util.Objects;

public class ManageableProductAdapter extends BaseProductAdapter<ManageableProductAdapter.ManageableProductViewHolder> {

    private final OnManageListener<ProductSummary> manageListener;

    public ManageableProductAdapter(List<ProductSummary> productSummaries, OnManageListener<ProductSummary> listener) {
        super(productSummaries);
        this.manageListener = listener;
    }

    @NonNull
    @Override
    public ManageableProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manageable_product_card, parent, false);
        return new ManageableProductViewHolder(view);
    }

    public void setProducts(List<ProductSummary> newProducts) {
        productSummaries = newProducts;
        notifyDataSetChanged();
    }

    public void removeProduct(Long productId) {
        productSummaries = Objects.requireNonNull(productSummaries)
                .stream()
                .filter(product -> !Objects.equals(product.getId(), productId))
                .collect(toList());
        notifyDataSetChanged();
    }

    public class ManageableProductViewHolder extends BaseProductViewHolder {

        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;

        Button seeMoreButton;
        Button editButton;
        Button deleteButton;

        public ManageableProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ProductSummary productSummary) {
            nameTextView.setText(productSummary.getName());
            priceTextView.setText(productSummary.getPrice().toString());
            imageView.setImageBitmap(productSummary.getImage());

            seeMoreButton.setOnClickListener(v -> manageListener.onSeeMoreClick(productSummary));
            editButton.setOnClickListener(v -> manageListener.onEditClick(productSummary));
            deleteButton.setOnClickListener(v -> manageListener.onDeleteClick(productSummary));
        }
    }
}
