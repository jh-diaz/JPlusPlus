package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class fraction extends Variable{
    private double value;
    private final double MAX_VALUE;
    private final double MIN_VALUE;

    public fraction(String variableName, int precedence, double value){
        super(variableName, DataType.fraction, precedence);
        MAX_VALUE = Double.MAX_VALUE;
        MIN_VALUE = Double.MIN_VALUE;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMAX_VALUE() {
        return MAX_VALUE;
    }

    public double getMIN_VALUE() {
        return MIN_VALUE;
    }
}
