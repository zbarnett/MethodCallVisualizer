package com.bat.soloz.parserinterface;

import japa.parser.ast.expr.MethodCallExpr;

/**
 * @author Zebulun Barnett
 */
class MethodCall {
	private MyClassDeclaration scope;
	private String methodName;
	
	MethodCall(String methodName){
		this.methodName = methodName;
	}
	
	MethodCall(MethodCallExpr method){
		this.methodName = method.getName();
	}
	
	public String toString(){
		return methodName;
	}
}