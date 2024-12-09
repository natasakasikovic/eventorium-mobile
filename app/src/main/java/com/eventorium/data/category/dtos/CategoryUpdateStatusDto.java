package com.eventorium.data.category.dtos;


import com.eventorium.data.util.models.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateStatusDto {
    private Status status;
}
