package com.luziano.reactive.exception;

public class CepNotFoundException extends RuntimeException{

    public CepNotFoundException() {
        super("CEP not found.");
    }
}
