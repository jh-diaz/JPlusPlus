package com.jplusplus.ui;

import javafx.scene.control.SplitPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Model {
    

    public void save(JPPFile file){
        try {
            Files.write(file.getPath(), file.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveNewFile(JPPFile file){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("./"));
            fileChooser.setTitle("Save JPPFile to");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JPP Files (*.jpp)", "*.jpp");
            fileChooser.getExtensionFilters().add(filter);
            File saveFile = fileChooser.showSaveDialog(null);
            if(saveFile!=null) {
                Path path = Paths.get(saveFile.getPath());
                file.setPath(path);

                Files.write(path, file.getContent(), StandardOpenOption.CREATE_NEW);
            }
        } catch(IOException e){
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
    public OutputTab run(JPPFile file, SplitPane splitPane){
        OutputTab ot = new OutputTab(splitPane);
        ot.setText("Output ("+file.getFilename()+")");
        if(file!=null){
            cmdCommand cmd = new cmdCommand(file.getPath().toString());
            ot.setTextAreaText(cmd.getOutput());
        }
        return ot;
    }
}
