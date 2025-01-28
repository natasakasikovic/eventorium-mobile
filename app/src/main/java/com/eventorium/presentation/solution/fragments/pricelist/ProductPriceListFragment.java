package com.eventorium.presentation.solution.fragments.pricelist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.solution.models.pricelist.UpdatePriceList;
import com.eventorium.databinding.FragmentProductPriceListBinding;
import com.eventorium.presentation.solution.adapters.PriceListItemAdapter;
import com.eventorium.presentation.solution.viewmodels.PriceListViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductPriceListFragment extends Fragment {

    private FragmentProductPriceListBinding binding;
    private PriceListViewModel priceListViewModel;

    public ProductPriceListFragment() {
    }

    public static ProductPriceListFragment newInstance() {
        return new ProductPriceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        priceListViewModel = new ViewModelProvider(this).get(PriceListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductPriceListBinding.inflate(inflater, container, false);
        priceListViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if(products.getError() == null) {
                binding.productsRecycleView.setAdapter(new PriceListItemAdapter(products.getData(), product -> {

                    if (product.getDiscount() > 100 || product.getDiscount() < 0) {
                        Toast.makeText(
                                getContext(),
                                R.string.discount_should_be_between_0_and_100,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    priceListViewModel.updateProduct(
                            product.getId(),
                            new UpdatePriceList(product.getPrice(), product.getDiscount())
                    ).observe(getViewLifecycleOwner(), priceListItem -> {
                        if (priceListItem.getError() == null) {
                            Toast.makeText(
                                    getContext(),
                                    R.string.item_has_been_updated_successfully,
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            Toast.makeText(
                                    getContext(),
                                    priceListItem.getError(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }));
            } else {
                Toast.makeText(
                        getContext(),
                        products.getError(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        return binding.getRoot();
    }

}