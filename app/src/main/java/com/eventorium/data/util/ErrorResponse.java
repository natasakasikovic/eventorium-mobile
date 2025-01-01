package com.eventorium.data.util;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;

    public static String getErrorMessage(String errorResponse) {
        Gson gson = new Gson();
        ErrorResponse parsedError = gson.fromJson(errorResponse, ErrorResponse.class);
        return parsedError.getMessage();
    }
}
