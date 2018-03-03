package com.jplusplus.modules;

import com.jplusplus.datatypes.DataType;
import com.jplusplus.exceptions.SemanticError;

import java.util.*;

public class Semantic {
    //private HashMap<String, Token> symbolTable;
    private List<Token> tokenList;
    private List<String> syntaxList;
    private Stack<Token> initializedIdentifiers;
    private Set<String> initializedIdentifiersSet;
    private int index = 0;

    public Semantic(LexicalScannerInterface lexical) {
        initializedIdentifiersSet = new HashSet<>();
        initializedIdentifiers = new Stack<>();
        tokenList = Arrays.asList(lexical.getTokens());
        syntaxList = Arrays.asList(lexical.getSyntaxList());
        checkSemantics();
    }

    public boolean checkSemantics() {

        for (index = 0; index < tokenList.size(); index++) {
            Token token = tokenList.get(index);
            if (token.getTokenType() == TokenType.DATA_TYPE) {
                if (checkDeclaration())
                    System.out.println("isgudlul");
            }
            if(token.getTokenType() == TokenType.FOR ||
                    token.getTokenType() == TokenType.WHILE ||
                    token.getTokenType() == TokenType.DO_WHILE ||
                    token.getTokenType() == TokenType.IF ||
                    token.getTokenType() == TokenType.ELSEIF ||
                    token.getTokenType() == TokenType.ELSE){
                initializedIdentifiers.add(token);
            }
            switch (token.getTokenType()){
                case FOR_TERMINATOR:       removeIdentifiersUntil(TokenType.FOR);      break;
                case WHILE_TERMINATOR:     removeIdentifiersUntil(TokenType.WHILE);    break;
                case DO_WHILE_TERMINATOR:  removeIdentifiersUntil(TokenType.DO_WHILE); break;
                case IF_TERMINATOR:        removeIdentifiersUntil(TokenType.IF);       break;
                case ELSEIF_TERMINATOR:    removeIdentifiersUntil(TokenType.ELSEIF);   break;
                case ELSE_TERMINATOR:      removeIdentifiersUntil(TokenType.ELSE);     break;
            }
        }

        return false;
    }
    private void removeIdentifiersUntil(TokenType type){
        while(initializedIdentifiers.peek().getTokenType()!=type)
            initializedIdentifiersSet.remove(initializedIdentifiers.pop().getData());
        initializedIdentifiersSet.remove(initializedIdentifiers.pop().getData());
    }

    private boolean checkDeclaration() {
        Token datatype = tokenList.get(index); //0
        Token variable = tokenList.get(++index); //1
        Token assignment = tokenList.get(++index); // 2
        List<Token> literalOrEquation = new ArrayList<>();
        while (tokenList.get(index).getTokenType() != TokenType.LINE_TERMINATOR)
            literalOrEquation.add(tokenList.get(++index)); //3
        literalOrEquation.remove(literalOrEquation.size() - 1);

        int syntaxIndex = 0;

        DataType type = DataType.integer;
        if (datatype.getData().equals(syntaxList.get(syntaxIndex = syntaxIndex + 2)))
            type = DataType.integer;
        else if (datatype.getData().equals(syntaxList.get(syntaxIndex = syntaxIndex + 2)))
            type = DataType.fraction;
        else if (datatype.getData().equals(syntaxList.get(syntaxIndex = syntaxIndex + 2)))
            type = DataType.nibble;
        else if (datatype.getData().equals(syntaxList.get(syntaxIndex = syntaxIndex + 2)))
            type = DataType.bool;
        else if (datatype.getData().equals(syntaxList.get(syntaxIndex = syntaxIndex + 2)))
            type = DataType.word;
        variable.setDataType(type);


        if (assignment.getTokenType() != TokenType.ASSIGNMENT)
            return false;
        if (literalOrEquation.size() > 1) {
            if (!isValidOperation(literalOrEquation, type))
                throw new SemanticError("Invalid operations.");
        }

        // int = 2, fraction = 4, nibble =6, bool = 8, word = 10
        if (type == DataType.integer) {
            if (checkInteger(literalOrEquation.get(0))) {
                if (initializedIdentifiersSet.add(variable.getData())) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.fraction) {
            if (checkFraction(literalOrEquation.get(0))) {
                if (initializedIdentifiersSet.add(variable.getData())) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.nibble) {
            if (checkNibble(literalOrEquation.get(0))) {
                if (initializedIdentifiersSet.add(variable.getData())) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.bool) {
            if (checkBoolean(literalOrEquation.get(0))) {
                if (initializedIdentifiersSet.add(variable.getData())) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.word) {
            if (checkWord(literalOrEquation.get(0))) {
                if (initializedIdentifiersSet.add(variable.getData())) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else
                    throwVariableExistsError(variable);
            }
        }
        throwMismatchError(type, variable);
        return false;
    }

    private boolean isValidOperation(List<Token> token, DataType datatype) {
        int acceptedTokenCount = 0;
        if (token.size() % 2 == 0)
            return false; // invalid declaration

        for (Token givenToken : token) {
            if (givenToken.getData().equals("+"))
                acceptedTokenCount++;
            else if (givenToken.getTokenType() == TokenType.LITERAL) {
                if (datatype == DataType.integer && !checkInteger(givenToken) ||
                        datatype == DataType.fraction && !checkFraction(givenToken) ||
                        datatype == DataType.nibble && !checkNibble(givenToken) ||
                        datatype == DataType.bool && !checkBoolean(givenToken) ||
                        datatype == DataType.word && !checkWord(givenToken))
                    throwMismatchError(datatype, givenToken);
                    //return false;

                else
                    acceptedTokenCount++;
            } else {
                for (Token initToken : initializedIdentifiers) {
                    if (initToken.getData().equals(givenToken.getData())) {
                        if (initToken.getDataType() != datatype &&
                                !((initToken.getDataType() == DataType.word && datatype == DataType.nibble) ||
                                        (initToken.getDataType() == DataType.nibble && datatype == DataType.word)))
                            throwMismatchError(datatype, givenToken);
                        acceptedTokenCount++;
                    }
                }
            }
        }
        if (acceptedTokenCount == token.size())
            return true;
        //should be impossible to get here lol
        return false;
    }

    private boolean checkInteger(Token token) {
        return token.getData().matches("\\d*");
    }

    private boolean checkFraction(Token token) {
        return token.getData().matches("(\\d+(?:\\.\\d+)?)");
    }

    private boolean checkNibble(Token token) {
        return token.getData().matches("'.'");
    }

    private boolean checkBoolean(Token token) {
        return token.getData().matches("true|false");
    }

    private boolean checkWord(Token token) {
        return token.getData().matches("\".*\"");
    }

    private void throwMismatchError(DataType type, Token token) {
        throw new SemanticError("Type mismatch, expected " + type.name() + " in line number " + token.getLineNumber() + " near " + token.getData());
    }

    private void throwVariableExistsError(Token token) {
        throw new SemanticError(token.getData() + " is already defined, in line number " + token.getLineNumber() + " near " + token.getData());
    }
}
