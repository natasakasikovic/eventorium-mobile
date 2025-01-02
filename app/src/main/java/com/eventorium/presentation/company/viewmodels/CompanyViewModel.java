package com.eventorium.presentation.company.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.company.models.Company;
import com.eventorium.data.company.models.CreateCompany;
import com.eventorium.data.company.repositories.CompanyRepository;
import com.eventorium.data.util.Result;

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
}
