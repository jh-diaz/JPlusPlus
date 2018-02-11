package com.jplusplus.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class cmdCommand {
    private List<String> output = new ArrayList<>();
    String command = "cmd.exe /c java -cp \"F:/School Folder/Compiler/JPlusPlus/out/production/JPlusPlus/\" " +
            "com.jplusplus.modules.JPPCompiler ";

    public cmdCommand(String path) {
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
