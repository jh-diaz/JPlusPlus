package com.jplusplus.ui;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stage;
    private Controller control;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        control = new Controller(new Model());
        loader.setControllerFactory(t->control);

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(this.getClass().getResource("keywords.css").toExternalForm());
        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
        new About();
        addShortcuts(scene);
    }
    public static void setTitle(String title){
        stage.setTitle(title);
    }
    private void addShortcuts(Scene scene){
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyCombRun = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
            final KeyCombination keyCombsave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            @Override
            public void handle(KeyEvent ke) {
                if(keyCombRun.match(ke)){
                    control.onRun();
                    ke.consume();
                }
                else if(keyCombsave.match(ke)){
                    control.onSave();
                    ke.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
