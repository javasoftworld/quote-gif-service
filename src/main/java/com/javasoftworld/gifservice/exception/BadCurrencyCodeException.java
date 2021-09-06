package com.javasoftworld.gifservice.exception;

public class BadCurrencyCodeException extends IllegalArgumentException {
    public BadCurrencyCodeException(String s) {
        super(s);
    }
}
