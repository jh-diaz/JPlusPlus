package com.jplusplus.modules;

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
        int index = 0;
        while(userFile.hasNext()){
            String word = userFile.next();
            if(word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TOKENS.DATA_TYPE);
            }

            else if(word.equals(syntaxList[index = index + 3])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TOKENS.ARITHMETIC_OPERATOR);
            }

            else if(word.equals(syntaxList[index = index + 3])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TOKENS.CONDITIONAL);
            }

            else if(word.equals(syntaxList[index = index + 3])){
                tokenList.add(TOKENS.EXPRESSION);
            }

            else if(word.equals(syntaxList[index = index + 3])){
                tokenList.add(TOKENS.ITERATIVE);
            }

            else if(word.equals(syntaxList[index = index + 3])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TOKENS.RELATIONAL_OPERATOR);
            }

            else if(word.equals(syntaxList[index = index + 3])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TOKENS.TERMINATOR);
            }

            else if(word.equals(syntaxList[index = index + 3]) || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TOKENS.OPERATION);
            }

            else if(word.equals(syntaxList[index = index + 3])){
                userComments.add(userFile.nextLine());
            }

            else if(word.length() >= 3 && (word.substring(0,2).equals(syntaxList[index = index + 3]))){
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
