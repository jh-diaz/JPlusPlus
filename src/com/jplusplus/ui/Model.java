package com.jplusplus.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Model {
    

    public void save(JPPFile file){
        try {
            Files.write(file.getPath(), file.getContent(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Result<JPPFile> open(Path file){
        try {
            List<String> lines = Files.readAllLines(file);
            return new Result<>(new JPPFile(file, lines), Result.Success);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<>(null, Result.Failed);
        }
    }
    public void close(){
        System.exit(0);
    }
    public void interrupt(){
        //change when compiler is done
    }
    public Result<JPPFile> run(JPPFile file){
        return new Result(file, false); //do change when compiler is done
    }
}
