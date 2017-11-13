package com.jplusplus.modules;

import java.util.ArrayList;

/**
 * Created by Joshua on 11/12/2017.
 */
public class SyntaxAnalyzer {
    private LexicalScanner.TOKENS[] tokens;
    private String[] lexemes;
    private ArrayList<String> syntaxErrors;

    public SyntaxAnalyzer(LexicalScanner.TOKENS[] tokens, String[] lexemes){
        this.tokens = tokens;
        this.lexemes = lexemes;
        syntaxErrors = new ArrayList<>();
    }

    private boolean isValidIdentifier(String identifier){
        if(identifier.substring(0, 2).equals("__")){
            identifier = identifier.substring(2);
            return identifier.matches("[a-zA-Z]+");
        }
        return false;
    }
}
