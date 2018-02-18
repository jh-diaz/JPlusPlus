package com.jplusplus.modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Transcompiler {
    private Token[] tokens;
    private File temp;
    private BufferedWriter writer;
    boolean isScannerCreated = false;
    boolean isOutputting = false;

    public Transcompiler(Token[] tokens){
        this.tokens = tokens;
        translateToJava();
    }
    public File getFile(){
        return temp;
    }

    private boolean translateToJava(){
        try {
            temp = File.createTempFile("jpp", ".java");
            writer = new BufferedWriter(new FileWriter(temp));
            int line=0;
            boolean ifParameter=false;
            boolean elseifParameter=false;
            boolean whileParameter=false;
            int inputCountB =0;
            int inputCountI =0;
            int inputCountD =0;
            int inputCountS =0;
            int outputCount=0;
            boolean forParameter=false;
            int condCount =0;
            createClass();
            createMain();
            for (Token token : tokens) {
                //System.out.println(token.getTokenType() + " "+token.getData() + " "+token.getLineNumber());
                while (token.getLineNumber()!=line) {
                    if(ifParameter || elseifParameter || whileParameter || forParameter){
                        ifParameter = false;
                        elseifParameter = false;
                        whileParameter = false;
                        forParameter = false;
                        writer.append("){");
                    }
                    line++;
                    writer.append("\n");

                }

                if(token.getTokenType() == TokenType.IF || ifParameter)
                    ifParameter = writeIf(token, ifParameter);
                else if(token.getTokenType() == TokenType.ELSEIF || elseifParameter)
                    elseifParameter = writeElseIf(token, elseifParameter);
                else if(token.getTokenType() == TokenType.ELSE)
                    writer.append("else{ ");
                else if(token.getTokenType() == TokenType.WHILE || whileParameter)
                    whileParameter = writeWhile(token, whileParameter);
                else if(token.getTokenType() == TokenType.INPUT_BOOLEAN_OPERATION || inputCountB!=0)
                    inputCountB = writeBooleanInput(token, inputCountB);
                else if(token.getTokenType() == TokenType.INPUT_INTEGER_OPERATION || inputCountI!=0)
                    inputCountI = writeIntegerInput(token, inputCountI);
                else if(token.getTokenType() == TokenType.INPUT_DOUBLE_OPERATION || inputCountD!=0)
                    inputCountD = writeDoubleInput(token, inputCountD);
                else if(token.getTokenType() == TokenType.INPUT_STRING_OPERATION ||
                        token.getTokenType()== TokenType.INPUT_CHARACTER_OPERATION|| inputCountS!=0)
                    inputCountS = writeStringInput(token, inputCountS);
                else if(token.getTokenType() == TokenType.OUTPUT_OPERATION || outputCount!=0)
                    outputCount = writeOutput(token, outputCount);
                else if(token.getTokenType() == TokenType.FOR || forParameter)
                    forParameter = writeFor(token, forParameter);
                else if(token.getTokenType() == TokenType.DO_WHILE)
                    writer.append("do{ ");
                else if(token.getTokenType() == TokenType.COND || condCount!=0)
                    condCount = writeCond(token, condCount);
                else if(token.getTokenType() == TokenType.DATA_TYPE)
                    writeDataType(token);
                else if(token.getTokenType() == TokenType.IF_TERMINATOR ||
                        token.getTokenType() == TokenType.WHILE_TERMINATOR ||
                        token.getTokenType() == TokenType.FOR_TERMINATOR ||
                        token.getTokenType() == TokenType.ELSEIF_TERMINATOR ||
                        token.getTokenType() == TokenType.ELSE_TERMINATOR ||
                        token.getTokenType() == TokenType.DO_WHILE_TERMINATOR)
                    writer.append("}");
                else if(token.getTokenType() == TokenType.LINE_TERMINATOR)
                    writer.append(";");
                else
                    writer.append(token.getData());

                writer.append(" ");
            }
            endFile();
            writer.close();
            return true;
        }
        catch(IOException io){
            return false;
        }
    }
    private void createClass() throws IOException{
        if(writer != null)
            writer.append("public class "+temp.getName().substring(0, temp.getName().length()-5)+"{\n");
    }
    private void createMain()throws IOException{
        if(writer != null)
            writer.append("static{\n");
    }
    private void endFile()throws IOException{
        if(writer != null)
            writer.append("\n}}");
    }
    private boolean writeIf(Token token, boolean ifParameter) throws IOException{
        if(!ifParameter) {
            writer.append(token.getData()+"(");
            ifParameter = true;
        }
        else
            writer.append(token.getData());

        return ifParameter;
    }
    private boolean writeElseIf(Token token, boolean ifParameter) throws IOException{
        if(!ifParameter) {
            writer.append("else if (");
            ifParameter=true;
        }
        else
            writer.append(token.getData());
        return ifParameter;
    }
    private boolean writeWhile(Token token, boolean ifParameter) throws IOException{
        if(!ifParameter) {
            writer.append(token.getData()+"(");
            ifParameter = true;
        }
        else
            writer.append(token.getData());
        return ifParameter;
    }
    private boolean writeFor(Token token, boolean ifParameter) throws IOException{
        if(!ifParameter) {
            writer.append(token.getData()+"(");
            ifParameter = true;
        }
        else
            writer.append(token.getData());
        return ifParameter;
    }
    private void writeScanner() throws IOException{
        writer.append("java.util.Scanner scanner = new java.util.Scanner(System.in);\n");
        isScannerCreated = true;
    }
    private int writeBooleanInput(Token token, int count) throws IOException{
        if(!isScannerCreated)
            writeScanner();
        if(token.getTokenType() == TokenType.IDENTIFIER) {
            writer.append(token.getData() + " = scanner.nextBoolean()");
            count=0;
        }
        else
            count++;
        return count;
    }
    private int writeIntegerInput(Token token, int count) throws IOException{
        if(!isScannerCreated)
            writeScanner();
        if(token.getTokenType() == TokenType.IDENTIFIER){
            writer.append(token.getData() +" = scanner.nextInt()");
            count=0;
        }
        else
            count++;
        return count;
    }
    private int writeStringInput(Token token, int count) throws IOException{
        if(!isScannerCreated)
            writeScanner();
        if(token.getTokenType() == TokenType.IDENTIFIER){
            writer.append(token.getData() +" = scanner.nextLine()");
            count=0;
        }
        else
            count++;
        return count;
    }
    private int writeDoubleInput(Token token, int count) throws IOException{
        if(!isScannerCreated)
            writeScanner();
        if(token.getTokenType() == TokenType.IDENTIFIER){
            writer.append(token.getData() +" = scanner.nextDouble()");
            count=0;
        }
        else
            count++;
        return count;
    }
    private void writePrintln() throws IOException{
        writer.append("System.out.println(\"\"");
        isOutputting = true;
    }

    private int writeOutput(Token token, int count) throws IOException{
        if(!isOutputting)
            writePrintln();
        if(token.getTokenType() == TokenType.IDENTIFIER || token.getTokenType()==TokenType.LITERAL){
            writer.append("+ "+token.getData());
            count++;
        }
        else if(token.getTokenType() == TokenType.LINE_TERMINATOR){
            count=0;
            writer.append(");");
            isOutputting = false;
        }
        else
            count++;
        return count;
    }
    private void writeDataType(Token token) throws IOException{
        if(token.getData().equals("integer"))
            writer.append("int");
        else if(token.getData().equals("nibble"))
            writer.append("char");
        else if(token.getData().equals("fraction"))
            writer.append("double");
        else if(token.getData().equals("word"))
            writer.append("String");
    }
    private int writeCond(Token token, int parameterCount) throws IOException{
        if(parameterCount==0) {
            writer.append("while(");
            parameterCount++;
        }
        else if(token.getData().equals("true") || token.getData().equals("false"))
            parameterCount=4;
        else{
            writer.append(token.getData());
            parameterCount++;
        }
        if(parameterCount==4){
            parameterCount=0;
            writer.append(")");
        }
        return parameterCount;
    }
}
