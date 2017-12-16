package com.jplusplus.modules;

import java.util.ArrayList;

/**
 * Created by Joshua on 11/15/2017.
 */
public class Syntax {
    private LexicalScannerInterface lexicalScanner;
    private Token currentToken;
    private int currentIndex;
    private int savedIndex;
    private String[] syntaxList;

    public Syntax(LexicalScannerInterface lexicalScanner){
        this.lexicalScanner = lexicalScanner;
        currentIndex = 0;
        savedIndex = 0;
        syntaxList = lexicalScanner.getSyntaxList();
        if(statement()){
            System.out.println("accepted");
        }
        else{
            System.out.println("rejected");
        }
    }

    private boolean statement(){
        return declarationStatement();
    }

    private boolean declarationStatement(){
        return expectedToken(TokenType.DATA_TYPE)
                && (saveToken() | assignment() | backtrackToken() | identifier())
                && expectedToken(TokenType.LINE_TERMINATOR);
    }

    private boolean assignment(){
        return identifier() && expectedToken(TokenType.ASSIGNMENT) && expression();
    }

    private boolean identifier(){
        return expectedToken(TokenType.IDENTIFIER);
    }

    private boolean literal(){
        return expectedToken(TokenType.LITERAL);
    }

    private boolean operand(){
        returnIndex2();
        returnIndex();
        return saveToken() || expectedToken(TokenType.IDENTIFIER)
                || backtrackToken() || expectedToken(TokenType.LITERAL);
    }

    private boolean expression(){
        return arithmeticExpression() || relationalComparison() ;
    }

    private boolean arithmeticExpression(){
        if(operand()){
            int occurences = 0;
            for(; arithmeticOperators() && operand(); occurences++);
            return occurences != 0;
        }
        else{
            return false;
        }
    }

    private boolean relationalComparison(){
        return  (saveToken() | operand() | backtrackToken() | arithmeticExpression())
                && expectedToken(TokenType.RELATIONAL_OPERATOR)
                && (saveToken() | operand() | backtrackToken() | arithmeticExpression());
    }

    private boolean arithmeticOperators(){
        int index = 13;
        return (saveToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index])))
        || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || backtrackToken();
    }

    private boolean returnIndex(){
        System.out.println(savedIndex);
        return false;
    }

    private boolean returnIndex2(){
        System.out.println(currentIndex);
        return false;
    }

    private boolean backtrackToken(){
        currentIndex = savedIndex;
        saveToken();
        return false; //para lang masabay sa return statement.
    }

    private boolean saveToken(){
        savedIndex = currentIndex;

        return false; //para lang masabay sa return statement.
    }

    private boolean expectedData(String data){
        return currentToken.getData().equals(data); //case sensitive lexeme/data
    }

    private boolean expectedToken(TokenType tokenType){
        nextToken();
        return currentToken.getTokenType() == tokenType;
    }

    private void nextToken(){
        if(lexicalScanner.hasNextToken(currentIndex)){
            currentToken = lexicalScanner.getToken(currentIndex++);
        }
        else{
            System.out.println("Insufficient tokens at line number " + currentToken.getLineNumber() + " near " + currentToken.getData());
            System.exit(0);
        }
    }
}
