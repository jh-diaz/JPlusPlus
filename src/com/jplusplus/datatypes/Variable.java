package com.jplusplus.datatypes;

/**
 * Created by Joshua on 11/13/2017.
 */
public class Variable {
    private String variableName;
    private DataType dataType;
    private int precedence;

    public Variable(){
        variableName = "";
        dataType = null;
    }

    public Variable(String variableName, DataType dataType, int precedence){
        this.variableName = variableName;
        this.dataType = dataType;
        this.precedence = precedence;
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

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }
}
