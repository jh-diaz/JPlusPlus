package com.jplusplus.modules;

import com.jplusplus.datatypes.DataType;
import com.jplusplus.exceptions.SemanticError;

import java.util.*;

public class Semantic {
    //private HashMap<String, Token> symbolTable;
    private List<Token> tokenList;
    private List<String> syntaxList;
    private Stack<Token> initializedIdentifiers;
    private Set<Token> initializedIdentifiersSet;
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

            /*if(token.getData().matches("__\\w+(\\+\\+)|__\\w+(\\-\\-)|(\\-\\-)__\\w+|(\\+\\+)__\\w+")){
                if(!checkIncrementDecrement(token))
                    throwInvalidConditionals(token);
            }*/
            if (token.getTokenType() == TokenType.DATA_TYPE || token.getTokenType() == TokenType.IDENTIFIER) {
                if (!checkDeclaration())
                    throw new SemanticError("Invalid Declaration in line " + tokenList.get(index).getLineNumber() + " near " + tokenList.get(index).getData());
            }
            if (token.getTokenType() == TokenType.RELATIONAL_OPERATOR) {
                if (!checkRelational())
                    throwInvalidConditionals(token);
            }
            if (token.getTokenType() == TokenType.FOR ||
                    token.getTokenType() == TokenType.WHILE ||
                    token.getTokenType() == TokenType.DO_WHILE ||
                    token.getTokenType() == TokenType.IF ||
                    token.getTokenType() == TokenType.ELSEIF ||
                    token.getTokenType() == TokenType.ELSE) {
                initializedIdentifiers.add(token);
            }
            if (token.getTokenType() == TokenType.WHILE ||
                    token.getTokenType() == TokenType.IF ||
                    token.getTokenType() == TokenType.ELSEIF ||
                    token.getTokenType() == TokenType.COND ||
                    token.getTokenType() == TokenType.FOR)
                if (!checkConditionals(token))
                    throwInvalidConditionals(token);

