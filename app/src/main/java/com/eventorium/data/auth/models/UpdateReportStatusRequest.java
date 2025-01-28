package com.eventorium.data.auth.models;

import com.eventorium.data.util.models.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateReportStatusRequest {
    private Status status;
}
