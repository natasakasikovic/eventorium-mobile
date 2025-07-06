package com.eventorium.data.category.models;


import com.eventorium.data.shared.models.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryStatus {
    private Status status;
}
