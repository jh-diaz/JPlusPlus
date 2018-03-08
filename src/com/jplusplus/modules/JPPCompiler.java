package com.jplusplus.modules;


import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

public class JPPCompiler {
    public static void main(String[] args) {
        new JPPCompiler(args[0]);
    }
    public JPPCompiler(String filePath){
        String strSyntax = this.getClass().getResource("/com/jplusplus/resources/syntax.jpp").getPath();
        strSyntax = strSyntax.substring(1);
        strSyntax = strSyntax.replaceAll("%20", " ");
        Lexical lex = new Lexical(strSyntax, filePath);
        Syntax syntax = new Syntax(lex);
        Semantic semantic = new Semantic(lex);
        //Transcompiler transcompiler = new Transcompiler(lex.getTokens());
        //runCompiler(transcompiler);
    }
    //walao weh lah no transcompiler yet lul
    /*
    private void runCompiler(Transcompiler tc) {

        String path = tc.getFile().getPath();
        String pathNoName = path.substring(0, path.length() - tc.getFile().getName().length());
        String property = System.getProperty("java.home");
        property = property.replace("jre", "jdk");
        System.setProperty("java.home", property);
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();//pls fix
        System.out.println(System.getProperty("java.home"));
        jc.run(null, null, null, path);
        File eh = tc.getFile().getParentFile();
        try {
            URLClassLoader load = URLClassLoader.newInstance(new URL[] { eh.toURI().toURL()});
            Class<?> cls = Class.forName(tc.getFile().getName().substring(0, tc.getFile().getName().length()-5), true, load);
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/
}
