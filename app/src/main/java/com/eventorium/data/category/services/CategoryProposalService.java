package com.eventorium.data.category.services;

import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryResponseDto;
import com.eventorium.data.category.dtos.CategoryUpdateStatusDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryProposalService {

    @GET("categories/pending/all")
    Call<List<CategoryResponseDto>> getServiceProposals();

    @PATCH("categories/pending/{id}")
    Call<CategoryResponseDto> updateCategoryStatus(
            @Path("id") Long id,
            @Body CategoryUpdateStatusDto dto
    );

    @PUT("categories/pending/{id}")
    Call<CategoryResponseDto> updateCategoryProposal(
            @Path("id") Long id,
            @Body CategoryRequestDto dto
    );

    @PUT("categories/pending/{id}/change")
    Call<CategoryResponseDto> changeCategoryProposal(
            @Path("id") Long id,
            @Body CategoryRequestDto dto
    );
}
