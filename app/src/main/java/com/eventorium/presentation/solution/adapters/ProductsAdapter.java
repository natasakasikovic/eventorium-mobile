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
import com.eventorium.data.shared.models.ImageHolder;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;
import com.eventorium.presentation.shared.utils.ImageLoader;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;

import java.util.List;

public class ProductsAdapter extends BaseProductAdapter<ProductsAdapter.ProductViewHolder> {

    private final OnSeeMoreClick<ProductSummary> onSeeMoreClick;
    private final ImageLoader imageLoader;
    private final ImageSourceProvider<ProductSummary> imageSourceProvider;


    public ProductsAdapter(
            List<ProductSummary> productSummaries,
            ImageLoader loader,
            ImageSourceProvider<ProductSummary> imageSourceProvider,
            OnSeeMoreClick<ProductSummary> onSeeMoreClick
    ) {
        super(productSummaries);
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
        ProductSummary product = productSummaries.get(position);
        holder.bind(product);
    }

    public class ProductViewHolder extends BaseProductViewHolder {
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

            imageLoader.loadImage(
                    ImageHolder.PRODUCT,
                    product.getId(),
                    imageSourceProvider.getImageSource(product),
                    imageView
            );

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
