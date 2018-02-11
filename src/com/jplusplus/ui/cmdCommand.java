package com.jplusplus.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class cmdCommand {
    private List<String> output = new ArrayList<>();
    String compilerPath="";
    String command1 = "cmd.exe /c java -cp";
    String command2 = "com.jplusplus.modules.JPPCompiler ";

    public cmdCommand(String path) {
        compilerPath = this.getClass().getResource("/").getPath();
        compilerPath = compilerPath.substring(1);
        compilerPath = compilerPath.replaceAll("%20", " ");
        String command = command1 + " \"" + compilerPath + "\" " + command2;
        try {
            String nc = command+"\""+path+"\"";
            Process p = Runtime.getRuntime().exec(nc);
            BufferedReader brInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String input;
            while ((input = brInput.readLine()) != null)
                output.add(input);
            if (output.size() == 0) {
                while ((input = brError.readLine()) != null)
                    output.add(input);
            }

        } catch (IOException io) {
            output.add("ERROR");
        }
    }

    public List<String> getOutput() {
        return output;
    }
}
