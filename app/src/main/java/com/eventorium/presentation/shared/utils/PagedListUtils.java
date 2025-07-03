package com.eventorium.presentation.shared.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PagedListUtils {
    public static <T> PagedList<T> fromList(List<T> list) {
        Executor mainThreadExecutor = new MainThreadExecutor();
        Executor backgroundExecutor = Executors.newSingleThreadExecutor();

        return new PagedList.Builder<>(
                new ListDataSource<>(list),
                new PagedList.Config.Builder()
                        .setPageSize(!list.isEmpty() ? list.size() : 1)
                        .setEnablePlaceholders(false)
                        .build()
        )
                .setFetchExecutor(backgroundExecutor)
                .setNotifyExecutor(mainThreadExecutor)
                .build();
    }
    private static class ListDataSource<T> extends PositionalDataSource<T> {
        private final List<T> data;

        public ListDataSource(List<T> data) {
            this.data = data;
        }

        @Override
        public void loadInitial(LoadInitialParams params, LoadInitialCallback<T> callback) {
            callback.onResult(data, 0, data.size());
        }

        @Override
        public void loadRange(PositionalDataSource.LoadRangeParams params, LoadRangeCallback<T> callback) {
            int end = Math.min(params.startPosition + params.loadSize, data.size());
            callback.onResult(data.subList(params.startPosition, end));
        }
    }
    private static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}



