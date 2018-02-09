package com.jplusplus.exceptions;

public class SyntaxError extends RuntimeException{
    public SyntaxError(){

    }

    public SyntaxError(String msg){
        super(msg);
    }

    public SyntaxError(Throwable cause){
        super(cause);
    }

    public SyntaxError(String msg, Throwable cause){
        super(msg, cause);
    }
}
