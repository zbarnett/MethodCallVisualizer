package com.bat.soloz.parserinterface;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zebulun Barnett
 */
class MethodCallVisitor extends VoidVisitorAdapter {
	private HashMap<String, CompilationUnit> compilationUnits;
	private LinkedList<MethodCall> methodCalls;
	
	MethodCallVisitor(HashMap<String, CompilationUnit> compilationUnits){
		this.compilationUnits = compilationUnits;
		methodCalls = new LinkedList<>();
	}
	
	@Override
	public void visit(final MethodCallExpr methodCall, final Object arg){
		super.visit(methodCall, arg);
		
		System.out.println("method call: " + methodCall);
		
		List<Node> children = methodCall.getChildrenNodes();
		
		// determine what type of method call it is
		if(children.isEmpty())
			System.out.println("\ttype: 1\n\tclass: " + findType1(methodCall));
		else if(children.get(0) instanceof ThisExpr)
			System.out.println("\ttype: 2");
		else if(children.get(0) instanceof NameExpr)
			System.out.println("\ttype: 3/4");
		else if(children.get(0) instanceof FieldAccessExpr)
			System.out.println("\ttype: 5");
	}

	@Override
	public void visit(final ObjectCreationExpr objectCall, final Object arg){
		super.visit(objectCall, arg);
		//System.out.println("constructor call: " + objectCall.getChildrenNodes().get(0));
		//System.out.println(" - scope: "+objectCall.getScope());
	}
	
	public LinkedList<MyClassDeclaration> findType1(MethodCallExpr methodCall){
		// search this class and all those it inherits from
		
		Node curr = methodCall;
		
		// run up the tree till we find the closest class def
		while(!(curr instanceof ClassOrInterfaceDeclaration))
			curr = curr.getParentNode();
		
		for(Node node : curr.getChildrenNodes())
			if(node instanceof MethodDeclaration)
				if(((MethodDeclaration)node).getName().equals(methodCall.getName())){
					// found method declaration!
					while(curr.getParentNode() != null) // run up and find CompilationUnit
						curr = curr.getParentNode();
					return MyClassDeclaration.getClassDeclarations((CompilationUnit)curr);
				}
		
		// haven't found it in this class
		// must look in parent class
//		for(String scope : compilationUnits.keySet()){
//			System.out.println(scope);
//		}
				
		
		return null;
	}
	
	LinkedList<MethodCall> getMethodCalls(){
		return methodCalls;
	}
}