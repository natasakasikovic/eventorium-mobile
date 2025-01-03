package com.eventorium.presentation.company.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.company.models.Company;
import com.eventorium.data.company.models.CreateCompany;
import com.eventorium.data.company.repositories.CompanyRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CompanyViewModel extends ViewModel {

    private final CompanyRepository repository;

    @Inject
    public CompanyViewModel(CompanyRepository companyRepository) {
        this.repository = companyRepository;
    }

    public LiveData<Result<Company>> registerCompany(CreateCompany company) {
        return repository.registerCompany(company);
    }

    public LiveData<Boolean> uploadImages(Long companyId, Context context, List<Uri> uris) {
        return repository.uploadImages(companyId, context, uris);
    }
}
