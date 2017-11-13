package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class Variable<T> {
    private String variableName;
    private DataType dataType;
    private int precedence;
    private T value;

    public Variable(){
        variableName = "";
        dataType = null;
    }

    public Variable(String variableName, DataType dataType, int precedence, T value){
        this.variableName = variableName;
        this.dataType = dataType;
        this.precedence = precedence;
        this.value = value;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public T getValue(){
        return value;
    }
}
