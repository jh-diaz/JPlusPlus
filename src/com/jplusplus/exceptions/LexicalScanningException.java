package com.jplusplus.exceptions;

public class LexicalScanningException extends RuntimeException{
    public LexicalScanningException() {}

    public LexicalScanningException(String msg){
        super(msg);
    }

    public LexicalScanningException(Throwable cause){
        super(cause);
    }

    public LexicalScanningException(String msg, Throwable cause){
        super(msg, cause);
    }
}
