package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class nibble extends Variable{
    private char value;

    public nibble(String variableName, int precedence, char value){
        super(variableName, DataType.nibble, precedence);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
