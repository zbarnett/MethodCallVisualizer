package com.bat.soloz.parserinterface;

// @author Zebulun Barnett

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import java.io.File;
import java.util.LinkedList;

public class ParserInterface {

    public static void main(String[] args) {
        File sourceFile = new File("test.java");
        LinkedList<MethodNode> nodes = null;
        
        try {
            nodes = analyzeSourceFile(sourceFile);
            
            printMethodNodes(nodes);
            
        } catch(Exception e) {
            System.out.println("An error has occurred while parsing the source file: " + sourceFile);
            System.out.println("The program will now terminate.");
            
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    // for debugging
    public static void printMethodNodeTree(MethodNode node, int level){
        if(node == null)
            return;
        
        String tabs = "";
        
        for(int i = 0; i < level; i++)
            tabs += "\t";
        
        System.out.print(tabs + node.getShortName());
        
        if(node.getChildren() == null)
            return;
        
        if(!node.getRecursivity().equals(RecursivityType.Normal)) {
            System.out.println(" : " + node.getRecursivity());
            return;
        }
        
        System.out.println(); 
         
        for(MethodNode child : node.getChildren()){
            printMethodNodeTree(child, level+1);
        }
    }
    
    public static void printMethodNodes(LinkedList<MethodNode> methodNodes){
        for(MethodNode methodNode : methodNodes){
            System.out.println(methodNode);
        }
    }
    
    public static LinkedList<MethodNode> analyzeSourceFile(File file) throws Exception {
       LinkedList<MethodNode> methodDeclarations = extractMethodDeclarations(file);
       LinkedList<MethodNode> linkedNodes = linkMethodNodes(methodDeclarations);
       
       return linkedNodes;
    }
    
    private static LinkedList<MethodNode> extractMethodDeclarations(File file) throws Exception {
        CompilationUnit compilationUnit = JavaParser.parse(file);
        
        MethodVisitor methodDeclarationScanner = new MethodVisitor();
        methodDeclarationScanner.visit(compilationUnit, null);
        
        LinkedList<MethodNode> methodNodes = methodDeclarationScanner.getMethodNodes();
        
        return methodNodes;
    }
    
    private static LinkedList<MethodNode> linkMethodNodes(final LinkedList<MethodNode> methodNodes){
        for(MethodNode methodNode : methodNodes) {
            String bodyText = methodNode.getBodyText();
             
            // we assume method is not recursive at this point
            methodNode.setRecursivity(RecursivityType.Normal);
             
            for(MethodNode otherMethodNode : methodNodes) {
                if(bodyText.contains(otherMethodNode.getShortName()+"(")){ // TODO: kinda hackish
                    methodNode.addChild(otherMethodNode);

                    // check for recursivity
                    if(methodNode.getLongName().equals(otherMethodNode.getLongName()))
                        methodNode.setRecursivity(RecursivityType.DirectlyRecursive);
                    
                    // TODO: add check for indirect recursion :\
                }
            }
        }

        return methodNodes;
    }
}