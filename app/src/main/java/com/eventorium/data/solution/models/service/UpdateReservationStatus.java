package com.eventorium.data.solution.models.service;

import com.eventorium.data.util.models.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationStatus {

    private Status status;
}
