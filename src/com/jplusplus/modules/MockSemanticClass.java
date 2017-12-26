package com.jplusplus.modules;

import com.jplusplus.datatypes.DataType;

import java.util.HashMap;

public class MockSemanticClass {
    private HashMap<String, Token> variableSymbolicTable;

    public MockSemanticClass(SyntaxScannerInterface syntaxScanner){
        variableSymbolicTable = syntaxScanner.getVariableSymbolicTable();
    }

    public void checkSemantics(){
        /**
         * sample code
         */
        String variable = "AGAGS";
        if(isVariableExisting(variable)){
            Token currentVariable = variableSymbolicTable.get(variable);
            currentVariable.getLineNumber();
            currentVariable.getData();
            currentVariable.getTokenType();
        }
        //your code goes here
    }

    public boolean isVariableExisting(String name){
        for(String variableName : variableSymbolicTable.keySet()){
            if(variableName.equals(name)){
                return true;
            }
        }
        return false;
    }

}
