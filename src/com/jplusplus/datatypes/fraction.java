package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class fraction extends Variable{
    private final double MAX_VALUE;
    private final double MIN_VALUE;

    public fraction(String variableName, int precedence, double value){
        super(variableName, DataType.fraction, precedence, value);
        MAX_VALUE = Double.MAX_VALUE;
        MIN_VALUE = Double.MIN_VALUE;
    }
}
