package com.jplusplus.grammar;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Joshua on 11/7/2017.
 */
public class LexicalScanner {
    public enum TOKENS{
        IDENTIFIER, ARITHMETIC_OPERATOR, DATA_TYPE, CONDITIONAL,
        EXPRESSION, ITERATIVE, RELATIONAL_OPERATOR, LITERAL,
        TERMINATOR, COMMENT, OPERATION;
    }

    private ArrayList<TOKENS> tokenList;
    private ArrayList<String> lexemeList;
    private ArrayList<String> userComments;
    private String[] syntaxList;

    public LexicalScanner(Scanner userFile, Scanner syntaxFile) {
        tokenList = new ArrayList<>();
        lexemeList = new ArrayList<>();
        userComments = new ArrayList<>();
        syntaxList = readSyntaxFile(syntaxFile);
        readInputs(userFile);
    }

    public ArrayList<TOKENS> getTokenList() {
        return tokenList;
    }

    public ArrayList<String> getLexemeList() {
        return lexemeList;
    }

    public int getLength(){
        return tokenList.size();
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
            if(word.equals(syntaxList[2])
                    || word.equals(syntaxList[4])
                    || word.equals(syntaxList[6])
                    || word.equals(syntaxList[8])
                    || word.equals(syntaxList[10])){
                tokenList.add(TOKENS.DATA_TYPE);
            }

            else if(word.equals(syntaxList[13])
                    || word.equals(syntaxList[15])
                    || word.equals(syntaxList[17])
                    || word.equals(syntaxList[19])
                    || word.equals(syntaxList[21])){
                tokenList.add(TOKENS.ARITHMETIC_OPERATOR);
            }

            else if(word.equals(syntaxList[24])
                    || word.equals(syntaxList[26])
                    || word.equals(syntaxList[28])){
                tokenList.add(TOKENS.CONDITIONAL);
            }

            else if(word.equals(syntaxList[31])){
                tokenList.add(TOKENS.EXPRESSION);
            }

            else if(word.equals(syntaxList[34])){
                tokenList.add(TOKENS.ITERATIVE);
            }

            else if(word.equals(syntaxList[37])
                    || word.equals(syntaxList[39])
                    || word.equals(syntaxList[41])
                    || word.equals(syntaxList[43])
                    || word.equals(syntaxList[45])
                    || word.equals(syntaxList[47])){
                tokenList.add(TOKENS.RELATIONAL_OPERATOR);
            }

            else if(word.equals(syntaxList[50]) || word.equals(syntaxList[52]) || word.equals(syntaxList[54])){
                tokenList.add(TOKENS.TERMINATOR);
            }

            else if(word.equals(syntaxList[57]) || word.equals(syntaxList[59])){
                tokenList.add(TOKENS.OPERATION);
            }

            else if(word.equals(syntaxList[62])){
                userComments.add(userFile.nextLine());
            }

            else if(word.length() >= 3 && (word.substring(0,2).equals(syntaxList[65]))){
                tokenList.add(TOKENS.IDENTIFIER);
            }

            else{
                tokenList.add(TOKENS.LITERAL);
            }
            lexemeList.add(word);
        }
    }

    private String[] readSyntaxFile(Scanner syntaxFile){
        ArrayList<String> syntaxList = new ArrayList<>();
        while(syntaxFile.hasNextLine()){
            String line = syntaxFile.nextLine().trim();
            if(!line.isEmpty()){
                syntaxList.add(line);
            }
        }
        return syntaxList.toArray(new String[]{});
    }

    public String[] getSyntaxList(){
        return this.syntaxList;
    }
}
