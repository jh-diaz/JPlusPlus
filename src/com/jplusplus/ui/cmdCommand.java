package com.jplusplus.ui;

import com.jplusplus.modules.Transcompiler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class cmdCommand {
    private List<String> output = new ArrayList<>();
    String compilerPath = "";

    public cmdCommand(String path) {
        String command1 = "cmd.exe /c java -cp";
        String command2 = "com.jplusplus.modules.JPPCompiler ";
        compilerPath = this.getClass().getResource("/").getPath();
        compilerPath = compilerPath.substring(1);
        compilerPath = compilerPath.replaceAll("%20", " ");
        String command = command1 + " \"" + compilerPath + "\" " + command2;
        try {
            String nc = command + "\"" + path + "\"";
            Process p = Runtime.getRuntime().exec(nc);
            BufferedReader brInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String input;
            while ((input = brInput.readLine()) != null)
                output.add(input);

            while ((input = brError.readLine()) != null)
                output.add(input);

            int exit = p.waitFor();
        } catch (Exception io) {
            output.add("ERROR");
        }
    }

    public List<String> getOutput() {
        return output;
    }
}
