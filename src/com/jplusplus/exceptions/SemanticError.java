package com.jplusplus.exceptions;

/**
 * Created by Diaz, Jericho Hans
 * On 2/25/2018
 */
//lul//
public class SemanticError extends RuntimeException {
    public SemanticError() {
    }

    public SemanticError(String message) {
        super(message);
    }

    public SemanticError(Throwable cause) {
        super(cause);
    }

    public SemanticError(String message, Throwable cause) {
        super(message, cause);
    }
}
