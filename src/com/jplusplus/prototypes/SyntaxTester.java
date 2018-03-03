package com.jplusplus.prototypes;

import java.util.Stack;

public class SyntaxTester {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack();
        stack.add(1);
        stack.add(2);
        stack.add(3);

        for(int x : stack){
            System.out.println(x);
        }
        System.out.println(stack.size());
    }
}
