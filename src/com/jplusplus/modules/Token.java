package com.jplusplus.modules;

import com.jplusplus.datatypes.DataType;

import javax.xml.crypto.Data;
import java.util.Objects;

/**
 * Created by Joshua on 11/15/2017.
 */
public class Token {
    private TokenType tokenType;
    private String data;
    private int lineNumber;
    private DataType dataType;
    private boolean isCounter;

    public Token(int lineNumber){
        this.tokenType = null;
        this.data = "";
        this.lineNumber = lineNumber;
        this.dataType = null;
        isCounter = false;
    }

    public Token(TokenType tokenType, String data, int lineNumber) {
        this.tokenType = tokenType;
        this.data = data;
        this.lineNumber = lineNumber;
        this.dataType = null;
        isCounter = false;
    }

    public Token(TokenType tokenType, String data, int lineNumber, DataType dataType) {
        this(tokenType, data, lineNumber);
        this.dataType = dataType;
        isCounter = false;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public boolean isCounter() {
        return isCounter;
    }

    public void setCounter(boolean counter) {
        isCounter = counter;
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

    public DataType getDataType() {
        return dataType;
    }

    @Override
    public int hashCode(){
        return data.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(!(o instanceof Token))
            return false;
        Token token = (Token) o;
        return data.equals(token.getData());
    }
}
