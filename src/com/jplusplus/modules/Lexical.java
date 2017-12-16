package com.jplusplus.modules;

import com.jplusplus.exceptions.LexicalScanningException;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Joshua on 11/15/2017.
 */
public class Lexical implements LexicalScannerInterface {
    private ArrayList<Token> tokens;
    private Scanner syntaxReader;
    private Scanner userFileReader;
    private String[] syntaxList;
    private int lineNumber;

    public Lexical(String syntaxFilePath, String userFilePath){
        try{
            syntaxReader = new Scanner(new BufferedReader(new FileReader(syntaxFilePath)));
        }
        catch(Exception e){
            System.out.println("Syntax File not found.");
            System.exit(0);
        }

        try{
            userFileReader = new Scanner(new BufferedReader(new FileReader(userFilePath)));
        }
        catch(Exception e){
            System.out.println("User File not found.");
            System.exit(0);
        }
        tokens = new ArrayList<>();
        syntaxList = readSyntaxFile();
        lineNumber = 0;
        tokenizeAll();
    }

    public Lexical(String syntaxFilePath, String userFilePath, boolean debugMode, String debugPath) throws FileNotFoundException{
        this(syntaxFilePath, userFilePath);
        if(debugMode){
            printTokens(debugPath);
        }
    }

    private String[] readSyntaxFile(){
        ArrayList<String> syntaxList = new ArrayList<>();
        while(syntaxReader.hasNextLine()){
            String line = syntaxReader.nextLine().trim();
            if(!line.isEmpty()){
                syntaxList.add(line);
            }
        }
        return syntaxList.toArray(new String[]{});
    }

    public boolean hasNextToken(int index){
        return tokens.size() > index;
    }

    public Token getToken(int index){
        return tokens.get(index);
    }

    public Token[] getTokens(){
        return tokens.toArray(new Token[]{});
    }

    public void tokenizeAll(){
        while(userFileReader.hasNextLine()){
            String line = userFileReader.nextLine();
            lineNumber++;
            if(!line.isEmpty()){
                String[] words = line.trim().replaceAll("\\s+", " ").split(" ");
                for(String word : words){
                    TokenType tempType = checkTokenType(word);
                    if(tempType != null){
                        if(tempType == TokenType.COMMENT){
                            if(userFileReader.hasNextLine()){
                                userFileReader.nextLine();
                            }
                            lineNumber++;
                            break;
                        }
                        else{
                            tokens.add(new Token(tempType, word, lineNumber));
                        }
                    }
                    else{
                        //System.out.println("Unknown token found at line number " + lineNumber + " near " + word);
                        //tokens.add(new Token(lineNumber));
                       throw new LexicalScanningException("Unknown token found at line number "+ lineNumber + " near " + word);
                    }
                }
            }
        }
    }

    private TokenType checkTokenType(String data){
        int index = 0;

        if(data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])){
            return TokenType.DATA_TYPE;
        }

        else if(data.equals(syntaxList[index = index + 3])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])){
            return TokenType.ARITHMETIC_OPERATOR;
        }

        //start conditional syntax
        else if(data.equals(syntaxList[index = index + 3])){
            return TokenType.IF;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.ELSEIF;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.ELSE;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.COND;
        }
        //end conditional syntax

        else if(data.equals(syntaxList[index = index + 3])){
            return TokenType.ASSIGNMENT;
        }

        else if(data.equals(syntaxList[index = index + 3])){
            return TokenType.WHILE;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.FOR;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.DO_WHILE;
        }

        else if(data.equals(syntaxList[index = index + 3])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])
                || data.equals(syntaxList[index = index + 2])){
            return TokenType.RELATIONAL_OPERATOR;
        }

        else if(data.equals(syntaxList[index = index + 3])){
            return TokenType.IF_TERMINATOR;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.ELSEIF_TERMINATOR;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.ELSE_TERMINATOR;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.WHILE_TERMINATOR;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.DO_WHILE_TERMINATOR;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.FOR_TERMINATOR;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.LINE_TERMINATOR;
        }

        else if(data.equals(syntaxList[index = index + 3])){
            return TokenType.OUTPUT_OPERATION;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.INPUT_INTEGER_OPERATION;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.INPUT_DOUBLE_OPERATION;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.INPUT_CHARACTER_OPERATION;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.INPUT_STRING_OPERATION;
        }

        else if(data.equals(syntaxList[index = index + 2])){
            return TokenType.INPUT_BOOLEAN_OPERATION;
        }

        else if(data.equals(syntaxList[index = index + 3])){
            return TokenType.COMMENT;
        }

        else if(data.length() >= 3 && (data.substring(0,2).equals(syntaxList[index = index + 3]))){
            return TokenType.IDENTIFIER;
        }

        else if(isValidLiteral(data)){
            return TokenType.LITERAL;
        }

        else{
            return null; //invalid token
        }
    }

    private boolean isValidLiteral(String str){
        return isInteger(str) || isFraction(str) || isBool(str) || isNibble(str) || isWord(str);
    }

    private boolean isInteger(String str) {
        try{
            if(Integer.parseInt(str) <= Integer.MAX_VALUE){
                return true;
            }
            else{
                return false;
            }
        }

        catch(Exception e){
            return false;
        }
    }

    private boolean isFraction(String str) {
        try{
            if(Double.parseDouble(str) <= Double.MAX_VALUE){
                return true;
            }
            else{
                return false;
            }
        }

        catch(Exception e){
            return false;
        }
    }

    private boolean isNibble(String str){
        return str.charAt(0) == '\'' && str.charAt(str.length()-1) == '\'' && str.length() == 3;
    }

    private boolean isWord(String str){
        return str.charAt(0) == '"' && str.charAt(str.length()-1) == '"';
    }

    private boolean isBool(String str){
        return str.equals("true") || str.equals("false");
    }

    public String[] getSyntaxList(){
        return syntaxList;
    }

    private void printTokens(String debugPath) throws FileNotFoundException{
        PrintWriter writer;
        writer = new PrintWriter(debugPath);

        writer.print("TOKEN LIST");
        writer.println();

        for(Token indivToken : tokens){
            writer.println("Token type: " + indivToken.getTokenType());
            writer.println("Token data: " + indivToken.getData());
            writer.println("at line number: " + indivToken.getLineNumber());
            writer.println();
        }

        writer.close();
    }
}
