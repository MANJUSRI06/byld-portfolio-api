package com.manju.byldportfolioapi.exception;

public class InsufficientHoldingException extends RuntimeException {
    public InsufficientHoldingException(String message) {
        super(message);
    }
}