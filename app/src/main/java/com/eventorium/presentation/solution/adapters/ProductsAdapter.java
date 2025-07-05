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
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;
import com.eventorium.presentation.shared.utils.ImageLoader;

import java.util.Objects;

public class ProductsAdapter extends PagedListAdapter<ProductSummary, ProductsAdapter.ProductViewHolder> {

    private final OnSeeMoreClick<ProductSummary> onSeeMoreClick;
    private final ImageLoader imageLoader;
    private final ImageSourceProvider<ProductSummary> imageSourceProvider;

    public ProductsAdapter(
            ImageLoader loader,
            ImageSourceProvider<ProductSummary> imageSourceProvider,
            OnSeeMoreClick<ProductSummary> onSeeMoreClick
    ) {
        super(DIFF_CALLBACK);
        this.onSeeMoreClick = onSeeMoreClick;
        this.imageSourceProvider = imageSourceProvider;
        this.imageLoader = loader;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductSummary product = getItem(position);
        if (product != null) {
            holder.bind(product);
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView discountTextView;
        ImageView imageView;
        Button seeMoreButton;
        LinearLayout layout;

        public ProductViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            discountTextView = itemView.findViewById(R.id.product_discount);
            imageView = itemView.findViewById(R.id.product_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            layout = itemView.findViewById(R.id.layout);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(ProductSummary product) {
            nameTextView.setText(product.getName());
            double price = product.getPrice() * (1 - product.getDiscount() / 100);
            priceTextView.setText(String.format("%.2f", price));

            imageLoader.loadImage(imageSourceProvider.getImageSource(product), imageView);
            seeMoreButton.setOnClickListener(v -> onSeeMoreClick.navigateToDetails(product));

            if (product.getAvailable() != null) {
                float alpha = product.getAvailable() ? 1f : 0.5f;
                layout.setAlpha(alpha);
            }

            setDiscountLabel(product);
        }

        @SuppressLint("SetTextI18n")
        private void setDiscountLabel(ProductSummary product) {
            if (hasDiscount(product)) {
                discountTextView.setVisibility(View.VISIBLE);
                discountTextView.setText(product.getDiscount() + "% OFF");
            } else {
                discountTextView.setVisibility(View.GONE);
            }
        }

        private boolean hasDiscount(ProductSummary product) {
            return product.getDiscount() != null && product.getDiscount() > 0;
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

