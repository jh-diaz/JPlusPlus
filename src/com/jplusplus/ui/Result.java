package com.jplusplus.ui;
public class Result<T> {

    public static final boolean Success = true;
    public static final boolean Failed = false;

    private T content;
    private boolean result;
    public Result(T content, boolean result){
        this.content = content;
        this.result = result;
    }
    public boolean isTrue(){
        return result;
    }
    public boolean hasContent(){return content != null; }
    public T getContent(){
        return content;
    }
}
