package com.eventorium.presentation.shared.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.PagingMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class PagedViewModel<T, F> extends ViewModel {

    protected final MediatorLiveData<List<T>> items = new MediatorLiveData<>();
    protected final MutableLiveData<Integer> page = new MutableLiveData<>(0);

    private final Set<Integer> requestedPages = new HashSet<>();
    protected final int pageSize = 2;

    public boolean isLoading = false;
    public boolean isLastPage = false;

    protected PagingMode mode = PagingMode.DEFAULT;
    protected String searchQuery = null;
    protected F filterParams = null;

    public LiveData<List<T>> getItems() {
        return items;
    }
    public void loadNextPage() {
        int currentPage = page.getValue() != null ? page.getValue() : 0;

        if (isLoading || isLastPage || requestedPages.contains(currentPage)) return;

        isLoading = true;

        LiveData<Result<PagedResponse<T>>> source = loadPage(mode, currentPage, pageSize);

        items.addSource(source, result -> {
            items.removeSource(source);
            isLoading = false;

            if (result.getError() == null && result.getData() != null) {
                requestedPages.add(currentPage);

                List<T> currentList = items.getValue();
                if (currentList == null) currentList = new ArrayList<>();

                PagedResponse<T> data = result.getData();
                List<T> newItems = data.getContent();

                isLastPage = newItems.size() < pageSize;

                currentList.addAll(newItems);
                items.postValue(currentList);

                page.postValue(currentPage + 1);
            }
        });
    }


    public void refresh() {
        requestedPages.clear();
        page.setValue(0);
        isLastPage = false;
        items.setValue(new ArrayList<>());
        loadNextPage();
    }

    public void search(String query) {
        if (Objects.equals(this.searchQuery, query)) return;
        this.searchQuery = query;
        this.mode = PagingMode.SEARCH;
        refresh();
    }

    public void filter(F filter) {
        this.filterParams = filter;
        this.mode = PagingMode.FILTER;
        refresh();
    }

    public void showAll() {
        if (this.mode != PagingMode.DEFAULT) {
            this.mode = PagingMode.DEFAULT;
            refresh();
        }
    }

    protected abstract LiveData<Result<PagedResponse<T>>> loadPage(
            PagingMode mode, int page, int size
    );
}
