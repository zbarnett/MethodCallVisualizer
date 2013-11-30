package com.bat.soloz.parserinterface;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.util.LinkedList;

/**
 * @author Zebulun Barnett
 */
public class MyClassDeclaration {
	String packageName;
	String className;
	ClassOrInterfaceType parentClass;
	LinkedList<ImportDeclaration> importsReversed;
	
	MyClassDeclaration(final ClassOrInterfaceDeclaration classDef){
		packageName = getPackageName(classDef);
		className = classDef.getName();
		parentClass = getParentClass(classDef);
		importsReversed = getImports(classDef);
		
		fullyQualifyParentClass();
	}
	
	public static LinkedList<MyClassDeclaration> getClassDeclarations(final CompilationUnit cu) {
		LinkedList<MyClassDeclaration> classDecs = new LinkedList<>();
		
		for(Node node : cu.getChildrenNodes()){
			if(node instanceof ClassOrInterfaceDeclaration)
				classDecs.add(new MyClassDeclaration((ClassOrInterfaceDeclaration)node));
		}
		return classDecs;
	}
	
	private void fullyQualifyParentClass(){ //  doesn't work for classes that don't have an import
		if(importsReversed == null || importsReversed.isEmpty())
			return;
		for(ImportDeclaration idec : importsReversed){
			String className = parentClass.getName();
			String importName = idec.getName().getName();
			if(importName.endsWith(className)){ // found match!
				parentClass.setName(importName);
				break;
			}
		}
	}
	
	private static String getPackageName(final ClassOrInterfaceDeclaration classDef){
		String subPackages = "";
		
		Node curr = classDef;
		while(!(curr instanceof CompilationUnit)) {
			curr = curr.getParentNode();
			if(curr instanceof ClassOrInterfaceDeclaration){
				subPackages += ((ClassOrInterfaceDeclaration)curr).getName() + "." + subPackages;
			}
		}
		
		// remove trailing dot
		if(!subPackages.equals(""))
			subPackages = subPackages.substring(0, subPackages.length()-1);
		
		CompilationUnit cu = (CompilationUnit)curr;
		
		if(cu.getPackage() == null)
			return subPackages;
		else if(subPackages.equals(""))
			return cu.getPackage().getName().toString();
		else
			return cu.getPackage().getName().toString() + "." + subPackages;
	}
	
	private static ClassOrInterfaceType getParentClass(final ClassOrInterfaceDeclaration classDef){
		if(classDef.getExtends() == null || classDef.getExtends().isEmpty())
			return (new ClassOrInterfaceType("Object"));
		else
			return classDef.getExtends().get(0);
	}
	
	private static LinkedList<ImportDeclaration> getImports(final ClassOrInterfaceDeclaration classDef){
		Node curr = classDef;
		
		while(!(curr instanceof CompilationUnit))
			curr = curr.getParentNode();
		
		LinkedList<ImportDeclaration> imports = (LinkedList)((CompilationUnit)curr).getImports();
		
		if(imports == null)
			return new LinkedList<>();
				
		// reverse imports so they can easily be searched in reverse order
		LinkedList<ImportDeclaration> reverse = new LinkedList();
		for(ImportDeclaration id : imports){
			reverse.addFirst(id);
		}
		
		return reverse;
	}
	
	@Override
	public String toString(){
		if(packageName.equals(""))
			return className;
		else
			return packageName + "." + className;
	}
}