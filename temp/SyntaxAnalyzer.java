package com.jplusplus.modules;

import java.util.ArrayList;

/**
 * Created by Joshua on 11/12/2017.
 */
public class SyntaxAnalyzer {
    private static final String insufficientToken = "No remaining token near ";
    private TokenType[] tokens;
    private String[] lexemes;
    private ArrayList<String> syntaxErrors;
    private ArrayList<String> variableList;
    private int index;
    private int savedIndex;

    public SyntaxAnalyzer(TokenType[] tokens, String[] lexemes){
        this.tokens = tokens;
        this.lexemes = lexemes;
        syntaxErrors = new ArrayList<>();
        variableList = new ArrayList<>();
        index = 0;
        saveIndex();
        System.out.println(isVariableDeclaration());
    }

    private boolean isVariableDeclaration(){
        if(isDataType(tokens[index], lexemes[index])){
            if(hasRemainingTokens()){
                if(isValidIdentifier(lexemes[++index]) && tokens[index] == TokenType.IDENTIFIER){
                    variableList.add(lexemes[index]);
                    if(hasRemainingTokens()){
                        saveIndex(); //kase doble yung leaf
                        if(tokens[index] == TokenType.LINE_TERMINATOR){
                            return true;
                        }
                        else{
                            backtrackIndex();
                            if(tokens[++index] == TokenType.ASSIGNMENT){
                                if(hasRemainingTokens()){
                                    saveIndex(); //dalawang leaf ulit, literal or identifier.
                                    if(isValidLiteral(lexemes[++index]) && tokens[index] == TokenType.LITERAL){
                                        if(hasRemainingTokens()){
                                            if(tokens[index] == TokenType.LINE_TERMINATOR){
                                                return true;
                                            }
                                            else{
                                                syntaxErrors.add("Expected semicolon terminator near " + lexemes[index-1]);
                                                return false;
                                            }
                                        }
                                        else{
                                            syntaxErrors.add("Missing semicolon terminator near " + lexemes[index]);
                                            return false;
                                        }
                                    }
                                    else{
                                        //identifier
                                        backtrackIndex();
                                        if(isValidIdentifier(lexemes[++index]) && tokens[index] == TokenType.LITERAL){
                                            //check if variable does exist
                                            if(isVariableExisting(lexemes[index])){
                                                if(hasRemainingTokens()){
                                                    if(tokens[index] == TokenType.LINE_TERMINATOR){
                                                        return true;
                                                    }
                                                    else{
                                                        syntaxErrors.add("Expected semicolon terminator near " + lexemes[index-1]);
                                                        return false;
                                                    }
                                                }
                                                else{
                                                    syntaxErrors.add("Missing semicolon terminator near " + lexemes[index]);
                                                    return false;
                                                }
                                            }
                                            else{
                                                syntaxErrors.add("Assignment to unknown identifier near " + lexemes[index]);
                                                return false;
                                            }
                                        }
                                        else{
                                            syntaxErrors.add("Expected valid identifier or literal near " + lexemes[index]);
                                            return false;
                                        }
                                    }
                                }
                                else{
                                    syntaxErrors.add("Expected semicolon terminator near " + lexemes[index-1]);
                                    return false;
                                }
                            }
                            else{
                                syntaxErrors.add("Expected semicolon terminator near " + lexemes[index-1]);
                                return false;
                            }
                        }
                    }
                    else{
                        syntaxErrors.add(insufficientToken + lexemes[index]);
                        return false;
                    }
                }
                else{
                    syntaxErrors.add("Invalid identifier near " + lexemes[index]);
                    return false;
                }
            }
            else{ //ala neng laman ing token
                syntaxErrors.add(insufficientToken + lexemes[index]);
                return false;
            }
        }
        else{
            syntaxErrors.add("Invalid data type near " + lexemes[index]);
            return false;
        }
    }

    private boolean isVariableExisting(String variableName){
        for(String variable : variableList){
            if(variableName.equals(variable)){
                return true;
            }
        }
        return false; //empty variable list
    }

    private boolean hasRemainingTokens(){
        return index < lexemes.length-1;
    }

    private void saveIndex(){
        savedIndex = index;
    }

    private void backtrackIndex(){
        index = savedIndex;
    }

    private boolean isValidIdentifier(String identifier){
        if(identifier.length() >= 3){
            if(identifier.substring(0, 2).equals("__")){
                identifier = identifier.substring(2);
                return identifier.matches("[a-zA-Z]+");
            }
        }
        return false;
    }

    private boolean isDataType(TokenType token, String lexeme){
        return token == TokenType.DATA_TYPE
                && (lexeme.equals("integer") || lexeme.equals("fraction") || lexeme.equals("nibble")
                    || lexeme.equals("word") || lexeme.equals("bool"));
    }

    private boolean isSemiColon(String lexeme){
        return lexeme.equals(";");
    }

    private boolean isAssignment(TokenType token){
        return token == TokenType.ASSIGNMENT;
    }

    private boolean isValidLiteral(String lexeme){
        return isIntegerLiteral(lexeme) || isWordLiteral(lexeme)
                || isFractionLiteral(lexeme) || isCharLiteral(lexeme)
                || isBoolLiteral(lexeme);
    }

    private boolean isIntegerLiteral(String lexeme){
        try{
            Integer.parseInt(lexeme);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    private boolean isWordLiteral(String lexeme){
        return lexeme.charAt(0) == '"' && lexeme.charAt(lexeme.length()-1) == '"';
    }

    private boolean isFractionLiteral(String lexeme){
        try{
            Double.parseDouble(lexeme);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    private boolean isCharLiteral(String lexeme){
        return lexeme.charAt(0) == '\'' && lexeme.charAt(lexeme.length()-1) == '\'';
    }

    private boolean isBoolLiteral(String lexeme){
        return lexeme.equals("true") || lexeme.equals("false");
    }

    public ArrayList<String> getSyntaxErrors(){
        return syntaxErrors;
    }
}
