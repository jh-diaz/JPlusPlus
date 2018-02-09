package com.jplusplus.modules;

import com.jplusplus.exceptions.SyntaxError;

/**
 * Created by Joshua on 11/15/2017.
 */
public class Syntax {
    private LexicalScannerInterface lexicalScanner;
    private Token currentToken;
    private int currentIndex;
    private int savedIndex;
    private int relationalCount;
    private String[] syntaxList;

    public Syntax(LexicalScannerInterface lexicalScanner){
        this.lexicalScanner = lexicalScanner;
        currentIndex = 0;
        savedIndex = 0;
        relationalCount = 0;
        syntaxList = lexicalScanner.getSyntaxList();
        if(IOOperation()){
            System.out.println("accepted");
        }
        else{
            System.out.println("rejected");
        }
    }

    private boolean hasTerminator(){
        getToken();
        return expectedToken(TokenType.DO_WHILE_TERMINATOR)
                || expectedToken(TokenType.ELSE_TERMINATOR) || expectedToken(TokenType.ELSEIF_TERMINATOR)
                || expectedToken(TokenType.IF_TERMINATOR) || expectedToken(TokenType.WHILE_TERMINATOR)
                || expectedToken(TokenType.FOR_TERMINATOR);
    }

    private boolean hasSemiColon(){
        if(!lexicalScanner.hasNextToken(currentIndex)){
            throw new SyntaxError("Missing line terminator: ; at line number " + currentToken.getLineNumber() + " near "+
                    currentToken.getData());
        }
        getToken();
        if(!expectedToken(TokenType.LINE_TERMINATOR)){
            throw new SyntaxError("Missing line terminator: ; at line number " + currentToken.getLineNumber() + " near "+
                    currentToken.getData());
        }
        else{
            return true;
        }
    }
    //ARITHMETIC EXPR
    private boolean expression(){
        relationalCount = 0;
        System.out.println("expression");
        getToken();
        System.out.println(currentToken.getData());
        return hasNextBinaryOperatorOrValue() && binaryOperatorsLowerPrec();
    }

