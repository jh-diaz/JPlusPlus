package com.jplusplus.ui;
import java.nio.file.Path;
import java.util.List;

public class JPPFile {
    private Path path;
    private final List<String> content;

    public JPPFile(Path path, List<String> content) {
        this.path = path;
        this.content = content;
    }
    public JPPFile(List<String> content) {
        this.content = content;
        this.path = null;
    }

    public Path getPath(){
        return path;
    }
    public void setPath(Path path) { this.path = path;}
    public List<String> getContent(){
        return content;
    }
}
