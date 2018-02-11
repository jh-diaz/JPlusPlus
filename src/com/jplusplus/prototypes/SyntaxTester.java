package com.jplusplus.prototypes;

import java.util.Stack;

public class SyntaxTester {
    public static void main(String[] args) {
        Stack<String> s = new Stack<>();
        s.push("haha");
        s.push("hehe");
        s.push("lul");
        System.out.println(s.get(s.size()-2));
    }
}
