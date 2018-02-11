package com.jplusplus.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        loader.setControllerFactory(t ->new Controller(new Model()));

        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
        new About();
    }
    public static void setTitle(String title){
        stage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
