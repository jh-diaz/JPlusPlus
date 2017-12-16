package com.jplusplus.parsetree;
import com.jplusplus.modules.Token;
import java.util.ArrayList;

/**
 * Created by Joshua on 11/13/2017.
 */
public class Node {
    private Token data;
    private Node parent;
    private ArrayList<Node> children;

    public Node(Token data){
        this.data = data;
        parent = null;
        children = new ArrayList<>();
    }

    public Node addChild(Token data){
        Node child = new Node(data);
        child.parent = this;
        children.add(child);

        return child;
    }

    public void addChildren(Token[] childrenData){
        for(Token childData : childrenData){
            Node child = new Node(childData);
            child.parent = this;
            children.add(child);
        }
    }

    public Token getData() {
        return data;
    }

    public void setData(Token data) {
        this.data = data;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public int getChildrenCount(){
        return children.size();
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public boolean hasChildren(){
        return children.size() != 0;
    }
}
