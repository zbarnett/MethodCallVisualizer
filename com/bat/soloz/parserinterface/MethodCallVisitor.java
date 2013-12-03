package com.bat.soloz.parserinterface;

import com.bat.soloz.graph.MethodNode;
import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.LinkedList;

/**
 * @author Zebulun Barnett
 */
class MethodCallVisitor extends VoidVisitorAdapter {
	private LinkedList<MyMethodCall> methodCalls;
	private LinkedList<MethodNode> methodNodes;
	
	MethodCallVisitor(LinkedList<MethodNode> methodNodes){
		this.methodNodes = methodNodes;
		methodCalls = new LinkedList<>();
	}
	
	@Override
	public void visit(final MethodCallExpr methodCall, final Object arg){
		//System.out.println("method call: " + methodCall.getName());
		
		MyMethodCall processedCall = new MyMethodCall(methodCall);
		//System.out.println("\tsignature: " + processedCall);
		
		super.visit(methodCall, arg);
	}

	@Override
	public void visit(final ObjectCreationExpr objectCall, final Object arg){
		// TODO: also add implicit super calls
		
		//System.out.println("constructor call: " + objectCall.getChildrenNodes().get(0));
		//System.out.println(" - scope: "+objectCall.getScope());
		
		super.visit(objectCall, arg);
	}
	
	LinkedList<MyMethodCall> getMethodCalls(){
		return methodCalls;
	}
}