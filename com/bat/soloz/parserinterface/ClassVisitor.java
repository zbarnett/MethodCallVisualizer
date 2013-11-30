package com.bat.soloz.parserinterface;

import com.bat.soloz.graph.MethodNode;
import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.LinkedList;

/**
 *
 * @author Zebulun Barnett
 */
class ClassVisitor extends VoidVisitorAdapter {
	LinkedList<MyClassDeclaration> classDecs;
	LinkedList<MethodNode> methodNodes;
	
	ClassVisitor() {
		classDecs = new LinkedList<>();
		methodNodes = new LinkedList<>();
	}
	
	@Override
	public void visit(final ClassOrInterfaceDeclaration classDeclaration, final Object arg){
		if(!classDeclaration.isInterface()) { // skip interfaces
			MyClassDeclaration classDec = new MyClassDeclaration(classDeclaration);
		
			// add a declaration for the current class
			classDecs.add(classDec);
			
			// find all methods of the current class and add them
			for(Node node : classDeclaration.getChildrenNodes()){
				if(node instanceof MethodDeclaration){ // TODO: add ConstructorDeclaration(s)?
					MethodDeclaration methodDec = (MethodDeclaration)node;
					String methodName = classDec.packageName + "." + classDec.className + "." + methodDec.getName();
					int lines = (methodDec.getBody().getStmts() == null)? 0 : methodDec.getBody().getStmts().size(); // check for null
					String bodyText = methodDec.getBody().toString();
					methodNodes.add(new MethodNode(methodName, lines, bodyText));
				}
			}
		}
		super.visit(classDeclaration, arg);
	}
	
	LinkedList<MyClassDeclaration> getClassDeclarations() {
		return classDecs;
	}
	
	LinkedList<MethodNode> getMethodNodes() {
		return methodNodes;
	}
}