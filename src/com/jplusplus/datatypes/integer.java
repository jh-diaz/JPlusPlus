package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class integer extends Variable{
    private final int MAX_VALUE;
    private final int MIN_VALUE;

    public integer(String variableName, int precedence, int value){
        super(variableName, DataType.integer, precedence, value);
        MAX_VALUE = Integer.MAX_VALUE;
        MIN_VALUE = Integer.MIN_VALUE;
    }
}
