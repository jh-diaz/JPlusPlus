package com.jplusplus.ui;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class TextAreaEvents {

    public static void addTextArea(TextArea area, Text text){

        area.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String[] countStr = area.getText().split("\n");
                int count = countStr.length;
                int caretPos = area.getCaretPosition();
                text.setText(count+ "Lines, "+caretPos+" Caret Position.");
            }
        });
        area.caretPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String[] countStr = area.getText().split("\n");
                int count = countStr.length;
                int caretPos = area.getCaretPosition();
                text.setText(count+ "Lines, "+caretPos+" Caret Position.");
            }
        });
    }
}
