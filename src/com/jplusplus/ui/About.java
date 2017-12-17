package com.jplusplus.ui;

import java.io.*;
import java.util.Properties;

public final class About {
    public static String version, about, title;

    public About(){
        try{
            InputStream in = getClass().getResourceAsStream("about.txt");
            Properties prop = new Properties();
            prop.load(in);

            version = prop.getProperty("version");
            about = prop.getProperty("about");
            title = prop.getProperty("title");


            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
