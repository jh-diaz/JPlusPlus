package com.jplusplus.modules;

/**
 * Created by Joshua on 11/15/2017.
 */
public class Syntax {
    private LexicalScannerInterface lexicalScanner;
    private Token currentToken;
    private int currentIndex;
    private int savedIndex;
    private String[] syntaxList;

    public Syntax(LexicalScannerInterface lexicalScanner){
        this.lexicalScanner = lexicalScanner;
        currentIndex = 0;
        savedIndex = 0;
        syntaxList = lexicalScanner.getSyntaxList();
        if(variableDeclaration()){
            System.out.println("accepted");
        }
        else{
            System.out.println("rejected");
        }
    }
    //ARITHMETIC EXPR
    private boolean E(){
        System.out.println("E");
        getToken();
        System.out.println(currentToken.getData());
        return T() && Eopt();
    }

    private boolean Eopt(){
        System.out.println("EOpt");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("+") || expectedData("-")){ //lookahead
            currentIndex++;
            return T() && Eopt();
        }
        else{
            return true; //lambda
        }
    }

    private boolean T(){
        System.out.println("T");
        getToken();
        System.out.println(currentToken.getData());
        return F() && TopT();
    }

    private boolean TopT(){
        System.out.println("TopT");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedData("*") || expectedData("/")){
            currentIndex++;
            return F() && TopT();
        }
        else{
            return true; //lambda
        }
    }

    private boolean F(){
        System.out.println("F");
        getToken();
        System.out.println(currentToken.getData());
        if(expectedToken(TokenType.LITERAL) || expectedToken(TokenType.IDENTIFIER)){
            if(lexicalScanner.hasNextToken(currentIndex+1)){
                currentIndex++;
            }
            return true;
        }
        else{
            return false;
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
            getToken();
            return expectedToken(TokenType.ASSIGNMENT) && expression();
        }
        else{
            return false;
        }
    }

    private boolean expression(){
        currentIndex++;
        getToken();
        System.out.println("EXPRESSION");
        System.out.println(currentToken.getData());
        return (saveIndex() || E())
                ||
                (backtrackIndex() || relationalExpression());
    }

    private boolean relationalExpression(){
        getToken();
        System.out.println("relational expression");
        System.out.println(currentToken.getData());
        return E() && expectedToken(TokenType.RELATIONAL_OPERATOR) && E();
    }
    //VARIABLE DECLARATION

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
