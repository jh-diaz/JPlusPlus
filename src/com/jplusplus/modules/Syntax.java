package com.jplusplus.modules;

import com.jplusplus.exceptions.LexicalScanningException;

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
        if(E()){
            System.out.println("accepted");
        }
        else{
            System.out.println("rejected");
        }
    }

    /*private boolean E(){
        System.out.println("E");
        T();
        Eopt();
        return true;
    }

    private void Eopt(){
        System.out.println("E opt");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("+") || expectedData("-")){
            currentIndex++;
            T();
            Eopt();
        }
    }

    private void T(){
        System.out.println("T");
        F();
        Topt();
    }

    private void Topt(){
        System.out.println("Topt");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("*") || expectedData("/")){
            currentIndex++;
            F();
            Topt();
        }
    }

    private void F(){
        System.out.println("F");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedToken(TokenType.LITERAL)){
            currentIndex++;
        }
        else{
            System.out.println("MISSING LITERAL AT " + currentToken.getData());
            throw new LexicalScanningException("sssss");
        }
    }*/

    private boolean E(){
        System.out.println("E");
        getToken();
        System.out.println(currentToken.getData());
        return T() && Eopt();
    }

    private boolean Eopt(){
        System.out.println("EOpt");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("+") || expectedData("-")){ //lookahead
            currentIndex++;
            return T() && Eopt();
        }
        else{
            return true; //lambda
        }
    }

    private boolean T(){
        System.out.println("T");
        getToken();
        System.out.println(currentToken.getData());
        return F() && TopT();
    }

    private boolean TopT(){
        System.out.println("TopT");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("*") || expectedData("/")){
            currentIndex++;
            return F() && TopT();
        }
        else{
            return true; //lambda
        }
    }

    private boolean F(){
        System.out.println("F");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedToken(TokenType.LITERAL) || expectedToken(TokenType.IDENTIFIER)){
            if(lexicalScanner.hasNextToken(currentIndex+1)){
                currentIndex++;
            }
            return true;
        }
        else{
            return false;
        }
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
        return currentToken.getTokenType() == tokenType;
    }

   /* *//*private void nextToken(){
        if(lexicalScanner.hasNextToken(currentIndex)){
            currentToken = lexicalScanner.getToken(currentIndex++);
        }
        else{
            System.out.println("Insufficient tokens at line number " + currentToken.getLineNumber() + " near " + currentToken.getData());
            System.exit(0);
       *//* }
    }*/

    private void getToken(){
        if(lexicalScanner.hasNextToken(currentIndex)){
            currentToken = lexicalScanner.getToken(currentIndex);
        }
        else{
            System.out.println("Insufficient tokens at line number " + currentToken.getLineNumber() + " near " + currentToken.getData());
            System.exit(0);
        }
    }
}
