package com.jplusplus.modules;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Joshua on 11/7/2017.
 */
public class LexicalScanner {
    private ArrayList<TokenType> tokenList;
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

    public ArrayList<TokenType> getTokenList() {
        return tokenList;
    }

    public ArrayList<String> getLexemeList() {
        return lexemeList;
    }

    public int getLength(){
        return tokenList.size();
    }

    public TokenType[] tokenListAsArray(){
        return tokenList.toArray(new TokenType[]{});
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
                tokenList.add(TokenType.DATA_TYPE);
            }

            else if(word.equals(syntaxList[index = index + 3])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TokenType.ARITHMETIC_OPERATOR);
            }

            else if(word.equals(syntaxList[index = index + 3])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TokenType.CONDITIONAL);
            }

            else if(word.equals(syntaxList[index = index + 3])){
                tokenList.add(TokenType.ASSIGNMENT);
            }

            else if(word.equals(syntaxList[index = index + 3])){
                tokenList.add(TokenType.ITERATIVE);
            }

            else if(word.equals(syntaxList[index = index + 3])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])
                    || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TokenType.RELATIONAL_OPERATOR);
            }

            else if(word.equals(syntaxList[index = index + 3])){
                tokenList.add(TokenType.IF_TERMINATOR);
            }

            else if(word.equals(syntaxList[index = index + 2])){
                tokenList.add(TokenType.WHILE_TERMINATOR);
            }

            else if(word.equals(syntaxList[index = index + 2])){
                tokenList.add(TokenType.LINE_TERMINATOR);
            }

            else if(word.equals(syntaxList[index = index + 3]) || word.equals(syntaxList[index = index + 2])){
                tokenList.add(TokenType.IO_OPERATION);
            }

            else if(word.equals(syntaxList[index = index + 3])){
                userComments.add(userFile.nextLine());
            }

            else if(word.length() >= 3 && (word.substring(0,2).equals(syntaxList[index = index + 3]))){
                tokenList.add(TokenType.IDENTIFIER);
            }

            else{
                tokenList.add(TokenType.LITERAL);
            }
            lexemeList.add(word);
            index = 0;
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
