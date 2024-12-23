package com.eventorium.data.util;

public class Result<T> {
    private T data;
    private String error;

    private Result(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null);
    }

    public static <T> Result<T> error(String error) {
        return new Result<>(null, error);
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}

