package com.eventorium.presentation.solution.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.data.solution.models.product.ProductSummary;

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


    public void setData(List<ProductSummary> data) {
        productSummaries = data;
        notifyDataSetChanged();
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

    public abstract static class BaseProductViewHolder extends RecyclerView.ViewHolder {
        public BaseProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(ProductSummary productSummary);
    }

}
