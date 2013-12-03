package com.bat.soloz.parserinterface;

/**
 *
 * @author Zebulun Barnett
 */
import com.bat.soloz.graph.RecursivityType;
import com.bat.soloz.graph.MethodNode;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class ParserInterface {
	public static LinkedList<MethodNode> analyzeSourceCode(final File file) throws ParseException, IOException {
		HashMap<String, CompilationUnit> compilationUnits = getCompilationUnits(file, new HashMap<String, CompilationUnit>());
		
		LinkedList<MethodNode> methodDeclarations = extractMethodDeclarations(compilationUnits);
		LinkedList<MyMethodCall> methodCalls = extractMethodCalls(compilationUnits, methodDeclarations);
		LinkedList<MethodNode> linkedNodes = linkMethodNodes(methodDeclarations, methodCalls);

		return linkedNodes;
	}
	
	private static HashMap<String, CompilationUnit> getCompilationUnits(final File file, final HashMap<String, CompilationUnit> compilationUnits) throws ParseException, IOException {
		if(file.isFile()){
			CompilationUnit cu = JavaParser.parse(file);
			cu.setData(removeExtension(file.getName())); // because there's no other way to know which freaking file this is
			//printTree(cu, "");
					
			compilationUnits.put(getCompilationUnitName(cu), cu);
		}else{
			for(File subFile : file.listFiles()){
				getCompilationUnits(subFile, compilationUnits);
			}
		}
		
		return compilationUnits;
	}
	
	private static String getCompilationUnitName(CompilationUnit cu) {
		String name = "";
		
		if(cu.getPackage() != null)
			name += cu.getPackage().getName().getName().toString() + ".";
		
		name += (String)cu.getData();
		
		return name;
	}
	
	private static final char EXTENSION_SEPARATOR = '.';
	private static final char DIRECTORY_SEPARATOR = '/';

	private static String removeExtension(String filename) {
		if (filename == null) {
			return null;
		}

		int index = indexOfExtension(filename);

		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	private static int indexOfExtension(String filename) {

		if (filename == null) {
			return -1;
		}

		// Check that no directory separator appears after the 
		// EXTENSION_SEPARATOR
		int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);

		int lastDirSeparator = filename.lastIndexOf(DIRECTORY_SEPARATOR);

		if (lastDirSeparator > extensionPos) {
			//LogIt.w(FileSystemUtil.class, "A directory separator appears after the file extension, assuming there is no file extension");
			return -1;
		}

		return extensionPos;
	}
	
	private static void printTree(final Node node, final String tabs){
		System.out.println(tabs + node.getClass().toString().substring(node.getClass().toString().lastIndexOf(".") + 1));
		
		for(Node child : node.getChildrenNodes())
			printTree(child, tabs+"\t");
	}

	private static LinkedList<MethodNode> extractMethodDeclarations(final HashMap<String, CompilationUnit> compilationUnits) throws ParseException, IOException {
		LinkedList<MethodNode> methodNodes = new LinkedList<>();
		
		for(CompilationUnit cu : compilationUnits.values()){
			ClassVisitor classDeclarationScanner = new ClassVisitor();
			classDeclarationScanner.visit(cu, null);
			
			methodNodes.addAll(classDeclarationScanner.getMethodNodes());
		}

		return methodNodes;
	}
	
	private static LinkedList<MyMethodCall> extractMethodCalls(final HashMap<String, CompilationUnit> compilationUnits, final LinkedList<MethodNode> methodNodes) throws ParseException, IOException {
		LinkedList<MyMethodCall> methodCalls = new LinkedList<>();
		
		for(CompilationUnit cu : compilationUnits.values()){
			LinkedList<MyClassDeclaration> scope = MyClassDeclaration.getClassDeclarations(cu);
		
			MethodCallVisitor methodCallScanner = new MethodCallVisitor(methodNodes);
			methodCallScanner.visit(cu, scope);
			
			methodCalls.addAll(methodCallScanner.getMethodCalls());
		}

		return methodCalls;
	}

	// totally not robust or anything.
	private static LinkedList<MethodNode> linkMethodNodes(final LinkedList<MethodNode> methodNodes, final LinkedList<MyMethodCall> methodCalls) {
		for (MethodNode methodNode : methodNodes) {
			String bodyText = methodNode.getBodyText();

			for (MethodNode otherMethodNode : methodNodes) {
				if (bodyText.contains(otherMethodNode.getShortName() + "(")) { // TODO: kinda hackish
					methodNode.addChild(otherMethodNode);

					// check for recursivity
					if (methodNode.getLongName().equals(otherMethodNode.getLongName())) {
						methodNode.setRecursivity(RecursivityType.DirectlyRecursive);
					}

					// TODO: add check for indirect recursion :\
				}
			}
		}

		return methodNodes;
	}
}