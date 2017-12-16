package com.jplusplus.modules;

import java.util.ArrayList;

/**
 * Created by Joshua on 11/17/2017.
 */
public interface LexicalScannerInterface {
    public boolean hasNextToken(int index);
    public void tokenizeAll();
    public Token getToken(int index);
    public Token[] getTokens();
    public String[] getSyntaxList();
}
