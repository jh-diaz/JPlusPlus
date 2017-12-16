package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class word extends Variable{
    private String value;
    public word(String variableName, int precedence, String value){
        super(variableName, DataType.word, precedence);
        this.value = value;
    }

    public char[] wordToNibbleArray(){
        return value.toCharArray();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}