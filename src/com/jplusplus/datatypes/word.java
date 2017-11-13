package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class word extends Variable{
    public word(String variableName, int precedence, String value){
        super(variableName, DataType.word, precedence, value);
    }

    public char[] wordToNibbleArray(){
        return (String.valueOf(super.getValue())).toCharArray();
    }
}