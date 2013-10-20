package com.bat.soloz.ui;

import com.bat.soloz.graph.MethodNode;
import com.bat.soloz.parserinterface.ParserInterface;

import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author zebulun
 */
public class GraphView extends JPanel {
	LinkedList<VisualNode> visualNodes;
	
	GraphView(java.io.File sourceFile){
		try {
			LinkedList<MethodNode> methodNodes = ParserInterface.analyzeSourceFile(sourceFile);
			
			visualNodes = new LinkedList<>();
		
			this.setPreferredSize(new java.awt.Dimension(400, 400));
			this.setSize(new java.awt.Dimension(400, 400));
			this.setBackground(new java.awt.Color(0, 255, 0, 255));
			//this.setLayout(null);
			// TODO: lay visual nodes out in an appealing manner

			for(MethodNode methodNode : methodNodes){
				VisualNode visualNode = new VisualNode(methodNode);
				this.add(visualNode);
				visualNodes.add(visualNode);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	public void paint(java.awt.Graphics g){
//		g.setColor(new java.awt.Color(0, 0, 0, 150));
//		g.fillRect(0, 0, 10, 10);
//	}
}