package com.eventorium.presentation.shared.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.GenericPageKeyedDataSource;
import com.eventorium.presentation.shared.models.PagingMode;

import java.util.Objects;

import lombok.Getter;

public abstract class PagedViewModel<T, F> extends ViewModel {

    @Getter
    private final LiveData<PagedList<T>> items;
    private final MutableLiveData<PagingMode> pagingModeLiveData = new MutableLiveData<>(PagingMode.DEFAULT);

    protected String searchQuery = null;
    protected F filterParams = null;
    protected final int pageSize = 10;

    private GenericPageKeyedDataSource<T> currentDataSource;

    public PagedViewModel() {
        items = Transformations.switchMap(pagingModeLiveData, mode -> {
            DataSource.Factory<Integer, T> factory = new DataSource.Factory<>() {
                @NonNull
                @Override
                public DataSource<Integer, T> create() {
                    currentDataSource = new GenericPageKeyedDataSource<>(
                            mode,
                            pageSize,
                            PagedViewModel.this::loadPage
                    );
                    return currentDataSource;
                }
            };
            return new LivePagedListBuilder<>(
                    factory,
                    new PagedList.Config.Builder()
                            .setPageSize(pageSize)
                            .setInitialLoadSizeHint(pageSize)
                            .setEnablePlaceholders(false)
                            .build()
            ).build();
        });
    }

    public void search(String query) {
        if (!Objects.equals(this.searchQuery, query)) {
            this.searchQuery = query;
            invalidateDataSource();
            pagingModeLiveData.setValue(PagingMode.SEARCH);
        }
    }

    public void filter(F filter) {
        if (!Objects.equals(this.filterParams, filter)) {
            this.filterParams = filter;
            invalidateDataSource();
            pagingModeLiveData.setValue(PagingMode.FILTER);
        }
    }

    public void showAll() {
        if (pagingModeLiveData.getValue() != PagingMode.DEFAULT) {
            invalidateDataSource();
            pagingModeLiveData.setValue(PagingMode.DEFAULT);
        }
    }

    public void refresh() {
        invalidateDataSource();
        PagingMode currentMode = pagingModeLiveData.getValue();
        if (currentMode != null) {
            pagingModeLiveData.setValue(currentMode);
        }
    }

    private void invalidateDataSource() {
        if (currentDataSource != null) {
            currentDataSource.invalidate();
        }
    }

    protected abstract LiveData<Result<PagedResponse<T>>> loadPage(
            PagingMode mode,
            int page,
            int size
    );
}


