package com.jplusplus.grammar;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Joshua on 11/7/2017.
 */
public class LexicalScanner {
    public enum TOKENS{
        IDENTIFIER, OPERATOR, DATA_TYPE, CONDITIONAL,
        EXPRESSION, ITERATIVE, COMPARISON, LITERAL,
        TERMINATOR;
    }

    private ArrayList<TOKENS> tokenList;
    private ArrayList<String> lexemeList;

    public LexicalScanner(Scanner userFile) {
        tokenList = new ArrayList<>();
        lexemeList = new ArrayList<>();
        readInputs(userFile);
    }

    public ArrayList<TOKENS> getTokenList() {
        return tokenList;
    }

    public ArrayList<String> getLexemeList() {
        return lexemeList;
    }

    public TOKENS[] tokenListAsArray(){
        return tokenList.toArray(new TOKENS[]{});
    }

    public String[] lexemeListAsArray(){
        return lexemeList.toArray(new String[]{});
    }

    private String[] splitInput(String userInput){
        return userInput.trim().split("\\s+");

    }

    private void readInputs(Scanner userFile){
        while(userFile.hasNext()){
            String word = userFile.next();
            if(word.equals("integer") || word.equals("string") || word.equals("boolean") || word.equals("character")){
                tokenList.add(TOKENS.DATA_TYPE);
            }

            else if(word.equals("+") || word.equals("-") || word.equals("*") || word.equals("/") || word.equals("%")){
                tokenList.add(TOKENS.OPERATOR);
            }

            else if(word.equals("if") || word.equals("elseif") || word.equals("else")){
                tokenList.add(TOKENS.CONDITIONAL);
            }

            else if(word.equals("=")){
                tokenList.add(TOKENS.EXPRESSION);
            }

            else if(word.equals("while")){
                tokenList.add(TOKENS.ITERATIVE);
            }

            else if(word.equals(">") || word.equals(">=") || word.equals("<") || word.equals("<=")){
                tokenList.add(TOKENS.COMPARISON);
            }

            else if(word.equals("endif") || word.equals("endwhile")){
                tokenList.add(TOKENS.TERMINATOR);
            }

            else if(word.toCharArray()[0] != '"' && word.matches("[a-zA-Z]+")){
                tokenList.add(TOKENS.IDENTIFIER);
            }

            else{
                tokenList.add(TOKENS.LITERAL);
            }
            lexemeList.add(word);
        }
    }
}
