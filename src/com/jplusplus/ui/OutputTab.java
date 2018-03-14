package com.jplusplus.ui;

import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.util.List;

public class OutputTab extends Tab{
    private SplitPane splitPane;
    private AnchorPane apane;
    private BorderPane bpane;
    private CodeArea output;
    private static int total=0;
    private cmdCommand cmd;
    public OutputTab(SplitPane splitPane, cmdCommand cmd){
        this.cmd= cmd;
        total++;
        this.splitPane = splitPane;
        apane = new AnchorPane();

        output = new CodeErrorTextArea();
        output.setEditable(true);
        TextField tf = new TextField();
        VirtualizedScrollPane vsp = new VirtualizedScrollPane(output);
        AnchorPane.setLeftAnchor(vsp, 0.0);
        AnchorPane.setBottomAnchor(vsp, 0.0);
        AnchorPane.setTopAnchor(vsp, 0.0);
        AnchorPane.setRightAnchor(vsp, 0.0);
        //apane.getChildren().add(vsp);
        bpane = new BorderPane();
        bpane.setCenter(vsp);
        bpane.setBottom(tf);
        setContent(bpane);


        tf.setOnAction((value) -> {
            //cmd.writeCommand(tf.getText());
            tf.clear();
        });
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
    public CodeArea getTextArea(){
        return output;
    }
    public void setTextAreaText(List<String> content){
        content.forEach( text -> output.appendText(text+"\n"));
    }


}
