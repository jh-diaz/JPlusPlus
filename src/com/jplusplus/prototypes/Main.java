package com.jplusplus.prototypes;

import com.jplusplus.modules.*;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * Created by Joshua on 11/14/2017.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        Lexical lexicalScanner = new Lexical("src/com/jplusplus/resources/syntax.jpp",
                "src/com/jplusplus/resources/arithmetic.jpp" );
        Syntax syntaxScanner = new Syntax(lexicalScanner);
        //Semantic semantic = new Semantic(lexicalScanner);
        //Transcompiler transcompiler = new Transcompiler(lexicalScanner.getTokens());

    }
}
