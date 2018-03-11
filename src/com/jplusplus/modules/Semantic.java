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
        if (checkSemantics())
            System.out.println("Accepted Semantics.");
    }

    public boolean checkSemantics() {

        for (index = 0; index < tokenList.size(); index++) {
            Token token = tokenList.get(index);

            switch (token.getTokenType()) {
                case DATA_TYPE:
                case IDENTIFIER:
                    if (!checkDeclaration())
                        throw new SemanticError("Invalid Declaration in line " + tokenList.get(index).getLineNumber() + " near " + tokenList.get(index).getData());
                    break;
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

                case COND:

                    if (!checkConditionals(token))
                        throwInvalidConditionals(token);
                    break;
                case WHILE:
                case IF:
                case ELSEIF:
                case FOR:
                    if (!checkConditionals(token))
                        throwInvalidConditionals(token);
                case ELSE:
                case DO_WHILE:
                    initializedIdentifiers.add(token);
                    break;
                case RELATIONAL_OPERATOR:
                    if (!checkRelational())
                        throwInvalidConditionals(token);
                    break;
                case INPUT_BOOLEAN_OPERATION:
                case INPUT_DOUBLE_OPERATION:
                case INPUT_STRING_OPERATION:
                case INPUT_CHARACTER_OPERATION:
                case INPUT_INTEGER_OPERATION:
                    checkInputs(token);
            }
        }
        return true;
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

    private void checkInputs(Token token) {
        Token inputToken = token;
        Token inputVariable = tokenList.get(++index);
        if (findToken(inputVariable).isPresent())
            inputVariable = findToken(inputVariable).get();
        else
            throwUndeclaredVariableError(inputVariable);

        switch (inputToken.getTokenType()) {
            case INPUT_BOOLEAN_OPERATION:
                if (inputVariable.getDataType() != DataType.bool)
                    throwMismatchError(DataType.bool, inputToken, inputVariable);
                break;
            case INPUT_CHARACTER_OPERATION:
                if (inputVariable.getDataType() != DataType.nibble)
                    throwMismatchError(DataType.nibble, inputToken, inputVariable);
                break;
            case INPUT_DOUBLE_OPERATION:
                if (inputVariable.getDataType() != DataType.fraction)
                    throwMismatchError(DataType.fraction, inputToken, inputVariable);
                break;
            case INPUT_INTEGER_OPERATION:
                if (inputVariable.getDataType() != DataType.integer)
                    throwMismatchError(DataType.integer, inputToken, inputVariable);
                break;
            case INPUT_STRING_OPERATION:
                if (inputVariable.getDataType() != DataType.word)
                    throwMismatchError(DataType.word, inputToken, inputVariable);
                break;
        }
    }

    private boolean checkRelational() {
        int localIndex = index;
        List<Token> first = new ArrayList<>();
        while (tokenList.get(--localIndex).getTokenType() == TokenType.IDENTIFIER ||
                tokenList.get(localIndex).getTokenType() == TokenType.LITERAL ||
                tokenList.get(localIndex).getTokenType() == TokenType.ARITHMETIC_OPERATOR)
            first.add(tokenList.get(localIndex));
        Token relational = tokenList.get(index);
        localIndex = index;
        List<Token> second = new ArrayList<>();
        while (tokenList.get(++localIndex).getTokenType() == TokenType.IDENTIFIER ||
                tokenList.get(localIndex).getTokenType() == TokenType.LITERAL ||
                tokenList.get(localIndex).getTokenType() == TokenType.ARITHMETIC_OPERATOR)
            second.add(tokenList.get(localIndex));

        first.forEach(t -> {
            if (t.getTokenType() == TokenType.LITERAL) {
                if (checkInteger(t))
                    t.setDataType(DataType.integer);
                else if (checkBoolean(t))
                    t.setDataType(DataType.bool);
                else if (checkFraction(t))
                    t.setDataType(DataType.fraction);
                else if (checkWord(t))
                    t.setDataType(DataType.word);
                else if (checkNibble(t))
                    t.setDataType(DataType.nibble);
            } else if (t.getTokenType() == TokenType.IDENTIFIER)
                t.setDataType(findToken(t).get().getDataType());
        });

        second.forEach(t -> {
            if (t.getTokenType() == TokenType.LITERAL) {
                if (checkInteger(t))
                    t.setDataType(DataType.integer);
                if (checkBoolean(t))
                    t.setDataType(DataType.bool);
                if (checkFraction(t))
                    t.setDataType(DataType.fraction);
                else if (checkWord(t))
                    t.setDataType(DataType.word);
                else if (checkNibble(t))
                    t.setDataType(DataType.nibble);
            } else if (t.getTokenType() == TokenType.IDENTIFIER)
                t.setDataType(findToken(t).get().getDataType());
        });

        if (first.stream().filter(t -> t.getDataType() == DataType.word).findFirst().isPresent() &&
                second.stream().filter(t -> t.getDataType() == DataType.word).findFirst().isPresent())
            return true;

        Token op1 = new Token(first.get(0).getLineNumber());
        Token op2 = new Token(second.get(0).getLineNumber());
        for (int i = 0; i < first.size(); i++) {
            if (first.get(i).getTokenType() != TokenType.ARITHMETIC_OPERATOR)
                op1.setDataType(checkOperations(op1, first.get(i)));
        }
        for (int i = 0; i < second.size(); i++) {
            if (second.get(i).getTokenType() != TokenType.ARITHMETIC_OPERATOR)
                op2.setDataType(checkOperations(op2, second.get(i)));
        }
        if ((op1.getDataType() == DataType.bool || op2.getDataType() == DataType.bool) && !relational.getData().equals("=="))
            throw new SemanticError("Invalid operations. Boolean cannot be compared using " + relational.getData() + " in line number " + relational.getLineNumber());
        if ((op1.getDataType() == DataType.integer || op1.getDataType() == DataType.fraction) &&
                (op2.getDataType() == DataType.integer || op2.getDataType() == DataType.fraction))
            return true;
        else if (op1.getDataType() == op2.getDataType())
            return true;

        throw new SemanticError("Invalid operations. Cannot compare " + op1.getDataType() + " with " + op2.getDataType() + " in line " + first.get(0).getLineNumber());
    }

    private DataType checkOperations(Token op1, Token op2) {
        if (op1.getDataType() == null)
            return op2.getDataType();
        else if (op1.getDataType() == DataType.integer && op2.getDataType() == DataType.integer) // 1 + 1
            return DataType.integer;
        else if (op1.getDataType() == DataType.integer && op2.getDataType() == DataType.fraction) // 1 + 2.3
            return DataType.fraction;
        else if (op1.getDataType() == DataType.fraction && op2.getDataType() == DataType.fraction) // 2.3 + 2.3
            return DataType.fraction;
        else if (op1.getDataType() == DataType.fraction && op2.getDataType() == DataType.integer) // 2.3 + 1
            return DataType.fraction;
        else if (op1.getDataType() == DataType.integer && op2.getDataType() == DataType.word) //1 + "word"
            return DataType.word;
        else if (op1.getDataType() == DataType.word && op2.getDataType() == DataType.integer) //"word" + 1
            return DataType.word;
        else if (op1.getDataType() == DataType.word && op2.getDataType() == DataType.fraction) //"word" + 2.3
            return DataType.word;
        else if (op1.getDataType() == DataType.fraction && op2.getDataType() == DataType.word) //2.3 + "word"
            return DataType.word;
        else if (op1.getDataType() == DataType.word && op2.getDataType() == DataType.word) //"word" + "word"
            return DataType.word;
        else if(op1.getDataType() == DataType.word && op2.getDataType() == DataType.nibble)
            return DataType.word;
        else if(op1.getDataType() == DataType.nibble && op2.getDataType() == DataType.word)
            return DataType.word;
        else if (op1.getDataType() == DataType.bool || op2.getDataType() == DataType.bool) //true + false"
            throw new SemanticError("Cannot do operations to type Bool on line number " + op2.getLineNumber() + " near " + op2.getData());
        else if (op1.getDataType() == DataType.nibble || op2.getDataType() == DataType.nibble) //'a'+'b'
            throw new SemanticError("Cannot do operations to type Nibble on line number " + op2.getLineNumber() + " near " + op2.getData());
        throw new SemanticError("how did this even happen? DEBUG: " + op1.getDataType() + " " + op2.getDataType());
    }

    private boolean checkConditionals(Token token) {
        List<Token> conditionals = new ArrayList<>();

        int localIndex = index;
        if (token.getTokenType() == TokenType.FOR) {
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
            Optional<Token> ot = findToken(tokenList.get(index));
            if (ot.isPresent())
                datatype = ot.get();
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
            type = findToken(variable).get().getDataType();
        variable.setDataType(type);


        if (assignment.getTokenType() != TokenType.ASSIGNMENT)
            return false;
        if (literalOrEquation.size() > 1) {
            if (!isValidOperation(literalOrEquation, type))
                throw new SemanticError("Invalid operations in line " + literalOrEquation.get(0).getLineNumber() + ".");
        }

        // int = 2, fraction = 4, nibble =6, bool = 8, word = 10
        if (literalOrEquation.get(0).getTokenType() == TokenType.IDENTIFIER) {
            Optional<Token> opt = findToken(literalOrEquation.get(0));
            if (opt.isPresent()) {
                DataType eqDatatype = opt.get().getDataType();
                if (eqDatatype == variable.getDataType() ||
                        (eqDatatype == DataType.integer && variable.getDataType() == DataType.fraction ||
                                eqDatatype == DataType.fraction && variable.getDataType() == DataType.integer)){
                    if(initializedIdentifiersSet.add(variable)){
                        initializedIdentifiers.add(variable);
                        return true;
                    } else if (datatype == findToken(variable).get())
                        return true;
                    else
                        throwVariableExistsError(variable);
                }

            } else
                throwUndeclaredVariableError(literalOrEquation.get(0));
        }
        if (type == DataType.integer) {
            if (checkInteger(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.integer).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype == findToken(variable).get())
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.fraction) {
            if (checkFraction(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.fraction).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype == findToken(variable).get())
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.nibble) {
            if (checkNibble(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.nibble).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype == findToken(variable).get())
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.bool) {
            if (checkBoolean(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.bool).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype == findToken(variable).get())
                    return true;
                else
                    throwVariableExistsError(variable);
            }
        } else if (type == DataType.word) {
            if (checkWord(literalOrEquation.get(0)) || literalOrEquation.stream().filter(t -> t.getDataType() == DataType.word).findFirst().isPresent()) {
                if (initializedIdentifiersSet.add(variable)) {
                    initializedIdentifiers.add(variable);
                    return true;
                } else if (datatype == findToken(variable).get())
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

        token.forEach(t -> {
            if (t.getTokenType() == TokenType.LITERAL) {
                if (checkInteger(t))
                    t.setDataType(DataType.integer);
                else if (checkBoolean(t))
                    t.setDataType(DataType.bool);
                else if (checkFraction(t))
                    t.setDataType(DataType.fraction);
                else if (checkWord(t))
                    t.setDataType(DataType.word);
                else if (checkNibble(t))
                    t.setDataType(DataType.nibble);
            } else if (t.getTokenType() == TokenType.IDENTIFIER)
                t.setDataType(findToken(t).get().getDataType());
        });

        Token op1 = new Token(token.get(0).getLineNumber());

        token.forEach(t -> {
            if(t.getTokenType() != TokenType.ARITHMETIC_OPERATOR && t.getTokenType() != TokenType.RELATIONAL_OPERATOR)
                op1.setDataType(checkOperations(op1, t));
        });
        token.forEach(t -> {
            if (t.getTokenType() != TokenType.IDENTIFIER)
                t.setDataType(datatype);
        });
        if(datatype == op1.getDataType())
            return true;
        else if(token.stream().filter(t -> t.getTokenType() == TokenType.RELATIONAL_OPERATOR).findFirst().isPresent())
            return true;
        else
            throwMismatchError(datatype, token.get(0));
        /*for (Token givenToken : token) {
            if (givenToken.getTokenType() == TokenType.ARITHMETIC_OPERATOR || givenToken.getTokenType() == TokenType.RELATIONAL_OPERATOR)
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
            }
            else {
                if (!findToken(givenToken).isPresent())
                    throwUndeclaredVariableError(givenToken);
                for (Token initToken : initializedIdentifiersSet) {
                    Token op1 = new Token(givenToken.getLineNumber());
                    op1.setDataType(checkOperations(op1, givenToken));
                    if (initToken.getData().equals(givenToken.getData())) {
                        //if ((initToken.getDataType() != datatype && !((initToken.getDataType() == DataType.word && datatype == DataType.nibble) || (initToken.getDataType() == DataType.nibble && datatype == DataType.word))) &&
                        //        !token.stream().filter(t -> t.getTokenType() == TokenType.RELATIONAL_OPERATOR).findFirst().isPresent())
                            throwMismatchError(datatype, givenToken);
                        acceptedTokenCount++;
                    }
                }
            }
        }*/
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
        return token.getData().matches("(\\-|\\+)?\\d*");
    }

    private boolean checkFraction(Token token) {
        return token.getData().matches("(\\-|\\+)?(\\d+(?:\\.\\d+)?)");
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

    private Optional<Token> findToken(Token toFind) {
        Optional<Token> tk = initializedIdentifiersSet.stream().filter(t -> t.equals(toFind)).findFirst();
        if (tk.isPresent())
            return tk;
        else
            throwUndeclaredVariableError(toFind);
        return null;
    }

    private void throwMismatchError(DataType type, Token token) {
        throw new SemanticError("Type mismatch, expected " + type.name() + " in line number " + token.getLineNumber() + " near " + token.getData());
    }

    private void throwMismatchError(DataType type, Token line, Token token) {
        throw new SemanticError("Type mismatch, expected " + type.name() + " in line number " + line.getLineNumber() + " near " + token.getData());
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

    private void throwIncomaptibleTypes(Token token, Token token2) {
        throw new SemanticError("Incompatible Types between " + token.getData() + "(" + token.getDataType() + ") and " + token2.getData() + "(" + token2.getDataType() + ") in line number " + token.getLineNumber());
    }
}
