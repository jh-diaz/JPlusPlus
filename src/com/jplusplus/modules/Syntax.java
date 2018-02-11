package com.jplusplus.modules;

import com.jplusplus.exceptions.SyntaxError;

import java.util.Stack;

/**
 * Created by Joshua on 11/15/2017.
 */
public class Syntax {
    private LexicalScannerInterface lexicalScanner;
    private Token currentToken;
    private int currentIndex;
    private int savedIndex;
    private int relationalCount;
    private int ifNestedCount;
    private String[] syntaxList;
    private Stack<TokenType> matchingTerminators;
    private Stack<Integer> tokenIndices;

    public Syntax(LexicalScannerInterface lexicalScanner){
        this.lexicalScanner = lexicalScanner;
        currentIndex = 0;
        savedIndex = 0;
        relationalCount = 0;
        syntaxList = lexicalScanner.getSyntaxList();
        matchingTerminators = new Stack<>();
        tokenIndices = new Stack<>();
        if(blockStatements()){
            System.out.println("accepted");
        }
        else{
            syntaxError("Ambiguous syntax error");
        }
    }

    //VARIABLE DECLARATION
    private boolean variableDeclaration(){
        //System.out.println("VARIABLE DECLARATION");
        getToken();
        //System.out.println(currentToken.getData());
        if(expectedToken(TokenType.DATA_TYPE)){
            currentIndex++;
            return assignment() && hasSemiColon();
        }
        else{
            return false;
        }
    }
    //VARIABLE DECLARATION

    //ASSIGNMENT
    private boolean assignment(){
        //System.out.println("ASSIGNMENT");
        getToken("Expecting identifier");
        //System.out.println(currentToken.getData());
        if(expectedToken(TokenType.IDENTIFIER)){
            currentIndex++;
            getToken("Expecting assignment operator");
            if(expectedToken(TokenType.ASSIGNMENT)){
                currentIndex++;

                return expression() && hasSemiColon();
            }
            else{
                syntaxError("Expecting assignment operator");

                return false;
            }
        }
        else{
            return false;
        }
    }
    //ASSIGNMENT

    //INPUT OUTPUT OPERATIONS
    private boolean inputOperation(){
        getToken();
        if(expectedToken(TokenType.INPUT_BOOLEAN_OPERATION) ||
                expectedToken(TokenType.INPUT_CHARACTER_OPERATION) || //look ahead
                expectedToken(TokenType.INPUT_DOUBLE_OPERATION) ||
                expectedToken(TokenType.INPUT_INTEGER_OPERATION) ||
                expectedToken(TokenType.INPUT_STRING_OPERATION)){
            currentIndex++;
            getToken("Expecting identifier");
            if(expectedToken(TokenType.IDENTIFIER)){
                currentIndex++;

                return true;
            }
            else{
                syntaxError("Expecting identifier");

                return false;
            }
        }
        else{
            return false;
        }
    }
    private boolean outputOperation(){
        getToken();
        if(expectedToken(TokenType.OUTPUT_OPERATION)){
            currentIndex++;
            getToken("Expecting literal or identifier");
            if(expectedToken(TokenType.IDENTIFIER) || expectedToken(TokenType.LITERAL)){
                currentIndex++;

                return true;
            }
            else{
                syntaxError("Expecting literal or identifier");

                return false;
            }
        }
        else{
            return false;
        }
    }
    private boolean IOOperation(){
        //return ((saveIndex() || inputOperation()) || (backtrackIndex() || outputOperation())) && hasSemiColon();
        return (inputOperation() || outputOperation()) && hasSemiColon();
    }
    //INPUT OUTPUT OPERATIONS

