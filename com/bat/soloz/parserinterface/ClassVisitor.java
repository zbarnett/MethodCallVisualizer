package com.bat.soloz.parserinterface;

import com.bat.soloz.graph.MethodNode;
import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Zebulun Barnett
 */
class ClassVisitor extends VoidVisitorAdapter {
	HashMap<ClassOrInterfaceDeclaration, MyClassDeclaration> classDecs;
	LinkedList<MethodNode> methodNodes;
	
	ClassVisitor() {
		classDecs = new HashMap<>();
		methodNodes = new LinkedList<>();
	}
	
	@Override
	public void visit(final ClassOrInterfaceDeclaration classDeclaration, final Object arg){
		if(!classDeclaration.isInterface()) { // skip interfaces
			MyClassDeclaration classDec = new MyClassDeclaration(classDeclaration);
		
			// add a declaration for the current class
			classDecs.put(classDeclaration, classDec);
			
			// find all methods and constructors of the current class and add them
			for(Node node : classDeclaration.getChildrenNodes()){
				if(node instanceof MethodDeclaration){
					MethodDeclaration methodDec = (MethodDeclaration)node;
					String methodName = classDec.toString() + "." + methodDec.getName();
					LinkedList<String> parameterList = new LinkedList<>();
					if(methodDec.getParameters() != null && methodDec.getParameters().size() > 0)
						for(Parameter param : methodDec.getParameters())
							parameterList.add(param.getType().toString());
					int lines = (methodDec.getBody().getStmts() == null)? 0 : methodDec.getBody().getStmts().size();
					String bodyText = methodDec.getBody().toString();
					methodNodes.add(new MethodNode(methodName, parameterList, lines, bodyText));
				} else if(node instanceof ConstructorDeclaration) {
					ConstructorDeclaration constDec = (ConstructorDeclaration)node;
					String constName = classDec.toString() + "." + constDec.getName();
					LinkedList<String> parameterList = new LinkedList<>();
					if(constDec.getParameters() != null && constDec.getParameters().size() > 0)
						for(Parameter param : constDec.getParameters())
							parameterList.add(param.getType().toString());
					int lines = (constDec.getBlock().getStmts() == null)? 0 : constDec.getBlock().getStmts().size();
					String bodyText = constDec.getBlock().toString();
					methodNodes.add(new MethodNode(constName, parameterList, lines, bodyText));
				}
			}
		}
		super.visit(classDeclaration, arg);
	}
	
	HashMap<ClassOrInterfaceDeclaration, MyClassDeclaration> getClassDeclarations() {
		return classDecs;
	}
	
	LinkedList<MethodNode> getMethodNodes() {
		return methodNodes;
	}
}