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
    private final MutableLiveData<PagingMode> pagingMode = new MutableLiveData<>(PagingMode.DEFAULT);

    protected String searchQuery = null;
    protected F filterParams = null;
    protected String sortCriteria = null;

    protected static final int PAGE_SIZE = 10;

    private GenericPageKeyedDataSource<T> currentDataSource;

    public PagedViewModel() {
        items = Transformations.switchMap(pagingMode, mode -> {
            DataSource.Factory<Integer, T> factory = new DataSource.Factory<>() {
                @NonNull
                @Override
                public DataSource<Integer, T> create() {
                    currentDataSource = new GenericPageKeyedDataSource<>(
                            mode,
                            PAGE_SIZE,
                            PagedViewModel.this::loadPage
                    );
                    return currentDataSource;
                }
            };
            return new LivePagedListBuilder<>(
                    factory,
                    new PagedList.Config.Builder()
                            .setPageSize(PAGE_SIZE)
                            .setInitialLoadSizeHint(PAGE_SIZE)
                            .setPrefetchDistance(PAGE_SIZE / 2)
                            .setEnablePlaceholders(true)
                            .build()
            ).build();
        });
    }

    public void search(String query) {
        if (!Objects.equals(this.searchQuery, query)) {
            this.searchQuery = query;
            pagingMode.setValue(PagingMode.SEARCH);
        }
    }

    public void filter(F filter) {
        if (!Objects.equals(this.filterParams, filter)) {
            this.filterParams = filter;
            pagingMode.setValue(PagingMode.FILTER);
        }
    }

    public void sort(String sortCriteria) {
        if (!Objects.equals(this.sortCriteria, sortCriteria)) {
            this.sortCriteria = sortCriteria;
            pagingMode.setValue(PagingMode.SORT);
        }
    }

    public void refresh() {
        PagingMode currentMode = pagingMode.getValue();
        if (currentMode != null) {
            pagingMode.setValue(currentMode);
        }
    }

    protected abstract LiveData<Result<PagedResponse<T>>> loadPage(
            PagingMode mode,
            int page,
            int size
    );
}
