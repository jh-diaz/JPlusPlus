package com.jplusplus.ui;

import com.jplusplus.modules.Transcompiler;
import org.fxmisc.richtext.CodeArea;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class cmdCommand {
    private volatile List<String> output = new ArrayList<>();
    private String compilerPath = "";
    private String codePath = "";
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
            boolean errored = false;
            BufferedReader brInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String input;
            while ((input = brInput.readLine()) != null) {
                if (input.matches("File: .*"))
                    codePath = input.replaceAll("File: ", "");
                else
                    output.add(input);
            }

            while ((input = brError.readLine()) != null) {
                output.add(input);
                errored = true;
            }

            int exit = p.waitFor();
            output.add("---------------------------------");
            if (!errored)
                runCode();
        } catch (Exception io) {
            io.printStackTrace();
            output.add("ERROR");
        }
    }

    public void runCode() {
        String path = codePath;
        File tc = new File(path);
        String pathNoName = path.substring(0, path.length() - tc.getName().length());
        String fileNoExtension = tc.getName().substring(0, tc.getName().length() - 5);
        //String property = System.getProperty("java.home");
        //property = property.replace("jre", "jdk");
        //System.setProperty("java.home", property);
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();//pls fix

        jc.run(null, null, null, path);
        List<String> cmd = new ArrayList<>();
        String cmd1 = "cmd.exe /c start cmd /k java -cp " + pathNoName + " " + fileNoExtension;
        System.out.println(cmd1);
        try {
            Process p = Runtime.getRuntime().exec(cmd1);

            /*Thread input = new Thread(new StreamThread("in", System.out, p.getInputStream()));
            Thread error = new Thread(new StreamThread("err", System.err, p.getErrorStream()));
            input.start();
            error.start();*/
            /*BufferedReader brInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String input;
            while ((input = brInput.readLine()) != null) {
                 output.add(input);
            }

            while ((input = brError.readLine()) != null)
                output.add(input);
            int exit = p.waitFor();*/
            Files.delete(Paths.get(path));
            //Files.delete(Paths.get(pathNoName + "\\" + fileNoExtension + ".class"));
        } catch (IOException e) {
            e.printStackTrace();
        //} catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /*public void writeCommand(String line) {
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            output.write(line);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public List<String> getOutput() {
        return output;
    }

    class StreamThread implements Runnable{

        private BufferedReader br;
        private PrintStream ps;
        private String name;

        public StreamThread(String name, PrintStream ps, InputStream stream) {
            this.name = name;
            this.ps = ps;
            br = new BufferedReader(new InputStreamReader(stream));
        }

        @Override
        public void run() {
            try {
                String input;
                while ((input = br.readLine()) != null) {
                    output.add(input);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
