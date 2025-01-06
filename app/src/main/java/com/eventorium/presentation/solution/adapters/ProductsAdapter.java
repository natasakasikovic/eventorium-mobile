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
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.presentation.util.listeners.OnSeeMoreClick;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<ProductSummary> productSummaries;
    private final OnSeeMoreClick<ProductSummary> onSeeMoreClick;

    public ProductsAdapter(List<ProductSummary> productSummaries, OnSeeMoreClick<ProductSummary> onSeeMoreClick) {
        this.productSummaries = productSummaries;
        this.onSeeMoreClick = onSeeMoreClick;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductSummary product = productSummaries.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productSummaries.size();
    }

    public void setData(List<ProductSummary> data) {
        productSummaries = data;
        notifyDataSetChanged();
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

        public void bind(ProductSummary product) {
            nameTextView.setText(product.getName());
            priceTextView.setText(product.getPrice().toString());
            imageView.setImageBitmap(product.getImage());
            seeMoreButton.setOnClickListener(v -> onSeeMoreClick.navigateToDetails(product));

            float alpha = product.getAvailable() ? 1f : 0.5f;
            layout.setAlpha(alpha);

            setDiscountLabel(product);
        }

        private void setDiscountLabel(ProductSummary product) {
            if (hasDiscount(product)) {
                discountTextView.setVisibility(View.VISIBLE);
                discountTextView.setText(product.getDiscount().toString() + "% OFF");
            } else {
                discountTextView.setVisibility(View.GONE);
            }
        }

        private boolean hasDiscount(ProductSummary product) {
            return product.getDiscount() != null && product.getDiscount() > 0;
        }
    }
}
