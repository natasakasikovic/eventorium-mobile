package com.eventorium.presentation.solution.adapters;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.PriceListItem;
import com.eventorium.presentation.util.listeners.OnSaveListener;

import java.util.List;

public class PriceListItemAdapter extends RecyclerView.Adapter<PriceListItemAdapter.ProductViewHolder> {
    private final List<PriceListItem> productList;
    private final OnSaveListener<PriceListItem> onSave;

    public PriceListItemAdapter(List<PriceListItem> productList, OnSaveListener<PriceListItem> onSave) {
        this.productList = productList;
        this.onSave = onSave;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_list_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PriceListItem priceListItem = productList.get(position);

        holder.nameTextView.setText(priceListItem.getName());
        holder.priceEditText.setText(String.format("%.2f", priceListItem.getPrice()));
        holder.discountEditText.setText(String.format("%.2f", priceListItem.getDiscount()));
        holder.netPriceTextView.setText(String.format("$%.2f", priceListItem.getNetPrice()));

        holder.priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!charSequence.toString().isEmpty()) {
                    try {
                        double newPrice = Double.parseDouble(charSequence.toString());
                        priceListItem.setPrice(newPrice);
                        notifyItemChanged(position);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        holder.discountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!charSequence.toString().isEmpty()) {
                    try {
                        double newDiscount = Double.parseDouble(charSequence.toString());
                        priceListItem.setDiscount(newDiscount);
                        notifyItemChanged(position);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        holder.saveButton.setOnClickListener(v -> onSave.save(priceListItem));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, netPriceTextView;
        EditText priceEditText, discountEditText;
        ImageButton saveButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceEditText = itemView.findViewById(R.id.priceEditText);
            discountEditText = itemView.findViewById(R.id.discountEditText);
            netPriceTextView = itemView.findViewById(R.id.netPriceTextView);
            saveButton = itemView.findViewById(R.id.saveButton);
        }
    }
}

