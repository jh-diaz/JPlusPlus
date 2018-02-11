package com.jplusplus.modules;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JPPCompiler {
    public static void main(String[] args) {
        new JPPCompiler(args[0]);
    }
    public JPPCompiler(String filePath){
        String syntax = this.getClass().getResource("/com/jplusplus/resources/syntax.jpp").getPath();
        syntax = syntax.substring(1);
        syntax = syntax.replaceAll("%20", " ");
        new Syntax(new Lexical(syntax, filePath));
    }
}
