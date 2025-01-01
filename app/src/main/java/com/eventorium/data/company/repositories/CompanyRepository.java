package com.eventorium.data.company.repositories;

import com.eventorium.data.company.services.CompanyService;

import javax.inject.Inject;

public class CompanyRepository {

    private final CompanyService service;

    @Inject
    public CompanyRepository(CompanyService service) {
        this.service = service;
    }

}
