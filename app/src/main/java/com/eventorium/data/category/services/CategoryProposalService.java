package com.eventorium.data.category.services;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.models.CategoryRequest;
import com.eventorium.data.category.models.UpdateCategoryStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryProposalService {

    @GET("categories/pending/all")
    Call<List<Category>> getServiceProposals();

    @PATCH("categories/pending/{id}")
    Call<Category> updateCategoryStatus(
            @Path("id") Long id,
            @Body UpdateCategoryStatus dto
    );

    @PUT("categories/pending/{id}")
    Call<Category> updateCategoryProposal(
            @Path("id") Long id,
            @Body CategoryRequest dto
    );

    @PUT("categories/pending/{id}/change")
    Call<Category> changeCategoryProposal(
            @Path("id") Long id,
            @Body CategoryRequest dto
    );
}