    //BLOCK STATEMENTS
    private boolean blockStatements(){
        getToken();
        boolean mismatch = false;
        boolean hasIf = false;
        for(; hasNextToken(); currentIndex++){
            getToken();
            if(expectedToken(TokenType.IF) || expectedToken(TokenType.ELSEIF) || expectedToken(TokenType.ELSE)
                    || expectedToken(TokenType.WHILE) || expectedToken(TokenType.DO_WHILE) || expectedToken(TokenType.FOR)){ //lahat ng block statements
                if(expectedToken(TokenType.IF)){
                    ifNestedCount++;
                    hasIf = true;
                    matchingTerminators.push(currentToken.getTokenType());
                    tokenIndices.push(currentIndex);
                }
                else if(expectedToken(TokenType.ELSEIF)){
                    if(hasIf){
                        saveIndex();
                        currentIndex--; //check yung nasa TAAS, dapat walang nakapagitan na statement sa if >... < elseif
                        getToken();
                        if(expectedToken(TokenType.IF_TERMINATOR) || expectedToken(TokenType.ELSEIF_TERMINATOR)){
                            backtrackIndex();
                            getToken();
                            matchingTerminators.push(currentToken.getTokenType());
                            tokenIndices.push(currentIndex);
                        }
                        else{
                            syntaxError("Not expecting a statement between if elseif clause");
                            System.exit(0);
                        }
                    }
                    else{
                        syntaxError("Elseif without matching if");
                        System.exit(0);
                    }
                }
                else if(expectedToken(TokenType.ELSE)){
                    if(hasIf){
                        ifNestedCount--;
                        if(ifNestedCount != 0){
                            hasIf = true;
                        }
                        else{
                            hasIf = false; //ends if elseif else clause
                        }
                        saveIndex();
                        currentIndex--; //check yung nasa TAAS, dapat walang nakapagitan na statement sa if >... < elseif
                        getToken();
                        if(expectedToken(TokenType.IF_TERMINATOR) || expectedToken(TokenType.ELSEIF_TERMINATOR)){
                            backtrackIndex();
                            getToken();
                            matchingTerminators.push(currentToken.getTokenType());
                            tokenIndices.push(currentIndex);
                        }
                        else{
                            syntaxError("Not expecting a statement between if elseif else clause");
                            System.exit(0);
                        }
                    }
                    else{
                        syntaxError("Else without matching if");
                        System.exit(0);
                    }
                }
                else{
                    matchingTerminators.push(currentToken.getTokenType());
                    tokenIndices.push(currentIndex);
                   //hasIf = false;
                }
            }
            else if(matchingTerminators.size() == 0 &&
                    (expectedToken(TokenType.IF_TERMINATOR)
                            || expectedToken(TokenType.ELSEIF_TERMINATOR)
                            || expectedToken(TokenType.ELSE_TERMINATOR)
                            || expectedToken(TokenType.WHILE_TERMINATOR)
                            || expectedToken(TokenType.DO_WHILE_TERMINATOR)
                            || expectedToken(TokenType.FOR_TERMINATOR))
                    ){
                syntaxError("No block start for terminator");
                System.exit(0);
            }
            else if(expectedToken(TokenType.IF_TERMINATOR)){
                if(matchingTerminators.peek() == TokenType.IF){
                    matchingTerminators.pop();
                    tokenIndices.pop();
                }
                else
                    mismatch = true;
            }
            else if(expectedToken(TokenType.ELSEIF_TERMINATOR)){
                if(matchingTerminators.peek() == TokenType.ELSEIF){
                    matchingTerminators.pop();
                    tokenIndices.pop();
                }
                else
                    mismatch = true;
            }
            else if(expectedToken(TokenType.ELSE_TERMINATOR)){
                if(matchingTerminators.peek() == TokenType.ELSE){
                    matchingTerminators.pop();
                    tokenIndices.pop();
                }
                else
                    mismatch = true;
            }
            else if(expectedToken(TokenType.WHILE_TERMINATOR)){
                if(matchingTerminators.peek() == TokenType.WHILE){
                    matchingTerminators.pop();
                    tokenIndices.pop();
                }
                else
                    mismatch = true;
            }
            else if(expectedToken(TokenType.DO_WHILE_TERMINATOR)){
                if(matchingTerminators.peek() == TokenType.DO_WHILE){
                    matchingTerminators.pop();
                    tokenIndices.pop();
                }
                else
                    mismatch = true;
            }
            else if(expectedToken(TokenType.FOR_TERMINATOR)){
                if(matchingTerminators.peek() == TokenType.FOR){
                    matchingTerminators.pop();
                    tokenIndices.pop();
                }
                else
                    mismatch = true;
            }

            if(mismatch){
                syntaxError("Terminator mismatch for " + matchingTerminators.pop());
                System.exit(0);
            }
        }
        if(matchingTerminators.size() != 0){
            syntaxError("Missing terminator for " + matchingTerminators.pop(), tokenIndices.pop());
        }
        currentIndex = 0;

        return true;
    }
    //BLOCK STATEMENTS

