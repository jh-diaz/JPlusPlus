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
                "src/com/jplusplus/resources/testinput.jpp" ,
                true, "src/com/jplusplus/resources/tokenlist.text");

        //Syntax syntaxScanner = new Syntax(lexicalScanner);
        /*for(int index=0; lexicalScanner.hasNextToken(index); index++){
            System.out.println(lexicalScanner.getToken(index).getTokenType());
        }*/

        /*Lexical lexicalScanner = new Lexical("F:\\Documents\\BS in CompSci\\Major Subjects\\6COMTHEORY\\JPlusPlus\\src\\com\\jplusplus\\resources\\syntax.jpp",
                "F:\\Documents\\BS in CompSci\\Major Subjects\\6COMTHEORY\\JPlusPlus\\src\\com\\jplusplus\\resources\\testinput.jpp" ,
                true, "F:\\Documents\\BS in CompSci\\Major Subjects\\6COMTHEORY\\JPlusPlus\\src\\com\\jplusplus\\resources\\tokenlist.text");*/
    }
}
