package com.eventorium.data.solution.services;

import com.eventorium.data.solution.dtos.UpdatePriceListRequestDto;
import com.eventorium.data.solution.dtos.UpdateServiceRequestDto;
import com.eventorium.data.solution.models.PriceListItem;
import com.eventorium.data.solution.models.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface PriceListService {
    @GET("price-list/services/all")
    Call<List<PriceListItem>> getServices();

    @GET("price-list/products/all")
    Call<List<PriceListItem>> getProducts();

    @PATCH("price-list/services/{id}")
    Call<PriceListItem> updateService(@Path("id") Long id, @Body UpdatePriceListRequestDto dto);

    @PATCH("price-list/products/{id}")
    Call<PriceListItem> updateProduct(@Path("id") Long id, @Body UpdatePriceListRequestDto dto);

    @GET("price-list/pdf")
    Call<ResponseBody> downloadPdf();
}
