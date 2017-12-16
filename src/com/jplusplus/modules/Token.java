package com.jplusplus.modules;

/**
 * Created by Joshua on 11/15/2017.
 */
public class Token {
    private TokenType tokenType;
    private String data;
    private int lineNumber;

    public Token(int lineNumber){
        this.tokenType = null;
        this.data = "";
        this.lineNumber = lineNumber;
    }

    public Token(TokenType tokenType, String data, int lineNumber) {
        this.tokenType = tokenType;
        this.data = data;
        this.lineNumber = lineNumber;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
