package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class integer extends Variable{
    private int value;
    private final int MAX_VALUE;
    private final int MIN_VALUE;

    public integer(String variableName, int precedence, int value){
        super(variableName, DataType.integer, precedence);
        MAX_VALUE = Integer.MAX_VALUE;
        MIN_VALUE = Integer.MIN_VALUE;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMAX_VALUE() {
        return MAX_VALUE;
    }

    public int getMIN_VALUE() {
        return MIN_VALUE;
    }
}
