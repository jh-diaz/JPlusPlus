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
public class asd {
    public static void main(String[] args) throws FileNotFoundException{
        Lexical lexicalScanner = new Lexical("src/com/jplusplus/resources/syntax.j",
                "src/com/jplusplus/resources/testinput.j" ,
                true, "src/com/jplusplus/resources/tokenlist.text");
        //Syntax syntaxScanner = new Syntax(lexicalScanner);
        /*for(int index=0; lexicalScanner.hasNextToken(index); index++){
            System.out.println(lexicalScanner.getToken(index).getTokenType());
        }*/
    }
}
