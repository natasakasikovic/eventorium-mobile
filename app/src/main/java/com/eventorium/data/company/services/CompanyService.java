package com.eventorium.data.company.services;

import com.eventorium.data.company.models.Company;
import com.eventorium.data.company.models.CreateCompany;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CompanyService {

    @POST("companies")
    Call<Company> registerCompany(@Body CreateCompany company);
}
