package com.eventorium.presentation.solution.adapters;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.solution.models.pricelist.PriceListItem;
import com.eventorium.presentation.solution.listeners.OnSaveListener;

import java.util.List;
import java.util.Objects;

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

        holder.createPriceChangeHandler(priceListItem);
        holder.createDiscountChangeHandler(priceListItem);

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

        private final Handler handler = new Handler(Looper.getMainLooper());
        private Runnable debounceRunnable;
        private static final long DEBOUNCE_DELAY = 300;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceEditText = itemView.findViewById(R.id.priceEditText);
            discountEditText = itemView.findViewById(R.id.discountEditText);
            netPriceTextView = itemView.findViewById(R.id.netPriceTextView);
            saveButton = itemView.findViewById(R.id.saveButton);
        }


        private void createPriceChangeHandler(PriceListItem priceListItem) {
            priceEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (debounceRunnable != null) {
                        handler.removeCallbacks(debounceRunnable);
                    }
                }

                @SuppressLint("DefaultLocale")
                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        debounceRunnable = () -> onPriceChanged(editable, priceListItem);
                        handler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);
                    }
                }
            });
        }

        private void createDiscountChangeHandler(PriceListItem priceListItem) {
            discountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (debounceRunnable != null) {
                        handler.removeCallbacks(debounceRunnable);
                    }
                }

                @SuppressLint("DefaultLocale")
                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        debounceRunnable = () -> onDiscountChanged(editable, priceListItem);
                        handler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);
                    }
                }
            });
        }

        @SuppressLint("DefaultLocale")
        private void onDiscountChanged(Editable editable, PriceListItem priceListItem) {
            try {
                double newDiscount = Double.parseDouble(editable.toString());
                priceListItem.setDiscount(newDiscount);
                priceListItem.setNetPrice(priceListItem.getPrice() * (1 - newDiscount / 100));
                netPriceTextView.setText(String.format("$%.2f", priceListItem.getNetPrice()));
            } catch (NumberFormatException e) {
                Log.e("PRICE_LIST", Objects.requireNonNull(e.getLocalizedMessage()));
            }
        }

        @SuppressLint("DefaultLocale")
        private void onPriceChanged(Editable editable, PriceListItem priceListItem) {
            try {
                double newPrice = Double.parseDouble(editable.toString());
                priceListItem.setPrice(newPrice);
                priceListItem.setNetPrice(newPrice * (1 - priceListItem.getDiscount() / 100));
                netPriceTextView.setText(String.format("$%.2f", priceListItem.getNetPrice()));
            } catch (NumberFormatException e) {
                Log.e("PRICE_LIST", Objects.requireNonNull(e.getLocalizedMessage()));
            }
        }
    }
}

