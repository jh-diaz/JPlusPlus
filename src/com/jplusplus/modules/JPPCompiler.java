package com.jplusplus.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JPPCompiler {
    public static void main(String[] args) {
        new JPPCompiler(args[0]);
    }
    private final String SYNTAX_FILE_PATH = "F:\\School Folder\\Compiler\\test\\src\\com\\jplusplus\\resources\\syntax.jpp";
    public JPPCompiler(String filePath){
        new Syntax(new Lexical(SYNTAX_FILE_PATH, filePath));
    }
}
