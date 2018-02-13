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
            "integer", "fraction", "nibble", "bool", "word",
            "input", "output",
            "if", "endif", "else", "endelse", "elseif", "endelseif",
            "cond", "while", "endwhile", "for", "endfor","do", "enddo"
    };
    private static final String KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String VARIABLES = "\\b__[a-zA-Z]*\\b";
    private static final String WORDS = "\".*\"";
    private static final String TERMINATOR = ";";
    private static final String COMMENTS = "\\\\.* | //.*";
    private static final String NUMBERS = "\\d";
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORDS_PATTERN + ")"
                    + "|(?<VARS>"+VARIABLES+")"
                    + "|(?<TERMINATOR>"+TERMINATOR+")"
                    + "|(?<COMMENTS>"+COMMENTS+")"
                    + "|(?<NUMBERS>"+NUMBERS+")"
                    + "|(?<WORDS>"+WORDS+")");

    public CodeTextArea(){

        IntFunction<String> frmt = (digits -> " %" + digits +"d ");
        setParagraphGraphicFactory(LineNumberFactory.get(this, frmt));


        richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .subscribe(change -> {
                    setStyleSpans(0, computeHighlighting(getText()));
                });
    }
    private static StyleSpans<Collection<String>> computeHighlighting(String text){
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd =0;
        StyleSpansBuilder<Collection<String>> spb = new StyleSpansBuilder<>();

        while(matcher.find()){
            String style =
                    matcher.group("KEYWORD")!=null?"keyword":
                            matcher.group("VARS")!=null?"vars":
                            matcher.group("TERMINATOR")!=null?"terminator":
                            matcher.group("COMMENTS")!=null?"comments":
                            matcher.group("NUMBERS")!=null?"numbers":
                            matcher.group("WORDS")!=null?"words":null;
            spb.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spb.add(Collections.singleton(style), matcher.end()-matcher.start());
            lastKwEnd = matcher.end();
        }
        spb.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spb.create();
    }
}
