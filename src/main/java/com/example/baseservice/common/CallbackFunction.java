package com.example.baseservice.common;

@FunctionalInterface
public interface CallbackFunction<T> {
    T execute();
}
