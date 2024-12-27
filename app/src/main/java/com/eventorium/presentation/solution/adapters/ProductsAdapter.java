package com.eventorium.presentation.solution.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;

import java.util.List;
import java.util.Objects;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private final List<ProductSummary> productSummaries;

    public ProductsAdapter(List<ProductSummary> productSummaries) {
        this.productSummaries = productSummaries;
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
        ProductSummary productSummary = productSummaries.get(position);
        holder.bind(productSummary);
    }

    @Override
    public int getItemCount() {
        return productSummaries.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;
        Button seeMoreButton;
        public ProductViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
        }

        public void bind(ProductSummary productSummary) {
            seeMoreButton.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(itemView);
                int currentId = Objects.requireNonNull(navController.getCurrentDestination()).getId();
                int actionId = 0;

                if (currentId == R.id.homepageFragment) {
                    actionId = R.id.action_home_to_product_details;
                } else {
                    throw new IllegalStateException("Unreachable...");
                }

                navController.navigate(actionId,
                        ProductDetailsFragment.newInstance(productSummary.getId()).getArguments());
            });
        }
    }
}
