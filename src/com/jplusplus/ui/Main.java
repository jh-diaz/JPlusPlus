package com.jplusplus.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        loader.setControllerFactory(t ->new Controller(new Model()));

        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
        new About();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
