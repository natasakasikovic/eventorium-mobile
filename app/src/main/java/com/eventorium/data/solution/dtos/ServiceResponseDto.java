package com.eventorium.data.solution.dtos;

import com.eventorium.data.util.models.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponseDto {
    private Long id;
    private String name;
    private Double price;
    private Boolean available;
    private Boolean visible;
    private Status status;
}
