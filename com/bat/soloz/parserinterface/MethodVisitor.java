package com.bat.soloz.parserinterface;

// @author Zebulun Barnett

import com.bat.soloz.graph.MethodNode;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.LinkedList;

public class MethodVisitor extends VoidVisitorAdapter {
    private LinkedList<MethodNode> methodNodes;
    
    public MethodVisitor(){
        methodNodes = new LinkedList<>();
    }
    
    public void visit(MethodDeclaration method, Object arg){
        // process single method here
        MethodNode thisNode = new MethodNode(method.getName(), method.getBody().getStmts().size(), null, method.getBody().toString(), null);
        
        methodNodes.add(thisNode);
    }
    
    public LinkedList<MethodNode> getMethodNodes(){
        return methodNodes;
    }
}