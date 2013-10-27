package com.bat.soloz.parserinterface;

/**
 *
 * @author Zebulun Barnett
 */
import com.bat.soloz.graph.RecursivityType;
import com.bat.soloz.graph.MethodNode;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.util.LinkedList;

public class ParserInterface {

	public static void printMethodNodes(final LinkedList<MethodNode> methodNodes) {
		for (MethodNode methodNode : methodNodes) {
			System.out.println(methodNode);
		}
	}

	public static LinkedList<MethodNode> analyzeSourceFile(final File file) throws Exception {
		LinkedList<MethodNode> methodDeclarations = extractMethodDeclarations(file);
		LinkedList<MethodNode> linkedNodes = linkMethodNodes(methodDeclarations);

		return linkedNodes;
	}

	private static LinkedList<MethodNode> extractMethodDeclarations(final File file) throws Exception {
		CompilationUnit compilationUnit = JavaParser.parse(file);

		MethodVisitor methodDeclarationScanner = new MethodVisitor();
		methodDeclarationScanner.visit(compilationUnit, null);

		LinkedList<MethodNode> methodNodes = methodDeclarationScanner.getMethodNodes();

		return methodNodes;
	}

	private static LinkedList<MethodNode> linkMethodNodes(final LinkedList<MethodNode> methodNodes) {
		for (MethodNode methodNode : methodNodes) {
			String bodyText = methodNode.getBodyText();

			// we assume method is not recursive at this point
			methodNode.setRecursivity(RecursivityType.NotRecursive);

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