    private boolean lineStatement(){
        return variableDeclaration() || assignment() || IOOperation();
    }
    //EXPRESSION
    private boolean expression(){
        relationalCount = 0;
        //System.out.println("expression");
        getToken("Expecting expression");
        //System.out.println(currentToken.getData());
        return hasNextBinaryOperatorOrValue() && binaryOperatorsLowerPrec();
    }
    private boolean binaryOperatorsLowerPrec(){
        //System.out.println("binaryOperatorsLowerPrec");
        getToken();
        //System.out.println(currentToken.getData());
        if(expectedData("+") || expectedData("-") || expectedToken(TokenType.RELATIONAL_OPERATOR)){ //lookahead
            currentIndex++;
            if(expectedToken(TokenType.RELATIONAL_OPERATOR)){
                if(relationalCount == 0){
                    relationalCount++;
                }
                else{
                    syntaxError("JPP only allows the use of one relational operator per expression");
                    return false;
                }
            }
            return hasNextBinaryOperatorOrValue() && binaryOperatorsLowerPrec();
        }
        else{
            return true; //lambda
        }
    }
    private boolean hasNextBinaryOperatorOrValue(){
        //System.out.println("hasNextBinaryOperatorOrValue");
        getToken("Expecting literal or identifier");
        //System.out.println(currentToken.getData());
        return isValue() && binaryOperatorsHigherPrec();
    }
    private boolean binaryOperatorsHigherPrec(){
        //System.out.println("binaryOperatorsHigherPrec");
        getToken();
        //System.out.println(currentToken.getData());
        if(expectedData("*") || expectedData("/") || expectedData("%")){
            currentIndex++;
            return isValue() && binaryOperatorsHigherPrec();
        }
        else{
            return true; //lambda
        }
    }
    private boolean isValue(){
        //System.out.println("isValue");
        getToken();
        //System.out.println(currentToken.getData());
        if(expectedToken(TokenType.LITERAL) || expectedToken(TokenType.IDENTIFIER)){
            if(lexicalScanner.hasNextToken(currentIndex+1)){
                currentIndex++;
            }
            return true;
        }
        else{
            syntaxError("Expecting literal or identifier");
            return false;
        }
    }
    //EXPRESSION

    //LINE TERMINATOR
    private boolean hasSemiColon(){
        /*if(!lexicalScanner.hasNextToken(currentIndex)){
            throw new SyntaxError("Missing line terminator: ; at line number " + currentToken.getLineNumber() + " near "+
                    currentToken.getData());
        }*/
        getToken("Missing line terminator: ;");
        if(!expectedToken(TokenType.LINE_TERMINATOR)){
            syntaxError("Missing line terminator: ;");
            return false;
        }
        else{
            return true;
        }
    }
    //LINE TERMINATOR

    //HELPER METHODS
    private boolean expectedData(String data){
        return currentToken.getData().equals(data); //case sensitive lexeme/data
    }
    private boolean expectedToken(TokenType tokenType){
        return currentToken.getTokenType() == tokenType;
    }
    private void syntaxError(String customMsg){
        throw new SyntaxError(customMsg + " at line number " + currentToken.getLineNumber() + " near " +
                currentToken.getData());
    }
    private void syntaxError(String customMsg, int tokenIndex){
        throw new SyntaxError(customMsg + " at line number " + lexicalScanner.getToken(tokenIndex).getLineNumber() + " near " +
                lexicalScanner.getToken(tokenIndex).getData());
    }
    private void getToken(String errorMsg){
        if(hasNextToken()){
            currentToken = lexicalScanner.getToken(currentIndex);
        }
        else{
            syntaxError(errorMsg);
            System.exit(0);
        }
    }
    private void getToken(){
        if(hasNextToken()){
            currentToken = lexicalScanner.getToken(currentIndex);
        }
        else{
            syntaxError("Insufficient tokens");
            System.exit(0);
        }
    }
    private boolean hasNextToken(){
        return lexicalScanner.hasNextToken(currentIndex);
    }
    //HELPER METHODS

    private boolean backtrackIndex(){
        currentIndex = savedIndex;
        saveIndex();

        return false; //para lang masabay sa return statement.
    }

    private boolean saveIndex(){
        savedIndex = currentIndex;

        return false; //para lang masabay sa return statement.
    }

    /*private boolean ifClause(){
        getToken();
        if(expectedToken(TokenType.IF)){
            currentIndex++;
            getToken("Expecting expression");
            if(expression()){
                //dagdag current index or not
                saveIndex();
                boolean hasIfTerminator = false;
                boolean overlappedClause = false;
                for(; hasNextToken(); currentIndex++){
                    getToken();
                    if(expectedToken(TokenType.ELSEIF) || expectedToken(TokenType.ELSEIF_TERMINATOR)
                            || expectedToken(TokenType.ELSE) || expectedToken(TokenType.ELSE_TERMINATOR)){
                            overlappedClause = true;
                            break;
                    }
                    else if(expectedToken(TokenType.IF_TERMINATOR)){
                        hasIfTerminator = true; //what if may statement sa gitna ng if elseif else clause?
                        break;
                    }
                }
                if(overlappedClause){
                    syntaxError("If clause should be terminated first");
                    return false;
                }
                else if(hasIfTerminator){

                }
                else{
                    syntaxError("Missing if terminator ");
                }

            }
            else{
                syntaxError("Expecting expression");
                return false;
            }
        }
        else{
            return false;
        }
    }*/
}