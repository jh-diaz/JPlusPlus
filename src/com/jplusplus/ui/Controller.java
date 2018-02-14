package com.jplusplus.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.File;
import java.util.Arrays;

public class Controller {

    private Model model;
    private JPPFile currentFile;
    @FXML private Text updates;
    @FXML private SplitPane splitPaneBottom, splitPaneLeft;
    @FXML private TabPane tabs;
    @FXML private CodeTextArea textarea;
    @FXML private AnchorPane ap, tap;


    public Controller(Model model){
        this.model = model;
    }
    @FXML
    protected void initialize(){
        textarea = new CodeTextArea();
        VirtualizedScrollPane vsp = new VirtualizedScrollPane(textarea);
        AnchorPane.setLeftAnchor(vsp, 0.0);
        AnchorPane.setBottomAnchor(vsp, 0.0);
        AnchorPane.setTopAnchor(vsp, 0.0);
        AnchorPane.setRightAnchor(vsp, 0.0);


        ap.getChildren().add(vsp);
        TextEditorEvents.addTextAreaEvents(textarea, updates);
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
                currentFile = (result.getContent());
                textarea.clear();
                currentFile.getContent().forEach(e -> textarea.appendText(e + "\n"));
            }
        }
        if(currentFile!=null)
            Main.setTitle(currentFile.getFilename());
    }
    @FXML protected void onSave(){
        if(currentFile!=null) {
            JPPFile file = new JPPFile(currentFile.getPath(), Arrays.asList(textarea.getText().split("\n")));
            currentFile = (file);
            model.save(file);
        }
        else{
            JPPFile file = new JPPFile(Arrays.asList(textarea.getText().split("\n")));
            if(model.saveNewFile(file))
                currentFile = (file);
        }
        if(currentFile!=null)
            Main.setTitle(currentFile.getFilename());
    }
    @FXML private void onClose(){
        model.close();
    }
    @FXML protected void onRun(){
        onSave();
        tabs.getTabs().add(model.run(currentFile, splitPaneBottom));
        tabs.getSelectionModel().selectLast();
    }
    @FXML private void onStop(){
        //can we even stop it?
    }
    @FXML private void onAbout(){
        String editorInfo = "JPP Text Editor.\n"+"Version: "+ About.version + "\nAbout: "+ About.about;
        String fileInfo="";
        if(currentFile!=null)
            fileInfo = "\nCurrent file is: "+currentFile.getFilename()+"\nLocation: "+currentFile.getPath();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("About");
        alert.setTitle("About");
        alert.setContentText(editorInfo + fileInfo);
        alert.show();
    }
}
