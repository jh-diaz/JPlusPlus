package com.jplusplus.ui;

import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class OutputTab extends Tab{
    private SplitPane splitPane;
    private AnchorPane apane;
    private TextArea output;
    private static int total=0;
    public OutputTab(SplitPane splitPane){
        total++;
        this.splitPane = splitPane;
        splitPane.setDividerPositions(0.60);
        setText("atetsaedfsxc");
        apane = new AnchorPane();
        splitPane.setPickOnBounds(true);

        output = new TextArea();
        output.setEditable(true);
        apane.getChildren().add(output);
        AnchorPane.setLeftAnchor(output, 5.0);
        AnchorPane.setBottomAnchor(output, 5.0);
        AnchorPane.setTopAnchor(output, 5.0);
        AnchorPane.setRightAnchor(output, 5.0);
        setContent(apane);


        this.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if(total==1) {
                    splitPane.setDividerPositions(1);
                }
                total--;
            }
        });
    }
    public void setTextAreaText(List<String> content){
        content.forEach( text -> output.appendText(text+"\n"));
    }


}
