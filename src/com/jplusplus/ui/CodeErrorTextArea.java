package com.jplusplus.ui;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Diaz, Jericho Hans
 * On 3/9/2018
 */
public class CodeErrorTextArea extends CodeArea{
    private static final String ERROR = "Exception .*|\tat .*";
    private static final Pattern PATTERN = Pattern.compile("(?<ERROR>"+ERROR+")");
    public CodeErrorTextArea(){
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
                    matcher.group("ERROR")!=null?"error":null;
            spb.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spb.add(Collections.singleton(style), matcher.end()-matcher.start());
            lastKwEnd = matcher.end();
        }
        spb.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spb.create();
    }

}