    private boolean binaryOperatorsLowerPrec(){
        System.out.println("binaryOperatorsLowerPrec");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("+") || expectedData("-") || expectedToken(TokenType.RELATIONAL_OPERATOR)){ //lookahead
            currentIndex++;
            if(expectedToken(TokenType.RELATIONAL_OPERATOR)){
                if(relationalCount == 0){
                    relationalCount++;
                }
                else{
                    throw new SyntaxError("JPP only allows the use of one relational operator per expression" +
                            " at line number " + currentToken.getLineNumber() + " near " + currentToken.getData());
                    //return false;
                }
            }
            return hasNextBinaryOperatorOrValue() && binaryOperatorsLowerPrec();
        }
        else{
            return true; //lambda
        }
    }

    private boolean hasNextBinaryOperatorOrValue(){
        System.out.println("hasNextBinaryOperatorOrValue");
        getToken();
        System.out.println(currentToken.getData());
        return isValue() && binaryOperatorsHigherPrec();
    }

    private boolean binaryOperatorsHigherPrec(){
        System.out.println("binaryOperatorsHigherPrec");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("*") || expectedData("/")){
            currentIndex++;
            return isValue() && binaryOperatorsHigherPrec();
        }
        else{
            return true; //lambda
        }
    }

    private boolean isValue(){
        System.out.println("isValue");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedToken(TokenType.LITERAL) || expectedToken(TokenType.IDENTIFIER)){
            if(lexicalScanner.hasNextToken(currentIndex+1)){
                currentIndex++;
            }
            return true;
        }
        else{
            throw new SyntaxError("Expecting literal or identifier at line number " + currentToken.getLineNumber() + " near " +
            currentToken.getData());
            //return false;
        }
    }
    //ARITHMETIC EXPR

    //VARIABLE DECLARATION
    private boolean variableDeclaration(){
        System.out.println("VARIABLE DECLARATION");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedToken(TokenType.DATA_TYPE)){
            currentIndex++;
            return assignment() && expectedToken(TokenType.LINE_TERMINATOR);
        }
        else{
            return false;
        }
    }

    private boolean assignment(){
        System.out.println("ASSIGNMENT");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedToken(TokenType.IDENTIFIER)){
            currentIndex++;
            if(!lexicalScanner.hasNextToken(currentIndex)){
                throw new SyntaxError("Expecting assignment operator at line number " + currentToken.getLineNumber() + " near " +
                        currentToken.getData());
            }
            getToken();
            if(expectedToken(TokenType.ASSIGNMENT)){
                currentIndex++;
                return expression();
            }
            else{
                throw new SyntaxError("Expecting assignment operator at line number " + currentToken.getLineNumber() + " near " +
                        currentToken.getData());
                //return false;
            }
        }
        else{
            return false;
        }
    }

   /* private boolean expression(){
        currentIndex++;
        getToken();
        System.out.println("EXPRESSION");
        System.out.println(currentToken.getData());
        return expression();
        //skip relational exp na muna
    }*/

    /*private boolean relationalExpression(){
        getToken();
        System.out.println("relational expression");
        System.out.println(currentToken.getData());
        return expression() && expectedToken(TokenType.RELATIONAL_OPERATOR) && expression();
    }*/
    //VARIABLE DECLARATION

    //INPUT OPERATIONS
    private boolean inputOperation(){
        getToken();
        if(expectedToken(TokenType.INPUT_BOOLEAN_OPERATION) ||
                expectedToken(TokenType.INPUT_CHARACTER_OPERATION) || //look ahead
                expectedToken(TokenType.INPUT_DOUBLE_OPERATION) ||
                expectedToken(TokenType.INPUT_INTEGER_OPERATION) ||
                expectedToken(TokenType.INPUT_STRING_OPERATION)){
            currentIndex++;
            if(!lexicalScanner.hasNextToken(currentIndex)){
                throw new SyntaxError("Expecting identifier at line number " + currentToken.getLineNumber() + " near " +
                        currentToken.getData());
            }
            getToken();
            if(expectedToken(TokenType.IDENTIFIER)){
                currentIndex++;
                return true;
            }
            else{
                throw new SyntaxError("Expecting identifier at line number " + currentToken.getLineNumber() + " near " +
                        currentToken.getData());
            }
        }
        else{
            return false;

        }
    }

    private boolean outputOperation(){
        if(expectedToken(TokenType.OUTPUT_OPERATION)){
            currentIndex++;
            if(!lexicalScanner.hasNextToken(currentIndex)){
                throw new SyntaxError("Expecting literal or identifier at line number " + currentToken.getLineNumber() + " near " +
                        currentToken.getData());
            }
            getToken();
            if(expectedToken(TokenType.IDENTIFIER) || expectedToken(TokenType.LITERAL)){
                currentIndex++;
                return true;
            }
            else{
                throw new SyntaxError("Expecting literal or identifier at line number " + currentToken.getLineNumber() + " near " +
                        currentToken.getData());
                //return false;
            }
        }
        else{
            return false;
        }
    }

    private boolean IOOperation(){
        return ((saveIndex() || inputOperation()) || (backtrackIndex() || outputOperation())) && hasSemiColon();
    }
    //INPUT OUTPUT OPERATIONS

    //if statement

    private boolean ifstatement(){
        getToken();
        if(expectedToken(TokenType.IF)){
            currentIndex++;
            if(!lexicalScanner.hasNextToken(currentIndex)){
                throw new SyntaxError("Expecting expression at line number " + currentToken.getLineNumber()
                        + " near " + currentToken.getData());
            }
            getToken();
            if(expression()){
                currentIndex++;
                if(!lexicalScanner.hasNextToken(currentIndex)){
                    throw new SyntaxError("If statement body should not be empty at line number " + currentToken.getLineNumber()
                            + " near " + currentToken.getData());
                }
                getToken();
                if(blockStatements(TokenType.IF_TERMINATOR)){
                    currentIndex++;
                    getToken();
                }
                else{
                    throw new SyntaxError("If statement body should not be empty at line number " + currentToken.getLineNumber()
                            + " near " + currentToken.getData());
                }
            }
            else{
                throw new SyntaxError("Expecting expression at line number " + currentToken.getLineNumber()
                        + " near " + currentToken.getData());
            }
        }
        else{
            return false;
        }
    }

    private boolean checkRemainingTokens(TokenType tokenType){
        saveIndex();
        while(lexicalScanner.hasNextToken(currentIndex)){
            getToken();
            if(expectedToken(tokenType)){
                backtrackIndex();
                return true;
            }
            currentIndex++;
        }
        backtrackIndex();
        return false;
    }

    private boolean incrementIndex(){
        currentIndex++;
        return true;
    }

    private boolean expectedData(String data){
        return currentToken.getData().equals(data); //case sensitive lexeme/data
    }

    private boolean expectedToken(TokenType tokenType){
        return currentToken.getTokenType() == tokenType;
    }

    private void getToken(){
        if(lexicalScanner.hasNextToken(currentIndex)){
            currentToken = lexicalScanner.getToken(currentIndex);
        }
        else{
            System.out.println("Insufficient tokens at line number " + currentToken.getLineNumber() + " near " + currentToken.getData());
            System.exit(0);
        }
    }

    private boolean backtrackIndex(){
        currentIndex = savedIndex;
        saveIndex();
        return false; //para lang masabay sa return statement.
    }

    private boolean saveIndex(){
        savedIndex = currentIndex;

        return false; //para lang masabay sa return statement.
    }
}
