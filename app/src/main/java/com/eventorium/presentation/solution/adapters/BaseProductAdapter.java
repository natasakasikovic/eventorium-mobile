package com.eventorium.presentation.solution.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.data.solution.models.product.ProductSummary;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseProductAdapter<T extends BaseProductAdapter.BaseProductViewHolder>
        extends RecyclerView.Adapter<T> {

    protected List<ProductSummary> productSummaries;
    public BaseProductAdapter(List<ProductSummary> productSummaries) {
        this.productSummaries = productSummaries;
    }

    @NonNull
    @Override
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public void setData(List<ProductSummary> newProducts) {
        this.productSummaries.clear();
        this.productSummaries.addAll(newProducts);
        notifyDataSetChanged();
    }


    public void removeData(Long productId) {
        if (productId == null) return;

        for (int i = 0; i < productSummaries.size(); i++) {
            ProductSummary product = productSummaries.get(i);
            if (productId.equals(product.getId())) {
                productSummaries.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        ProductSummary productSummary = productSummaries.get(position);
        holder.bind(productSummary);
    }

    @Override
    public int getItemCount() {
        return productSummaries.size();
    }

    public void addProducts(List<ProductSummary> newProducts) {
        int start = productSummaries.size();
        productSummaries.addAll(newProducts);
        notifyItemRangeInserted(start, productSummaries.size());
    }

    public abstract static class BaseProductViewHolder extends RecyclerView.ViewHolder {
        public BaseProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(ProductSummary productSummary);
    }

}
