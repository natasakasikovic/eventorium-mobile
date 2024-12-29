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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.util.listeners.OnSeeMoreClick;

import java.util.List;
import java.util.Objects;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private final List<ProductSummary> productSummaries;
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
        bindProductDetails(holder, product);
        setProductAvailability(holder, product);
        setDiscountLabel(holder, product);
    }

    private void bindProductDetails(ProductViewHolder holder, ProductSummary product) {
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(product.getPrice().toString());
        // TODO: Add image when backend is updated to support images
        holder.bind(product);
    }

    private void setProductAvailability(ProductViewHolder holder, ProductSummary product) {
        float alpha = product.getAvailable() ? 1f : 0.5f;
        holder.layout.setAlpha(alpha);
    }

    private void setDiscountLabel(ProductViewHolder holder, ProductSummary product) {
        if (hasDiscount(product)) {
            holder.discountTextView.setVisibility(View.VISIBLE);
            holder.discountTextView.setText(product.getDiscount().toString() + "% OFF");
        } else {
            holder.discountTextView.setVisibility(View.GONE);
        }
    }

    private boolean hasDiscount(ProductSummary product) {
        return product.getDiscount() != null && product.getDiscount() > 0;
    }

    @Override
    public int getItemCount() {
        return productSummaries.size();
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

        public void bind(ProductSummary productSummary) {
            seeMoreButton.setOnClickListener(v -> onSeeMoreClick.navigateToDetails(productSummary));
        }
    }
}