            switch (token.getTokenType()) {
                case FOR_TERMINATOR:
                    removeIdentifiersUntil(TokenType.FOR);
                    break;
                case WHILE_TERMINATOR:
                    removeIdentifiersUntil(TokenType.WHILE);
                    break;
                case DO_WHILE_TERMINATOR:
                    removeIdentifiersUntil(TokenType.DO_WHILE);
                    break;
                case IF_TERMINATOR:
                    removeIdentifiersUntil(TokenType.IF);
                    break;
                case ELSEIF_TERMINATOR:
                    removeIdentifiersUntil(TokenType.ELSEIF);
                    break;
                case ELSE_TERMINATOR:
                    removeIdentifiersUntil(TokenType.ELSE);
                    break;
            }
        }
        return false;
    }

    /* APPARENTLY WE HAVE NO INCREMENT AND DECREMENT IN OUR RULES :(
    private boolean checkIncrementDecrement(Token token){
        String tokenData = token.getData().replaceFirst("\\+\\+|\\-\\-", "");
        Optional<Token> ot = initializedIdentifiersSet.stream().filter(t -> t.getData().equals(tokenData)).findFirst();
        if(ot.isPresent()){
            if(ot.get().getDataType() == DataType.integer || ot.get().getDataType() == DataType.fraction)
                return true;
            throwMismatchError(DataType.integer, token);
        }
        throwUndeclaredVariableError(token);
        return false;
    }*/

    private boolean checkRelational() {
        Token first = tokenList.get(--index);
        Token relational = tokenList.get(++index);
        Token second = tokenList.get(++index);


        if (first.getTokenType() == TokenType.LITERAL) {
            if (checkInteger(first))
                first.setDataType(DataType.integer);
            else if (checkBoolean(first))
                first.setDataType(DataType.bool);
            else if (checkFraction(first))
                first.setDataType(DataType.fraction);
            else if (checkWord(first))
                first.setDataType(DataType.word);
            else if (checkNibble(first))
                first.setDataType(DataType.nibble);
        } else if (first.getTokenType() == TokenType.IDENTIFIER)
            first.setDataType(initializedIdentifiersSet.stream().filter(c -> c.equals(first)).findFirst().get().getDataType());

        if (second.getTokenType() == TokenType.LITERAL) {
            if (checkInteger(second))
                second.setDataType(DataType.integer);
            if (checkBoolean(second))
                second.setDataType(DataType.bool);
            if (checkFraction(second))
                second.setDataType(DataType.fraction);
            else if (checkWord(second))
                second.setDataType(DataType.word);
            else if (checkNibble(second))
                second.setDataType(DataType.nibble);
        } else if (second.getTokenType() == TokenType.IDENTIFIER)
            second.setDataType(initializedIdentifiersSet.stream().filter(c -> c.equals(second)).findFirst().get().getDataType());
            if ((first.getDataType() == DataType.integer || first.getDataType() == DataType.fraction) &&
                    (second.getDataType() == DataType.integer || second.getDataType() == DataType.fraction))
                return true;
            if (first.getDataType() == second.getDataType())
                return true;



        throw new SemanticError("Invalid operations. Cannot compare " + first.getDataType() + " with " + second.getDataType() + " in line "+ first.getLineNumber());
    }

    private boolean checkConditionals(Token token) {
        List<Token> conditionals = new ArrayList<>();

        int localIndex = index;
        if(token.getTokenType() == TokenType.FOR) {
            while (tokenList.get(localIndex).getTokenType() != TokenType.LINE_TERMINATOR)
                localIndex++;
            localIndex++;
        }
        while (tokenList.get(localIndex).getTokenType() != TokenType.LINE_TERMINATOR) {
            conditionals.add(tokenList.get(++localIndex));
        }
        conditionals.remove(conditionals.size() - 1);
        localIndex--;

        if (conditionals.stream().filter(t -> t.getTokenType() == TokenType.RELATIONAL_OPERATOR).findFirst().isPresent() ||
                conditionals.size() == 1 && conditionals.get(0).getData().matches("true|false")) {
            return true;
        }


        return false;
    }

    private void removeIdentifiersUntil(TokenType type) {
        while (initializedIdentifiers.peek().getTokenType() != type)
            initializedIdentifiersSet.remove(initializedIdentifiers.pop());
        initializedIdentifiersSet.remove(initializedIdentifiers.pop());
    }

    private boolean checkDeclaration() {
        Token datatype = new Token(tokenList.get(index).getLineNumber());
        if (tokenList.get(index).getTokenType() == TokenType.DATA_TYPE)
            datatype = tokenList.get(index); //0
        else if (tokenList.get(index).getTokenType() == TokenType.IDENTIFIER) {
            Optional<Token> ot = initializedIdentifiersSet.stream().filter(c -> c.equals(tokenList.get(index))).findFirst();
            if (ot.isPresent())
                datatype.setData(ot.get().getData());
            //else if (tokenList.get(index).getData().matches("__\\w+(\\+\\+)|__\\w+(\\-\\-)|(\\-\\-)__\\w+|(\\+\\+)__\\w+"))
            //    datatype.setData(tokenList.get(index).getData().substring(0, tokenList.get(index).getData().length() - 2));
            else
                throwUndeclaredVariableError(tokenList.get(index));
            --index;
        }
        Token variable = tokenList.get(++index); //1
        Token assignment = tokenList.get(++index); // 2
        if (assignment.getTokenType() != TokenType.ASSIGNMENT) {
            --index;
            return true;
        }
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
        else
            type = initializedIdentifiersSet.stream().filter(t -> t.equals(variable)).findFirst().get().getDataType();
        variable.setDataType(type);


        if (assignment.getTokenType() != TokenType.ASSIGNMENT)
            return false;
        if (literalOrEquation.size() > 1) {
            if (!isValidOperation(literalOrEquation, type))
                throw new SemanticError("Invalid operations in line "+literalOrEquation.get(0).getLineNumber()+".");
        }

        // int = 2, fraction = 4, nibble =6, bool = 8, word = 10
        if (type == DataType.integer) {
            if (checkInteger(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.integer).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype.getTokenType() == null)
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.fraction) {
            if (checkFraction(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.fraction).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype.getTokenType() == null)
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.nibble) {
            if (checkNibble(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.nibble).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype.getTokenType() == null)
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.bool) {
            if (checkBoolean(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.bool).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype.getTokenType() == null)
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.word) {
            if (checkWord(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.word).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype.getTokenType() == null)
                    return true;
                else
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
            if (givenToken.getData().equals("+") || givenToken.getData().equals("-") || givenToken.getData().equals("*") ||
                    givenToken.getData().equals("/") || givenToken.getData().equals("%") || givenToken.getTokenType() == TokenType.RELATIONAL_OPERATOR)
                acceptedTokenCount++;
            else if (givenToken.getTokenType() == TokenType.LITERAL) {
                if (datatype == DataType.integer && !checkInteger(givenToken) ||
                        datatype == DataType.fraction && !checkFraction(givenToken) ||
                        datatype == DataType.nibble && !checkNibble(givenToken) ||
                        datatype == DataType.word && !checkWord(givenToken))
                    throwMismatchError(datatype, givenToken);
                    //return false;
                else if (datatype == DataType.bool && !checkBoolean(givenToken) &&
                        token.stream().filter(t -> t.getTokenType() == TokenType.RELATIONAL_OPERATOR).findFirst().isPresent())
                    acceptedTokenCount++;
                else
                    acceptedTokenCount++;
            } else {
                for (Token initToken : initializedIdentifiers) {
                    if (initToken.getData().equals(givenToken.getData())) {
                        if ((initToken.getDataType() != datatype &&
                                !((initToken.getDataType() == DataType.word && datatype == DataType.nibble) ||
                                        (initToken.getDataType() == DataType.nibble && datatype == DataType.word))) &&
                                !token.stream().filter(t -> t.getTokenType() == TokenType.RELATIONAL_OPERATOR).findFirst().isPresent())
                            throwMismatchError(datatype, givenToken);
                        acceptedTokenCount++;
                    }
                }
            }
        }
        if (acceptedTokenCount == token.size()) {
            token.forEach(t -> {
                if (t.getTokenType() != TokenType.IDENTIFIER)
                    t.setDataType(datatype);
            });
            return true;
        }
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

    private void throwUndeclaredVariableError(Token token) {
        throw new SemanticError("Undeclared variable " + token.getData() + " in line number " + token.getLineNumber());
    }

    private void throwInvalidConditionals(Token token) {
        throw new SemanticError("Wrong conditionals in line " + token.getLineNumber());
    }
}
