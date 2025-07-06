package com.eventorium.presentation.shared.models;

import androidx.lifecycle.LiveData;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;

public interface PageLoader<T> {
    LiveData<Result<PagedResponse<T>>> loadPage(PagingMode mode, int page, int size);
}
