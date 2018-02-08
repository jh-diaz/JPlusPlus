package com.jplusplus.deprecated;

public class Syntax {
    /*private boolean declarationStatement(){
        return expectedToken(TokenType.DATA_TYPE)
                && (saveToken() | assignment() | backtrackToken() | identifier())
                && expectedToken(TokenType.LINE_TERMINATOR);
    }

    private boolean assignment(){
        return identifier() && expectedToken(TokenType.ASSIGNMENT) && expression();
    }

    private boolean identifier(){
        return expectedToken(TokenType.IDENTIFIER);
    }

    private boolean literal(){
        return expectedToken(TokenType.LITERAL);
    }

    private boolean operand(){
        returnIndex2();
        returnIndex();
        return saveToken() || expectedToken(TokenType.IDENTIFIER)
                || backtrackToken() || expectedToken(TokenType.LITERAL);
    }

    private boolean expression(){
        return arithmeticExpression() || relationalComparison() ;
    }

    private boolean arithmeticExpression(){
        if(operand()){
            int occurences = 0;
            for(; arithmeticOperators() && operand(); occurences++);
            return occurences != 0;
        }
        else{
            return false;
        }
    }

    private boolean relationalComparison(){
        return  (saveToken() | operand() | backtrackToken() | arithmeticExpression())
                && expectedToken(TokenType.RELATIONAL_OPERATOR)
                && (saveToken() | operand() | backtrackToken() | arithmeticExpression());
    }

    private boolean arithmeticOperators(){
        int index = 13;
        return (saveToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index])))
        || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || (backtrackToken() || (expectedToken(TokenType.ARITHMETIC_OPERATOR) && expectedData(syntaxList[index+2])))
                || backtrackToken();
    }

    private boolean returnIndex(){
        System.out.println(savedIndex);
        return false;
    }

    private boolean returnIndex2(){
        System.out.println(currentIndex);
        return false;
    }*/
}
