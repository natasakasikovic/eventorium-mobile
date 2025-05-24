package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.utils.RetrofitCallbackHelper;
import com.eventorium.data.solution.models.pricelist.UpdatePriceList;
import com.eventorium.data.solution.models.pricelist.PriceListItem;
import com.eventorium.data.solution.services.PriceListService;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListRepository {

    private final PriceListService priceListService;

    @Inject
    public PriceListRepository(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    public LiveData<Result<List<PriceListItem>>> getServices() {
        MutableLiveData<Result<List<PriceListItem>>> result = new MutableLiveData<>();
        priceListService.getServices().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<PriceListItem>>> getProducts() {
        MutableLiveData<Result<List<PriceListItem>>> result = new MutableLiveData<>();
        priceListService.getProducts().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<PriceListItem>> updateService(Long id, UpdatePriceList dto) {
        MutableLiveData<Result<PriceListItem>> result = new MutableLiveData<>();
        priceListService.updateService(id, dto).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<PriceListItem>> updateProduct(Long id, UpdatePriceList dto) {
        MutableLiveData<Result<PriceListItem>> result = new MutableLiveData<>();
        priceListService.updateProduct(id, dto).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<Uri>> downloadPdf(Context context) {
        MutableLiveData<Result<Uri>> pdfFile = new MutableLiveData<>();
        priceListService.downloadPdf().enqueue(handlePdfExport(context, pdfFile));
        return pdfFile;
    }
}
