package com.eventorium.presentation.shared.listeners;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.PagingMode;

public interface DataLoader<T, F> {
        Result<PagedResponse<T>> loadPage(PagingMode mode, int page, int size, String query, F filter);
}
