package com.eventorium.presentation.shared.models;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.util.List;

public class GenericPageKeyedDataSource<T> extends PageKeyedDataSource<Integer, T> {

    private final PagingMode mode;
    private final PageLoader<T> pageLoader;
    private final int pageSize;

    public GenericPageKeyedDataSource(
            PagingMode mode,
            int pageSize,
            PageLoader<T> pageLoader
    ) {
        this.mode = mode;
        this.pageLoader = pageLoader;
        this.pageSize = pageSize;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, T> callback) {
        new Handler(Looper.getMainLooper()).post(() -> {
            pageLoader.loadPage(mode, 0, params.requestedLoadSize)
                    .observeForever(result -> {
                        if (result.getError() == null) {
                            List<T> data = result.getData().getContent();
                            callback.onResult(
                                    data,
                                    null,
                                    data.size() < pageSize ? null : 1
                            );
                        }
                    });
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, T> callback) {
        // Rarely used in forward paging
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<Integer, T> callback) {
        pageLoader.loadPage(mode, params.key, params.requestedLoadSize)
                .observeForever(result -> {
                    if (result.getError() == null) {
                        List<T> data = result.getData().getContent();
                        Integer nextKey = data.size() < pageSize ? null : params.key + 1;
                        callback.onResult(data, nextKey);
                    }
                });
    }
}

