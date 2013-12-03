package com.bat.soloz.parserinterface;

import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;

/**
 * @author Zebulun Barnett
 */
public class SemanticUtils {
	
	static Type getTypeOfNameExpr(NameExpr nameExpr) {
		// this is where the heavy lifting happens
		// check first in current scope,
		// then parent scope,
		// so on...
		// check method parameters if in method
		// check class variables
		// check parent class variables
		// so on...
		// if not found yet.
		// variable is not delcared and we throw exception and decide this method is not worth the trouble
		
		return new ClassOrInterfaceType();
	}
	
	static ClassOrInterfaceDeclaration getThisClass(final Node node) throws Exception {
		Node curr = node;
		while(curr != null && !(curr instanceof ClassOrInterfaceDeclaration))
			curr = curr.getParentNode();
		
		if(curr == null)
			throw new Exception("Class Dec not found for node:"+node.toString());
		
		return (ClassOrInterfaceDeclaration)curr;
	}
	
	static boolean isClassType(NameExpr nameExpr) {
		// method stub
		return false;
	}
	
	static String getParameterTypes(MethodCallExpr methodCall) {
		// method stub
		return "()";
	}
}