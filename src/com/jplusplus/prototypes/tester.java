package com.jplusplus.prototypes;

import com.jplusplus.datatypes.DataType;
import com.jplusplus.datatypes.Variable;
import com.jplusplus.datatypes.fraction;
import com.jplusplus.modules.LexicalScanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class tester {
    public static void main(String[] args) {
       /*Scanner userFile = null, syntaxFile = null;
        try{
            userFile = new Scanner(new FileReader("src/com/jplusplus/grammar/tex.text"));
            syntaxFile = new Scanner(new FileReader("src/com/jplusplus/resources/syntax.j"));
        }
        catch(FileNotFoundException e){
            System.out.println("Configuration files are missing. ");
        }

        LexicalScanner scanner = new LexicalScanner(userFile, syntaxFile);
        LexicalScanner.TOKENS[] tokens = scanner.tokenListAsArray();
        String[] lexemes = scanner.lexemeListAsArray();
        int size = scanner.getLength();

        System.out.printf("%-22s%-22s\n", "TOKEN", "LEXEMES");
        for(int index=0; index<size; index++){
            System.out.printf("%-22s%-22s\n", tokens[index], lexemes[index]);
        }*/

    }
}
