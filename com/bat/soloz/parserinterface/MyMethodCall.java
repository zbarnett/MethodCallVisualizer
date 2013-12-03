package com.bat.soloz.parserinterface;

import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import java.util.LinkedList;

/**
 * @author Zebulun Barnett
 */
class MyMethodCall {
	private String signature;
	private String calledFrom;
	
	MyMethodCall(MethodCallExpr methodCall) {
		this.signature = processSignature(methodCall);
		this.calledFrom = processCalledFrom(methodCall);
	}
	
	private String processSignature(MethodCallExpr methodCall) {
		LinkedList<Node> children = (LinkedList<Node>)methodCall.getChildrenNodes();
		
		// determine what type of method call it is
		if(children == null)
			System.out.println("the children are null!");
		
		if(children.getFirst() instanceof ThisExpr)
			return getSigType2(methodCall) + SemanticUtils.getParameterTypes(methodCall);
		else if(children.getFirst() instanceof SuperExpr)
			return getSigType6(methodCall) + SemanticUtils.getParameterTypes(methodCall);
		else if(children.getFirst() instanceof NameExpr)
			return getSigType34(methodCall) + SemanticUtils.getParameterTypes(methodCall);
		else if(children.getFirst() instanceof FieldAccessExpr)
			return getSigType5(methodCall) + SemanticUtils.getParameterTypes(methodCall);
		else
			return getSigType1(methodCall) + SemanticUtils.getParameterTypes(methodCall);
	}
	
	private String getSigType1(MethodCallExpr methodCall) {
		// handles: method()
		
		// first check it's own class for matching declaration
		
		return "type 1: " + methodCall.getName();
	}
	
	//	private MyClassDeclaration findType1(MethodCallExpr methodCall){
//		// search this class and all those it inherits from
//		
//		Node curr = methodCall;
//		
//		// run up the tree till we find the closest class def
//		while(!(curr instanceof ClassOrInterfaceDeclaration))
//			curr = curr.getParentNode();
//		
//		MyClassDeclaration classDec = new MyClassDeclaration((ClassOrInterfaceDeclaration)curr);
//		
//		for(Node node : curr.getChildrenNodes())
//			if(node instanceof MethodDeclaration){
//				if(((MethodDeclaration)node).getName().equals(methodCall.getName())){
//					// found method declaration!
//					// now look for corresponding method node in methodNodes list
//					for(MethodNode methodNode : methodNodes)
//						if(methodNode.getSignature().equals(""))///something
//							// link node and/or increment calls
//						
//					return classDec;
//				}
//			}
//		
//		// haven't found it in this class
//		// must look in parent class
////		for(String scope : compilationUnits.keySet()){
////			System.out.println(scope);
////		}
//				
//		
//		return null;
//	}
	
	private String getSigType2(MethodCallExpr methodCall) {
		// handles: this.method()
		try {
			ClassOrInterfaceDeclaration classDec = SemanticUtils.getThisClass(methodCall);
		} catch(Exception e){
			e.printStackTrace();
		}
		return "type 2: " + methodCall.getName();
	}
		
	private String getSigType34(MethodCallExpr methodCall) {
		// handles: Test.bad(1), instance.method()
		
		NameExpr exp = null;
		for(Node curr : methodCall.getChildrenNodes())
			if(curr instanceof NameExpr)
				exp = (NameExpr)curr;
		
		boolean isClass = SemanticUtils.isClassType(exp);
		
		if(isClass){
			// lookup class
		} else {
			// exp is a variable name...find its type
			Type variableType = SemanticUtils.getTypeOfNameExpr(exp);
		}
		
		return "type 3/4: " + methodCall.getName();
	}
				
	private String getSigType5(MethodCallExpr methodCall) {
		// handles: inst.instance.method()
		
		// hardest one...save till last
		
		return "type 5: " + methodCall.getName();
	}
	
	private String getSigType6(MethodCallExpr methodCall) {
		// handles: super.method()
		
		// to get parent class we must first get this class
		try {
			ClassOrInterfaceDeclaration thisClass = SemanticUtils.getThisClass(methodCall);
		
			if(thisClass.getExtends() != null){
				ClassOrInterfaceType parentClass = (ClassOrInterfaceType)(thisClass.getExtends().get(0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "type 6: " + methodCall.getName();
	}
	
	private String processCalledFrom(MethodCallExpr methodCall) {
		Node curr = methodCall;
		
		// find containing method
		while(!(curr instanceof MethodDeclaration) && !(curr instanceof ClassOrInterfaceDeclaration))
			curr = curr.getParentNode();
		
		if(curr instanceof ClassOrInterfaceDeclaration) {
			//System.out.println("method being called from NO METHOD! SHUT. DOWN. EVERYTHING.");
			MyClassDeclaration classDec = new MyClassDeclaration((ClassOrInterfaceDeclaration)curr);
			return classDec.toString();
		}
		
		// assume we been called by method
		MethodDeclaration methodDec = (MethodDeclaration)curr;
		String methodName = methodDec.getName();
		
		String params = "";
		for(Node curr2 : methodDec.getChildrenNodes()){
			if(curr2 instanceof Parameter)
				params += ((Parameter)curr2).getType().toString();
		}
		
		// ramp up to the nearest class declaration
		while(!(curr instanceof ClassOrInterfaceDeclaration))
			curr = curr.getParentNode();
		
		MyClassDeclaration classDec = new MyClassDeclaration((ClassOrInterfaceDeclaration)curr);
		
		return classDec.toString() + "." + methodName + "(" + params + ")";
	}
	
	String getSignature() {
		return signature;
	}
	
	@Override
	public String toString(){
		return calledFrom + " -> " + signature;
	}
}