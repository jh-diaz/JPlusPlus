package com.jplusplus.ui;

import javafx.scene.layout.AnchorPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeTextArea extends CodeArea {
    private static final String[] KEYWORDS = {
            "integer", "fraction", "nibble", "bool", "string",
            "input", "output",
            "if", "endif", "else", "endelse", "elseif", "endelseif",
            "cond", "while", "endwhile", "for", "endfor", "enddo"
    };
    private static final String KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final Pattern PATTERN = Pattern.compile(KEYWORDS_PATTERN);

    public CodeTextArea(){

        IntFunction<String> frmt = (digits -> " %" + digits +"d ");
        setParagraphGraphicFactory(LineNumberFactory.get(this,frmt));


        AnchorPane.setLeftAnchor(this, 5.0);
        AnchorPane.setBottomAnchor(this, 5.0);
        AnchorPane.setTopAnchor(this, 5.0);
        AnchorPane.setRightAnchor(this, 5.0);

        richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
                .subscribe(change -> {
                    setStyleSpans(0, computeHighlighting(getText()));
                });
    }
    private static StyleSpans<Collection<String>> computeHighlighting(String text){
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd =0;
        StyleSpansBuilder<Collection<String>> spb = new StyleSpansBuilder<>();

        while(matcher.find()){
            String style = "keyword";
            spb.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spb.add(Collections.singleton(style), matcher.end()-matcher.start());
            lastKwEnd = matcher.end();
        }
        spb.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spb.create();
    }
}
