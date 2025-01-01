package com.eventorium.presentation.company.viewmodels;

import androidx.lifecycle.ViewModel;

import com.eventorium.data.company.repositories.CompanyRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CompanyViewModel extends ViewModel {

    private final CompanyRepository repository;

    @Inject
    public CompanyViewModel(CompanyRepository companyRepository) {
        this.repository = companyRepository;
    }
}
