package com.jplusplus.prototypes;

import com.jplusplus.modules.Lexical;
import com.jplusplus.modules.Syntax;
import com.jplusplus.modules.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Joshua on 11/14/2017.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        Lexical lexicalScanner = new Lexical("src/com/jplusplus/resources/syntax.jpp",
                "src/com/jplusplus/resources/arithmetic.jpp" );
        Syntax syntaxScanner = new Syntax(lexicalScanner);
    }
}
