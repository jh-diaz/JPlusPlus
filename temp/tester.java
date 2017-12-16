package com.jplusplus.prototypes;

public class tester {
    public static void main(String[] args) {
   /*    Scanner userFile = null, syntaxFile = null;
        try{
            userFile = new Scanner(new FileReader("src/com/jplusplus/resources/testinput.j"));
            syntaxFile = new Scanner(new FileReader("src/com/jplusplus/resources/syntax.j"));
        }
        catch(FileNotFoundException e){
            System.out.println("Configuration files are missing. ");
        }

        LexicalScanner scanner = new LexicalScanner(userFile, syntaxFile);
        LexicalScanner.TOKENS[] tokens = scanner.tokenListAsArray();
        String[] lexemes = scanner.lexemeListAsArray();
        SyntaxAnalyzer parser = new SyntaxAnalyzer(tokens, lexemes);

        for(String error : parser.getSyntaxErrors()){
            System.out.println(error);
        }*/

        /*int size = scanner.getLength();

        System.out.printf("%-22s%-22s\n", "TOKEN", "LEXEMES");
        for(int index=0; index<size; index++){
            System.out.printf("%-22s%-22s\n", tokens[index], lexemes[index]);
        }*/

         //root node
//        Node dataType = new Node(LexicalScanner.TOKENS.DATA_TYPE);
//        Node identifier = new Node(LexicalScanner.TOKENS.IDENTIFIER);
//        Node assignment = new Node(LexicalScanner.TOKENS.ASSIGNMENT);
//        Node terminator = new Node(LexicalScanner.TOKENS.TERMINATOR);

        /*Node variableDeclaration = new Node(LexicalScanner.TOKENS.DATA_TYPE);
        Node identifier = variableDeclaration.addChild(LexicalScanner.TOKENS.IDENTIFIER);
        identifier.addChildren(new LexicalScanner.TOKENS[]{LexicalScanner.TOKENS.TERMINATOR, LexicalScanner.TOKENS.ASSIGNMENT});
        Node assignment = identifier.getChildren().get(1);
        identifier = assignment.addChild(LexicalScanner.TOKENS.IDENTIFIER);
        identifier.addChild(LexicalScanner.TOKENS.TERMINATOR);

        System.out.println("---");
        traverseTree(variableDeclaration);*/
        System.out.println("¶");
    }

    /*public static void traverseTree(Node root){
        System.out.println(root.getData());
        for(int index=0; index<root.getChildrenCount(); index++){
            traverseTree(root.getChildren().get(index));
        }
    }*/
}
