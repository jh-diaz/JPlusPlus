package com.jplusplus.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;

public class Controller {

    @FXML private TextArea textarea;
    private Model model;
    private JPPFile currentFile;
    @FXML private Text updates;

    public Controller(Model model){
        this.model = model;
    }
    @FXML
    protected void initialize(){
        TextAreaEvents.addTextArea(textarea, updates);
    }

    @FXML private void onOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setTitle("Choose a JPP File");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JPP Files (*.jpp)", "*.jpp");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            Result<JPPFile> result = model.open(file.toPath());
            if(result.isTrue() && result.hasContent()){
                currentFile = result.getContent();
                textarea.clear();
                currentFile.getContent().forEach(e -> textarea.appendText(e + "\n"));
            }
        }
    }
    @FXML private void onSave(){
        JPPFile file = new JPPFile(currentFile.getPath(), Arrays.asList(textarea.getText().split("\n")));
        model.save(file);
    }
    @FXML private void onClose(){
        model.close();
    }
    @FXML private void onRun(){

    }
    @FXML private void onStop(){

    }
    @FXML private void onAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("About");
        alert.setTitle("About");
        alert.setContentText("JPP Text Editor.\n"+"Version: "+ About.version + "\nAbout: "+ About.about);
        alert.show();

    }
}
