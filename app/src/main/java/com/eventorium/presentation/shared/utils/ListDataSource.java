package com.eventorium.presentation.shared.utils;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.List;

public class ListDataSource<T> extends PositionalDataSource<T> {
    private final List<T> data;

    public ListDataSource(List<T> data) {
        this.data = data;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, LoadInitialCallback<T> callback) {
        callback.onResult(data, 0, data.size());
    }

    @Override
    public void loadRange(LoadRangeParams params, LoadRangeCallback<T> callback) {
        int end = Math.min(params.startPosition + params.loadSize, data.size());
        callback.onResult(data.subList(params.startPosition, end));
    }
}

